package com.zoronime

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import java.net.URLDecoder

data class ZoroniMeDownload(
    @JsonProperty("qualityList") val qualityList: List<ZoroniMeQuality> = emptyList()
)

data class ZoroniMeQuality(
    @JsonProperty("title") val title: String = "",
    @JsonProperty("urlList") val urlList: List<ZoroniMeUrl> = emptyList()
)

data class ZoroniMeUrl(
    @JsonProperty("title") val title: String = "",
    @JsonProperty("url") val url: String = ""
)

class ZoroniMe : MainAPI() {
    override var mainUrl = "https://hdoboxapp.org"
    override var name = "Zoronime"
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA
    )

    override val mainPage = mainPageOf(
        "$mainUrl/ongoing?page=%d" to "Sedang Tayang",
        "$mainUrl/completed?page=%d" to "Selesai"
    )

    // Decode Next.js /_next/image?url=ENCODED_URL&w=...&q=... to real poster URL
    private fun extractNextImageUrl(srcAttr: String): String? {
        return Regex("""url=(https?[^&\s]+)""").find(srcAttr)?.groupValues?.get(1)?.let {
            try { URLDecoder.decode(it, "UTF-8") } catch (_: Exception) { it }
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = request.data.replace("%d", "$page")
        val document = app.get(url).document

        val cards = document.select("a.anime-card").mapNotNull { el ->
            val href = el.absUrl("href").takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val title = el.selectFirst("h3")?.text()?.trim() ?: return@mapNotNull null
            val imgEl = el.selectFirst("img[data-nimg]")
            val poster = imgEl?.attr("srcset")?.let { extractNextImageUrl(it) }
                ?: imgEl?.attr("src")?.let { extractNextImageUrl(it) }
            newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }

        // Find the last page number from visible pagination links
        val lastPage = document.select("a[href*='page=']")
            .mapNotNull { Regex("page=(\\d+)").find(it.attr("href"))?.groupValues?.get(1)?.toIntOrNull() }
            .maxOrNull() ?: 1

        return newHomePageResponse(request.name, cards, hasNext = page < lastPage)
    }

    // Search from the full A-Z anime list (all 1700+ titles in one page)
    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/anime").document
        return document.select("a[href^='/anime/']").mapNotNull { el ->
            val titleEl = el.selectFirst("span") ?: return@mapNotNull null
            val title = titleEl.text().trim()
            if (!title.contains(query, ignoreCase = true)) return@mapNotNull null
            val href = el.absUrl("href").takeIf { it.isNotBlank() } ?: return@mapNotNull null
            newAnimeSearchResponse(title, href, TvType.Anime)
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document

        // Extract from JSON-LD TVSeries schema (has direct poster URL, title, genres, etc.)
        val tvSeriesJson = document.select("script[type='application/ld+json']")
            .mapNotNull { it.data().takeIf { d -> d.contains("\"TVSeries\"") } }
            .firstOrNull()

        // Extract fields from JSON-LD string via regex (no external JSON library needed)
        fun String?.jsonStr(key: String) = this?.let {
            Regex(""""$key"\s*:\s*"((?:[^"\\]|\\.)*)"""").find(it)?.groupValues?.get(1)
        }

        val title = tvSeriesJson.jsonStr("name")
            ?: document.selectFirst("h1")?.text()?.trim() ?: ""
        val japName = tvSeriesJson.jsonStr("alternateName")
        val poster = tvSeriesJson.jsonStr("image")
        val genres = tvSeriesJson?.let {
            Regex(""""genre"\s*:\s*\[(.*?)\]""").find(it)?.groupValues?.get(1)
                ?.let { arr -> Regex(""""([^"]+)"""").findAll(arr).map { m -> m.groupValues[1] }.toList() }
        } ?: emptyList()

        // Status: ongoing badge has bg-green-500, completed has other colors
        val status = if (document.selectFirst("span[class*='bg-green-500']") != null)
            ShowStatus.Ongoing else ShowStatus.Completed

        // Synopsis: find h2 with "Sinopsis" text, take its next sibling's text
        val synopsisH2 = document.select("h2").firstOrNull { it.text().contains("Sinopsis", ignoreCase = true) }
        val synopsis = synopsisH2?.nextElementSibling()?.text()?.trim()
            ?: synopsisH2?.parent()?.nextElementSibling()?.text()?.trim()

        // Episodes: listed newest-first, reverse to ascending order
        val episodes = document.select("a[href*='/episode/']")
            .distinctBy { it.attr("href") }
            .reversed()
            .mapIndexed { idx, el ->
                val epUrl = el.absUrl("href")
                val epNum = Regex("-episode-(\\d+)").find(epUrl)?.groupValues?.get(1)?.toIntOrNull() ?: (idx + 1)
                newEpisode(epUrl) {
                    episode = epNum
                    name = "Episode $epNum"
                }
            }

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = synopsis
            this.tags = genres
            this.showStatus = status
            japName?.let { this.japName = it }
            addEpisodes(DubStatus.Subbed, episodes)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val html = app.get(data).text

        // Find the RSC flight push block that contains the download quality list
        val pushPattern = Regex("""self\.__next_f\.push\(\[1,"((?:[^"\\]|\\.)*)"\]\)""")
        val inner = pushPattern.findAll(html)
            .map { it.groupValues[1] }
            .firstOrNull { it.contains("qualityList") } ?: return false

        // Unescape the JSON-encoded string (Next.js RSC encoding)
        val unescaped = inner
            .replace("\\\\", "\u0001")  // temp: protect literal backslash
            .replace("\\\"", "\"")
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t")
            .replace("\u0001", "\\")

        // Locate "download":{ and extract its JSON object via brace counting
        val dlIdx = unescaped.indexOf("\"download\":{")
        if (dlIdx < 0) return false

        val objStart = dlIdx + "\"download\":".length
        var depth = 0
        var objEnd = objStart
        for (i in objStart until unescaped.length) {
            when (unescaped[i]) {
                '{' -> depth++
                '}' -> {
                    depth--
                    if (depth == 0) { objEnd = i + 1; break }
                }
            }
        }
        if (objEnd <= objStart) return false

        val dlJson = unescaped.substring(objStart, objEnd)
        val dlData = try {
            parseJson<ZoroniMeDownload>(dlJson)
        } catch (_: Exception) { return false }

        for (quality in dlData.qualityList) {
            val qualityInt = Regex("(\\d{3,4})p").find(quality.title)?.groupValues?.get(1)?.toIntOrNull()
                ?: Qualities.Unknown.value

            for (urlEntry in quality.urlList) {
                val rawUrl = urlEntry.url

                // Pixeldrain single-file link: /u/{id} → direct stream via /api/file/{id}
                val pixelMatch = Regex("""pixeldrain\.com/u/([A-Za-z0-9]+)""").find(rawUrl)
                if (pixelMatch != null) {
                    callback(
                        newExtractorLink(
                            source = name,
                            name = "$name [${quality.title}]",
                            url = "https://pixeldrain.com/api/file/${pixelMatch.groupValues[1]}",
                            type = ExtractorLinkType.VIDEO
                        ) {
                            this.quality = qualityInt
                        }
                    )
                } else {
                    loadExtractor(rawUrl, data, subtitleCallback, callback)
                }
            }
        }

        return true
    }
}

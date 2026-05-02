package com.alqanime

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import org.jsoup.nodes.Element
import java.net.URLDecoder

class Alqanime : MainAPI() {
    override var mainUrl = "https://alqanime.net"
    override var name = "Alqanime"
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA
    )

    companion object {
        fun getType(t: String): TvType = when {
            t.contains("Movie", true) -> TvType.AnimeMovie
            t.contains("OVA", true) || t.contains("Special", true) -> TvType.OVA
            else -> TvType.Anime
        }

        fun getStatus(t: String): ShowStatus = when {
            t.contains("Completed", true) || t.contains("Tamat", true) -> ShowStatus.Completed
            t.contains("Ongoing", true) -> ShowStatus.Ongoing
            else -> ShowStatus.Completed
        }
    }

    private val commonHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Language" to "en-US,en;q=0.5",
        "Referer" to mainUrl,
    )

    override val mainPage = mainPageOf(
        "$mainUrl/page/%d/" to "Rilisan Terbaru",
        "$mainUrl/advanced-search/page/%d/?status=ongoing&order=update" to "Sedang Tayang",
        "$mainUrl/advanced-search/page/%d/?status=completed&order=update" to "Selesai Tayang",
        "$mainUrl/advanced-search/page/%d/?type[]=movie&order=update" to "Film Layar Lebar",
        "$mainUrl/popular/page/%d/" to "Popular",
        "$mainUrl/tag/action/page/%d/" to "Action",
        "$mainUrl/tag/romance/page/%d/" to "Romance",
        "$mainUrl/tag/fantasy/page/%d/" to "Fantasy",
        "$mainUrl/tag/comedy/page/%d/" to "Comedy",
        "$mainUrl/tag/school/page/%d/" to "School",
        "$mainUrl/tag/isekai/page/%d/" to "Isekai",
        "$mainUrl/tag/shounen/page/%d/" to "Shounen",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data.format(page), headers = commonHeaders).document
        val selector = "div.listupd:not(.popularslider) article.bs"
        val home = document.select(selector).mapNotNull { it.toSearchResult() }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): AnimeSearchResponse? {
        val href = fixUrlNull(this.selectFirst("a")?.attr("href")) ?: return null
        val title = this.selectFirst(".ntitle")?.text()?.trim() ?: return null
        val posterUrl = fixUrlNull(this.selectFirst("img")?.attr("src"))
        val typeText = this.selectFirst(".typez")?.text()?.trim() ?: ""
        val epNum = this.selectFirst("a")?.attr("title")
            ?.let { Regex("Episode\\s*\\((\\d+)\\)", RegexOption.IGNORE_CASE).find(it)?.groupValues?.getOrNull(1)?.toIntOrNull() }
        val rating = this.selectFirst("div.numscore")?.text()?.trim()
        return newAnimeSearchResponse(title, href, getType(typeText)) {
            this.posterUrl = posterUrl
            addDubStatus("Sub Indo", epNum)
            this.score = Score.from10(rating)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query", headers = commonHeaders).document
        return document.select("article.bs").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url, headers = commonHeaders).document

        val rawTitle = document.selectFirst("h1.entry-title")?.text()?.trim() ?: return null
        val title = rawTitle
            .replace(Regex("\\s*\\(Episode[^)]*\\)", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*Sub Indo\\b.*", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*\\(BD\\).*", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s*BD Batch.*", RegexOption.IGNORE_CASE), "")
            .trim()

        val poster = document.selectFirst("div.thumb img")?.attr("src")
        val coverBg = document.selectFirst("div.ime img")?.attr("src")
        val trailerRaw = document.selectFirst("a.trailerbutton")?.attr("href")
        val trailer = trailerRaw?.let { url ->
            val videoId = Regex("[?&]v=([^&]+)").find(url)?.groupValues?.getOrNull(1)
            if (videoId != null) "https://www.youtube.com/embed/$videoId" else url
        }
        val description = document.select("div.entry-content > p")
            .filter { it.text().length > 10 }
            .joinToString("\n\n") { it.text().trim() }
            .ifBlank { null }
        val genres = document.select("div.genxed a").map { it.text() }

        val speMap = document.select("div.spe > span").associate { span ->
            val label = span.selectFirst("b")?.text()?.trim() ?: ""
            val value = span.text().replace(label, "").trim()
            label to value
        }

        val status = getStatus(speMap.entries.find { it.key.contains("Status", true) }?.value ?: "")
        val typeText = speMap.entries.find { it.key.contains("Tipe", true) }?.value ?: ""
        val type = getType(typeText)
        val year = Regex("(\\d{4})").find(
            speMap.entries.find { it.key.contains("Dirilis", true) }?.value ?: ""
        )?.groupValues?.getOrNull(1)?.toIntOrNull()

        val japName = document.selectFirst("span.alter")?.text()?.trim()
            ?.split(",")?.firstOrNull()?.trim()?.trimStart('-')?.trimEnd('-')?.trim()
        val studio = document.selectFirst("div.spe > span:contains(Studio) a")?.text()?.trim()
        val season = document.selectFirst("div.spe > span:contains(Musim) a")?.text()?.trim()
        val duration = Regex("(\\d+)\\s*min").find(
            speMap.entries.find { it.key.contains("Durasi", true) }?.value ?: ""
        )?.groupValues?.getOrNull(1)?.toIntOrNull()
        val actors = document.select("div.spe span:contains(Casts) a.casts")
            .map { Actor(it.text()) }
        val scoreText = document.selectFirst("strong:contains(Score)")?.text()
            ?.replace("Score", "")?.trim()

        val episodes = mutableListOf<Episode>()
        for (col in document.select("div.sorattl.collapsible")) {
            val epTitle = col.selectFirst("h3")?.text()?.trim() ?: continue
            if (epTitle.equals("Batch", ignoreCase = true)) continue
            val epNum = Regex("Episode\\s*(\\d+)", RegexOption.IGNORE_CASE)
                .find(epTitle)?.groupValues?.getOrNull(1)?.toIntOrNull()
            val contentDiv = col.nextElementSibling()
                ?.takeIf { it.hasClass("content") } ?: continue

            val pixeldrainFolderIds = mutableListOf<String>()
            for (tr in contentDiv.select("tr")) {
                for (a in tr.select("div.slink a")) {
                    val resolved = resolveUrl(a.attr("href"))
                    val listId = Regex("pixeldrain\\.com/l/([A-Za-z0-9]+)").find(resolved)?.groupValues?.get(1)
                    if (listId != null) pixeldrainFolderIds.add(listId)
                }
            }

            if (pixeldrainFolderIds.isNotEmpty()) {
                val epMap = mutableMapOf<Int, MutableList<EpisodeLink>>()
                val epThumbs = mutableMapOf<Int, String>()
                for (listId in pixeldrainFolderIds) {
                    try {
                        val apiJson = app.get("https://pixeldrain.com/api/list/$listId")
                            .parsedSafe<PixeldrainList>()
                        apiJson?.files
                            ?.filter { it.mimeType.startsWith("video/") }
                            ?.sortedBy { it.name }
                            ?.forEach { file ->
                                val fileEpNum = Regex("(?:_|-)0*(\\d+)(?:_|-)").find(file.name)
                                    ?.groupValues?.get(1)?.toIntOrNull() ?: return@forEach
                                val fileQuality = Regex("(\\d{3,4})p", RegexOption.IGNORE_CASE)
                                    .find(file.name)?.groupValues?.get(1) ?: ""
                                val streamUrl = "https://pixeldrain.com/api/file/${file.id}"
                                epMap.getOrPut(fileEpNum) { mutableListOf() }
                                    .add(EpisodeLink(streamUrl, fileQuality))
                                if (!epThumbs.containsKey(fileEpNum))
                                    epThumbs[fileEpNum] = "https://pixeldrain.com/api/file/${file.id}/thumbnail"
                            }
                    } catch (_: Exception) { }
                }
                for ((epNum, links) in epMap.toSortedMap()) {
                    episodes.add(newEpisode(links.toJson()) {
                        this.name = "Episode $epNum"
                        this.episode = epNum
                        this.posterUrl = epThumbs[epNum]
                    })
                }
            }

            if (pixeldrainFolderIds.isEmpty()) {
                val linkList = mutableListOf<EpisodeLink>()
                for (tr in contentDiv.select("tr")) {
                    val quality = tr.selectFirst("div.res")?.text()?.trim() ?: continue
                    for (a in tr.select("div.slink a")) {
                        linkList.add(EpisodeLink(a.attr("href"), quality))
                    }
                }
                if (linkList.isNotEmpty()) {
                    episodes.add(newEpisode(linkList.toJson()) {
                        this.name = epTitle
                        this.episode = epNum
                    })
                }
            }
        }

        return newAnimeLoadResponse(title, url, type) {
            this.japName = japName
            engName = title
            posterUrl = poster
            backgroundPosterUrl = coverBg
            this.year = year
            this.duration = duration
            addEpisodes(DubStatus.Subbed, episodes.reversed())
            showStatus = status
            plot = description
            addTrailer(trailer, addRaw = true)
            this.tags = listOfNotNull(*genres.toTypedArray(), studio, season)
            addActors(actors)
            this.score = Score.from10(scoreText?.toFloatOrNull())
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val links = parseJson<List<EpisodeLink>>(data)
        links.amap { (rawUrl, quality) ->
            val resolvedUrl = resolveUrl(rawUrl)
            val qualityInt = quality.fixQuality()

            if (resolvedUrl.contains("pixeldrain.com/api/file/")) {
                callback(newExtractorLink("Pixeldrain", "Pixeldrain", resolvedUrl) {
                    this.referer = "https://pixeldrain.com/"
                    this.quality = qualityInt
                })
                return@amap
            }

            val collected = mutableListOf<ExtractorLink>()
            loadExtractor(resolvedUrl, "$mainUrl/", subtitleCallback) { collected.add(it) }
            collected.forEach { link ->
                callback(newExtractorLink(link.source, link.name, link.url, link.type) {
                    this.referer = link.referer
                    this.quality = qualityInt
                    this.headers = link.headers
                    this.extractorData = link.extractorData
                })
            }
        }
        return true
    }

    private fun resolveUrl(url: String): String {
        if (url.contains("ouo.io")) {
            val sParam = Regex("[?&]s=([^&]+)").find(url)?.groupValues?.getOrNull(1)
            if (sParam != null) return URLDecoder.decode(sParam, "UTF-8")
        }
        if (url.contains("acefile.co/f/")) {
            val id = Regex("/f/(\\w+)").find(url)?.groupValues?.getOrNull(1)
            if (id != null) return "https://acefile.co/player/$id"
        }
        return url
    }

    private fun String.fixQuality(): Int = when {
        this.contains("1080", true) -> Qualities.P1080.value
        this.contains("720", true) -> Qualities.P720.value
        this.contains("480", true) -> Qualities.P480.value
        this.contains("360", true) -> Qualities.P360.value
        else -> Qualities.Unknown.value
    }

    data class EpisodeLink(
        @JsonProperty("url") val url: String,
        @JsonProperty("quality") val quality: String
    )

    data class PixeldrainList(
        @JsonProperty("files") val files: List<PixeldrainFile> = emptyList()
    )

    data class PixeldrainFile(
        @JsonProperty("id") val id: String,
        @JsonProperty("name") val name: String,
        @JsonProperty("mime_type") val mimeType: String = ""
    )
}

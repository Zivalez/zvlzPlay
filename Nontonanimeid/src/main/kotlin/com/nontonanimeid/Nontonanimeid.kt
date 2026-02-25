package com.nontonanimeid

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

class Nontonanimeid : MainAPI() {
    override var mainUrl = "https://s11.nontonanimeid.boats"
    override var name = "NontonAnimeID"
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

        fun getStatus(cls: String): ShowStatus = when {
            cls.contains("airing", true) -> ShowStatus.Ongoing
            else -> ShowStatus.Completed
        }
    }

    override val mainPage = mainPageOf(
        "$mainUrl/" to "Episode Terbaru",
        "$mainUrl/anime/page/%d/" to "Anime Terbaru",
        "$mainUrl/ongoing-list/" to "Sedang Tayang",
        "$mainUrl/popular-series/" to "Popular Series",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = request.data.replace("%d", "$page")
        val isSinglePage = request.name in listOf("Episode Terbaru", "Sedang Tayang", "Popular Series")

        if (isSinglePage && page > 1) return newHomePageResponse(request.name, emptyList(), hasNext = false)

        val document = app.get(url).document
        val home = when {
            request.data.contains("/anime/") -> document.select("a.as-anime-card")
            request.data.contains("/ongoing-list") -> document.select("a.gacha-card")
            request.data.contains("/popular-series") -> document.select("div.animeseries")
            else -> document.select("article.animeseries")
        }.mapNotNull { it.toSearchResult() }

        return newHomePageResponse(request.name, home, hasNext = !isSinglePage)
    }

    private fun Element.toSearchResult(): AnimeSearchResponse? {
        return when {
            hasClass("as-anime-card") -> {
                val title = selectFirst("h3.as-anime-title")?.text()?.trim() ?: return null
                val href = absUrl("href").takeIf { it.isNotBlank() } ?: return null
                val poster = selectFirst("img")?.attr("src")
                newAnimeSearchResponse(title, href, TvType.Anime) {
                    this.posterUrl = poster
                }
            }
            hasClass("gacha-card") -> {
                val title = selectFirst("h3.title")?.text()?.trim() ?: return null
                val href = absUrl("href").takeIf { it.isNotBlank() } ?: return null
                val poster = selectFirst("img")?.attr("src")
                newAnimeSearchResponse(title, href, TvType.Anime) {
                    this.posterUrl = poster
                }
            }
            else -> {
                // article.animeseries or div.animeseries
                val a = selectFirst("a") ?: return null
                val href = a.absUrl("href").takeIf { it.isNotBlank() } ?: return null
                val title = selectFirst("h3.entry-title span, h3.title span")?.text()?.trim()
                    ?: selectFirst("img")?.attr("alt")?.trim() ?: return null
                val poster = selectFirst("img")?.attr("src")
                val epNum = selectFirst("span.types.episodes")?.ownText()?.trim()?.toIntOrNull()
                newAnimeSearchResponse(title, href, TvType.Anime) {
                    this.posterUrl = poster
                    addSub(epNum)
                }
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("a.as-anime-card").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document

        val rawTitle = document.selectFirst("h1")?.text()?.trim() ?: ""
        val title = rawTitle.removePrefix("Nonton ").removeSuffix(" Sub Indo").trim()

        val poster = document.selectFirst(".anime-card__sidebar img")?.attr("src")
        val scoreEl = document.selectFirst(".anime-card__score")
        val typeText = scoreEl?.selectFirst(".type")?.text()?.trim() ?: "TV"
        val tvType = getType(typeText)

        val statusEl = document.selectFirst("[class*='status-']")
        val status = getStatus(statusEl?.className() ?: "")

        val synopsis = document.selectFirst("#tab-synopsis")?.text()?.trim()
        val tags = document.select(".anime-card__genres.in-tab a").map { it.text().trim() }

        val year = document.select("ul.details-list li")
            .firstOrNull { it.text().contains("Aired:") }
            ?.text()
            ?.let { Regex("(\\d{4})").find(it)?.groupValues?.get(1)?.toIntOrNull() }

        val episodes = document.select("div.episode-list-items a.episode-item").map { ep ->
            val epHref = ep.absUrl("href")
            val epTitle = ep.selectFirst("span.ep-title")?.text()?.trim()
            val epNum = epTitle?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
            newEpisode(epHref) {
                name = epTitle
                episode = epNum
            }
        }.reversed()

        return newAnimeLoadResponse(title, url, tvType) {
            this.posterUrl = poster
            this.year = year
            this.plot = synopsis
            this.tags = tags
            showStatus = status
            addEpisodes(DubStatus.Subbed, episodes)
        }
    }

    // Decode Dean Edwards JS packer (eval(function(p,a,c,k,e,d){...}))
    private fun unpackJs(packed: String): String? {
        val match = Regex("""\}\s*\(\s*'((?:[^'\\]|\\.)*)',\s*(\d+),\s*(\d+),\s*'((?:[^'\\]|\\.)*)'\.split\(""")
            .find(packed) ?: return null
        var p = match.groupValues[1].replace("\\'", "'")
        val a = match.groupValues[2].toIntOrNull() ?: return null
        val c = match.groupValues[3].toIntOrNull() ?: return null
        val k = match.groupValues[4].split("|")

        fun toBase(n: Int): String {
            val prefix = if (n < a) "" else toBase(n / a)
            val rem = n % a
            return prefix + if (rem > 35) (rem + 29).toChar() else rem.toString(36)
        }

        for (i in c - 1 downTo 0) {
            if (i < k.size && k[i].isNotEmpty()) {
                p = p.replace(Regex("\\b${Regex.escape(toBase(i))}\\b"), k[i])
            }
        }
        return p
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document

        // Extract nonce from base64-encoded inline script (var kotakajax={...})
        val nonce = document.select("script[src]").mapNotNull { script ->
            val src = script.attr("src")
            if (!src.startsWith("data:text/javascript;base64,")) return@mapNotNull null
            val decoded = try {
                base64Decode(src.removePrefix("data:text/javascript;base64,"))
            } catch (_: Exception) { return@mapNotNull null }
            if (!decoded.contains("kotakajax")) return@mapNotNull null
            Regex(""""nonce"\s*:\s*"([^"]+)"""").find(decoded)?.groupValues?.get(1)
        }.firstOrNull() ?: return false

        val ajaxUrl = "$mainUrl/wp-admin/admin-ajax.php"

        document.select("li.serverplayer").amap { li ->
            val post = li.attr("data-post")
            val type = li.attr("data-type").lowercase()
            val nume = li.attr("data-nume")
            val serverLabel = li.selectFirst("span")?.text()?.trim() ?: type

            val ajaxResponse = app.post(
                ajaxUrl,
                data = mapOf(
                    "action" to "player_ajax",
                    "nonce" to nonce,
                    "serverName" to type,
                    "nume" to nume,
                    "post" to post
                ),
                referer = data,
                headers = mapOf("Origin" to mainUrl)
            ).text

            val iframeSrc = Regex("""src=["']([^"']*kotakanimeid\.link[^"']*)["']""")
                .find(ajaxResponse)?.groupValues?.get(1) ?: return@amap

            val iframePage = app.get(iframeSrc, referer = data).text

            val evalScript = iframePage.lines()
                .firstOrNull { it.contains("eval(function(p,a,c,k") }
                ?: return@amap

            val unpacked = unpackJs(evalScript) ?: return@amap

            val fileUrl = Regex(""""file"\s*:\s*"([^"]+)"""")
                .find(unpacked)?.groupValues?.get(1) ?: return@amap

            val isM3u8 = unpacked.contains("mpegurl", true) || fileUrl.contains("m3u8") ||
                         unpacked.contains("isHLS=true")

            callback(
                newExtractorLink(
                    this.name,
                    "$name [$serverLabel]",
                    fileUrl,
                    if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                ) {
                    this.referer = iframeSrc
                    this.quality = Qualities.Unknown.value
                }
            )
        }

        return true
    }
}

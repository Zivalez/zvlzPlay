package com.nontonanimeid

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.amap
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

    private var loadmoreNonce: String? = null
    private val ajaxUrl get() = "$mainUrl/wp-admin/admin-ajax.php"
    private val pageSize = 20

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
        val isEpisodeTerbaru = request.name == "Episode Terbaru"
        val isSinglePage = request.name in listOf("Sedang Tayang", "Popular Series")

        if (isSinglePage && page > 1) return newHomePageResponse(request.name, emptyList(), hasNext = false)

        if (isEpisodeTerbaru) {
            val homePage = app.get(mainUrl).document

            if (loadmoreNonce == null) {
                loadmoreNonce = homePage.select("script:not([src])").mapNotNull { script ->
                    Regex(""""nonce"\s*:\s*"([^"]+)"""").find(script.data())?.groupValues?.get(1)
                }.firstOrNull()
            }

            if (page == 1) {
                val home = homePage.select("article.animeseries").mapNotNull { it.toSearchResult() }
                return newHomePageResponse(request.name, home, hasNext = loadmoreNonce != null)
            }

            val nonce = loadmoreNonce ?: return newHomePageResponse(request.name, emptyList(), hasNext = false)
            val offset = (page - 1) * pageSize
            val ajaxHtml = app.post(
                ajaxUrl,
                data = mapOf(
                    "action" to "loadmore",
                    "nonce" to nonce,
                    "offset" to offset.toString()
                )
            ).text

            if (ajaxHtml.isBlank() || ajaxHtml == "0") {
                return newHomePageResponse(request.name, emptyList(), hasNext = false)
            }

            val parsed = org.jsoup.Jsoup.parseBodyFragment(ajaxHtml)
            val home = parsed.select("article.animeseries").mapNotNull { it.toSearchResult() }
            return newHomePageResponse(request.name, home, hasNext = home.size >= pageSize)
        }

        val url = request.data.replace("%d", "$page")
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

        val episodeLinksRaw = document.select("div.episode-list-items a.episode-item").map { ep ->
            Triple(
                ep.attr("href"),
                ep.selectFirst("span.ep-title")?.text()?.trim(),
                ep.selectFirst("span.ep-title")?.text()?.trim()
                    ?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
            )
        }.toMutableList()

        if (document.selectFirst("div.misha_loadmore2") != null) {
            val paramsDecoded = document.select("script[src]").mapNotNull { script ->
                val src = script.attr("src")
                if (!src.startsWith("data:text/javascript;base64,")) return@mapNotNull null
                try { base64Decode(src.removePrefix("data:text/javascript;base64,")) }
                catch (_: Exception) { null }
            }.firstOrNull { it.contains("misha_loadmore_params2") }

            if (paramsDecoded != null) {
                val nonce = Regex(""""nonce"\s*:\s*"([^"]+)"""").find(paramsDecoded)?.groupValues?.get(1)
                val postsQuery = Regex(""""posts"\s*:\s*"((?:[^"\\]|\\.)*)"""").find(paramsDecoded)
                    ?.groupValues?.get(1)?.replace("\\\"", "\"")
                val maxPage = Regex(""""max_page"\s*:\s*"([^"]+)"""").find(paramsDecoded)?.groupValues?.get(1)?.toIntOrNull() ?: 0
                val totalPosts = Regex(""""total_posts"\s*:\s*"([^"]+)"""").find(paramsDecoded)?.groupValues?.get(1) ?: ""
                val postsToDisplay = Regex(""""posts_to_display"\s*:\s*"([^"]+)"""").find(paramsDecoded)?.groupValues?.get(1) ?: "20"

                if (nonce != null && postsQuery != null) {
                    var currentPage = 1
                    while (currentPage <= maxPage) {
                        val ajaxHtml = app.post(
                            ajaxUrl,
                            data = mapOf(
                                "action" to "loadmore2",
                                "nonce" to nonce,
                                "query" to postsQuery,
                                "page" to currentPage.toString(),
                                "type" to "anime",
                                "posts_to_display" to postsToDisplay,
                                "is_large_series" to "",
                                "total_posts" to totalPosts
                            ),
                            referer = url
                        ).text
                        if (ajaxHtml.isBlank() || ajaxHtml == "0" || ajaxHtml == "false") break
                        val moreEps = org.jsoup.Jsoup.parseBodyFragment(ajaxHtml).select("a.episode-item").map { ep ->
                            Triple(
                                ep.attr("href"),
                                ep.selectFirst("span.ep-title")?.text()?.trim(),
                                ep.selectFirst("span.ep-title")?.text()?.trim()
                                    ?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
                            )
                        }
                        if (moreEps.isEmpty()) break
                        episodeLinksRaw.addAll(moreEps)
                        currentPage++
                    }
                }
            }
        }

        val episodeLinks = episodeLinksRaw.reversed()

        val episodes = episodeLinks.amap { (epHref, epTitle, epNum) ->
            val thumb = try {
                app.get(epHref).document.selectFirst("div.featuredimgs img")?.attr("src")
            } catch (_: Exception) { null }
            newEpisode(epHref) {
                name = epTitle
                episode = epNum
                posterUrl = thumb
            }
        }

        return newAnimeLoadResponse(title, url, tvType) {
            this.posterUrl = poster
            this.year = year
            this.plot = synopsis
            this.tags = tags
            showStatus = status
            addEpisodes(DubStatus.Subbed, episodes)
        }
    }

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

        val nonce = document.select("script").mapNotNull { script ->
            val src = script.attr("src")
            val content = if (src.startsWith("data:text/javascript;base64,")) {
                try {
                    base64Decode(src.removePrefix("data:text/javascript;base64,"))
                } catch (_: Exception) {
                    null
                }
            } else {
                script.data().takeIf { it.isNotBlank() } ?: script.html()
            }
            if (content != null && (content.contains("nonce") || content.contains("kotakajax") || content.contains("player_ajax"))) {
                Regex(""""nonce"\s*:\s*"([^"]+)"""").find(content)?.groupValues?.get(1)
                    ?: Regex("""nonce\s*:\s*["']([^"']+)["']""").find(content)?.groupValues?.get(1)
            } else {
                null
            }
        }.firstOrNull()

        val ajaxUrl = "$mainUrl/wp-admin/admin-ajax.php"

        if (nonce != null) {
            document.select("li.serverplayer").amap { li ->
                val post = li.attr("data-post")
                val type = li.attr("data-type").lowercase()
                val nume = li.attr("data-nume")
                val serverLabel = li.selectFirst("span")?.text()?.trim() ?: type

                val ajaxResponse = runCatching {
                    app.post(
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
                }.getOrNull() ?: return@amap

                val iframeSrc = Regex("""src=["']([^"']+)["']""")
                    .find(ajaxResponse)?.groupValues?.get(1) ?: return@amap

                val fixedIframe = if (iframeSrc.startsWith("//")) "https:$iframeSrc" else iframeSrc

                val extracted = loadExtractor(fixedIframe, data, subtitleCallback) { link ->
                    callback(
                        newExtractorLink(
                            this.name,
                            "$name [$serverLabel]",
                            link.url,
                            link.type
                        ) {
                            this.referer = link.referer
                            this.quality = link.quality
                            this.headers = link.headers
                            this.extractorData = link.extractorData
                        }
                    )
                }

                if (!extracted) {
                    val iframePage = runCatching { app.get(fixedIframe, referer = data).text }.getOrNull()
                    if (iframePage != null) {
                        val evalScript = iframePage.lines().firstOrNull { it.contains("eval(function(p,a,c,k") }
                        val unpacked = evalScript?.let { unpackJs(it) } ?: iframePage
                        val fileUrl = Regex(""""file"\s*:\s*"([^"]+)"""").find(unpacked)?.groupValues?.get(1)
                            ?: Regex("""sources:\s*\[\{\s*file:\s*["']([^"']+)["']""").find(unpacked)?.groupValues?.get(1)

                        if (!fileUrl.isNullOrBlank()) {
                            val isM3u8 = unpacked.contains("mpegurl", true) || fileUrl.contains("m3u8") || unpacked.contains("isHLS=true")
                            callback(
                                newExtractorLink(
                                    this.name,
                                    "$name [$serverLabel]",
                                    fileUrl,
                                    if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                                ) {
                                    this.referer = fixedIframe
                                    this.quality = Qualities.Unknown.value
                                }
                            )
                        }
                    }
                }
            }
        }

        document.select("div.player-embed iframe, div.embed-holder iframe, div.responsive-embed iframe, iframe").forEach { ifr ->
            val src = ifr.attr("src").ifBlank { ifr.attr("data-src") }.trim()
            if (src.isNotBlank() && !src.contains("google.com") && !src.contains("youtube.com")) {
                val fixedSrc = if (src.startsWith("//")) "https:$src" else src
                loadExtractor(fixedSrc, data, subtitleCallback, callback)
            }
        }

        return true
    }
}

package com.winbu

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Winbu : MainAPI() {
    override var mainUrl = "https://winbu.net"
    override var name = "Winbu"
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA,
        TvType.Movie,
    )

    private val commonHeaders = mapOf(
        "User-Agent"      to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Accept"          to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Language" to "id-ID,id;q=0.9,en-US;q=0.8,en;q=0.7",
        "Referer"         to "$mainUrl/",
    )

    override val mainPage = mainPageOf(
        "animedonghua/page/%d/"          to "Anime Donghua",
        "film/page/%d/"                  to "Film",
        "anime-terbaru-animasu/page/%d/" to "Series Terbaru",
        "others/page/%d/"                to "Jepang Korea China Barat",
        "tvshow/page/%d/"                to "TV Show",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data.format(page)}", headers = commonHeaders).document
        val homeList = document.select("div.ml-item").mapNotNull { it.toSearchResult() }
        return newHomePageResponse(request.name, homeList)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val a = selectFirst("a.ml-mask") ?: return null
        val title = a.attr("title").trim().ifEmpty {
            selectFirst(".judul")?.text()?.trim()
        } ?: return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        val img = selectFirst("img.mli-thumb")
        val posterUrl = fixUrlNull(
            img?.attr("data-original")?.takeIf { it.isNotEmpty() }
                ?: img?.attr("src")
        )
        return if (href.contains("/film/")) {
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = posterUrl
            }
        } else {
            newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = posterUrl
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        // Extract nonce from homepage inline script
        val homeDoc = app.get(mainUrl, headers = commonHeaders).document
        val ajaxScript = homeDoc.select("script:not([src])").firstOrNull { it.data().contains("ajaxSearch") }
        val nonce = Regex(""""nonce"\s*:\s*"([^"]+)"""").find(ajaxScript?.data() ?: "")
            ?.groupValues?.getOrNull(1) ?: ""

        val responseText = app.get(
            "$mainUrl/wp-json/eastheme/search/",
            params = mapOf("keyword" to query, "nonce" to nonce),
            headers = commonHeaders,
        ).text

        if (!responseText.trimStart().startsWith("{")) return emptyList()

        return try {
            val jsonObj = JSONObject(responseText)
            if (jsonObj.has("error")) return emptyList()
            jsonObj.keys().asSequence().mapNotNull { key ->
                val item = runCatching { jsonObj.getJSONObject(key) }.getOrNull() ?: return@mapNotNull null
                val title = item.optString("title").takeIf { it.isNotEmpty() } ?: return@mapNotNull null
                val itemUrl = item.optString("url").takeIf { it.isNotEmpty() } ?: return@mapNotNull null
                val img = item.optString("img").takeIf { it.isNotEmpty() }
                if (itemUrl.contains("/film/")) {
                    newMovieSearchResponse(title, itemUrl, TvType.Movie) { posterUrl = img }
                } else {
                    newAnimeSearchResponse(title, itemUrl, TvType.Anime) { posterUrl = img }
                }
            }.toList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url, headers = commonHeaders).document
        val title = document.selectFirst(".mli-info .judul")?.text()?.trim()
            ?: return null

        val poster = document.selectFirst("img.mli-thumb")?.attr("src")
        val tags = document.select(".mli-mvi a[href*='/genre/']").map { it.text() }
        val year = document.selectFirst(".mli-mvi a[href*='/season/']")?.text()?.let {
            Regex("(\\d{4})").find(it)?.groupValues?.getOrNull(1)?.toIntOrNull()
        }
        val description = document.selectFirst("div.mli-desc p")?.text()?.trim()

        // Trailer iframe src — only add if it has an actual video ID
        val trailerSrc = document.selectFirst("#pop-trailer iframe")?.attr("src")
            ?.takeIf { it.contains("youtube.com/embed/") && it.length > "https://www.youtube.com/embed/".length }

        val isMovie = url.contains("/film/")

        if (isMovie) {
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                posterUrl = poster
                this.year = year
                plot = description
                if (trailerSrc != null) addTrailer(trailerSrc)
                this.tags = tags
            }
        }

        val episodes = document.select("div.tvseason div.les-content a").mapNotNull { el ->
            val epTitle = el.text().trim()
            val epHref = fixUrlNull(el.attr("href")) ?: return@mapNotNull null
            val epNum = Regex("Episode\\s*(\\d+)", RegexOption.IGNORE_CASE)
                .find(epTitle)?.groupValues?.getOrNull(1)?.toIntOrNull()
            newEpisode(epHref) {
                name = epTitle
                episode = epNum
            }
        }.reversed()

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            engName = title
            posterUrl = poster
            this.year = year
            addEpisodes(DubStatus.Subbed, episodes)
            plot = description
            if (trailerSrc != null) addTrailer(trailerSrc)
            this.tags = tags
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data, headers = commonHeaders).document

        // Resolve filedon.co/embed/ → direct video URL via #app data-page JSON
        suspend fun resolveFiledon(url: String, label: String) {
            val page = app.get(url, headers = commonHeaders + mapOf("Referer" to data)).document
            val dataPage = page.selectFirst("#app")?.attr("data-page") ?: return
            try {
                val props = JSONObject(dataPage).optJSONObject("props") ?: return
                val directUrl = props.optString("url").takeIf { it.isNotEmpty() } ?: return
                callback(
                    newExtractorLink(name, label, directUrl) {
                        this.referer = url
                        this.quality = Qualities.Unknown.value
                    }
                )
            } catch (_: Exception) {}
        }

        suspend fun handleUrl(src: String, label: String) {
            when {
                src.contains("filedon.co/embed/", ignoreCase = true) ->
                    resolveFiledon(src, label)
                src.contains("mega.nz/embed/", ignoreCase = true) ->
                    loadExtractor(src.replace("/embed/", "/file/"), data, subtitleCallback, callback)
                src.contains("short.icu/", ignoreCase = true) ||
                src.contains("strp2p.com", ignoreCase = true) -> { /* not extractable */ }
                else -> loadExtractor(src, data, subtitleCallback, callback)
            }
        }

        // AJAX: iterate all player options
        document.select(".east_player_option").forEach { player ->
            val post = player.attr("data-post").takeIf { it.isNotEmpty() } ?: return@forEach
            val nume = player.attr("data-nume").takeIf { it.isNotEmpty() } ?: return@forEach
            val type = player.attr("data-type").ifEmpty { "schtml" }
            val label = player.selectFirst("span")?.text()?.trim()
                ?: player.text().trim().ifEmpty { "Server $nume" }

            val response = app.post(
                "$mainUrl/wp-admin/admin-ajax.php",
                data = mapOf(
                    "action" to "player_ajax",
                    "post"   to post,
                    "nume"   to nume,
                    "type"   to type,
                ),
                headers = commonHeaders + mapOf("Referer" to data),
            ).text

            val iframeSrc = Jsoup.parse(response).selectFirst("iframe")?.attr("src")
                ?.takeIf { it.startsWith("http") } ?: return@forEach

            handleUrl(iframeSrc, label)
        }

        // Download links (Gofile, MEGA, Filedon, BuzzHeavier, etc.)
        document.select("#downloadb li").forEach { li ->
            val quality = li.selectFirst("strong")?.text()?.trim() ?: ""
            li.select("a[href]").forEach { a ->
                val href = fixUrlNull(a.attr("href")) ?: return@forEach
                val label = "${a.text().trim()} $quality".trim()
                handleUrl(href, label)
            }
        }

        return true
    }
}

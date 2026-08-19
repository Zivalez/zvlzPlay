package com.layarkaca

import com.lagradost.api.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.*
import org.json.JSONObject
import org.jsoup.nodes.Element
import java.net.URI

class LayarKacaProvider : MainAPI() {

    override var mainUrl = "https://tv12.lk21official.cc"
    private var seriesUrl = "https://dramamu.lk21.de"

    override var name = "LayarKaca"
    override val hasMainPage = true
    override var lang = "id"
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.AsianDrama
    )

    override val mainPage = mainPageOf(
        "$mainUrl/latest/page/" to "Film Terbaru",
        "$mainUrl/populer/page/" to "Film Terpopuler",
        "$seriesUrl/latest-series/page/" to "Series Terbaru",
        "$seriesUrl/series/ongoing/page/" to "Series Ongoing",
        "$seriesUrl/series/complete/page/" to "Series Complete",
        "$seriesUrl/series/asian/page/" to "Series Asian",
        "$seriesUrl/series/west/page/" to "Series West",
        "$mainUrl/rating/page/" to "Film Berdasarkan IMDb Rating",
        "$mainUrl/most-commented/page/" to "Film Dengan Komentar Terbanyak",
        "$mainUrl/genre/action/page/" to "Film Action",
        "$mainUrl/genre/animation/page/" to "Film Animation",
        "$mainUrl/genre/horror/page/" to "Film Horror",
        "$seriesUrl/country/china/page/" to "Film China",
        "$seriesUrl/country/south-korea/page/" to "Film Korea",
        "$seriesUrl/country/japan/page/" to "Film Japan",
        "$seriesUrl/country/thailand/page/" to "Film Thailand",
    )

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        val document = app.get(request.data + page).document

        val home = document.select("article figure").mapNotNull {
            it.toSearchResult()
        }
        return newHomePageResponse(request.name, home)
    }

    private suspend fun getProperLink(url: String): String {
        if (url.startsWith(seriesUrl)) return url
        val res = app.get(url).document

        val redirectTarget = res.selectFirst("a#openNow, div.links a, a[href*=\"nontondrama\"], a[href*=\"dramamu\"]")?.attr("href")
        if (!redirectTarget.isNullOrBlank()) {
            return redirectTarget
        }

        return if (res.select("title").text().contains("Nontondrama", true)) {
            res.selectFirst("a#openNow")?.attr("href")
                ?: res.selectFirst("div.links a")?.attr("href")
                ?: url
        } else {
            url
        }
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst("h3")?.ownText()?.trim() ?: return null
        val href = fixUrl(this.selectFirst("a")!!.attr("href"))
        val posterUrl = fixUrlNull(this.selectFirst("img")?.getImageAttr())
        val type = if (this.selectFirst("span.episode") == null) TvType.Movie else TvType.TvSeries
        val posterheaders = mapOf("Referer" to getBaseUrl(posterUrl))
        return if (type == TvType.TvSeries) {
            val episode = this.selectFirst("span.episode strong")?.text()?.filter { it.isDigit() }
                ?.toIntOrNull()
            newAnimeSearchResponse(title, href, TvType.TvSeries) {
                this.posterUrl = posterUrl
                this.posterHeaders = posterheaders
                addSub(episode)
            }
        } else {
            val quality = this.select("div.quality").text().trim()
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = posterUrl
                this.posterHeaders = posterheaders
                addQuality(quality)
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("article figure").mapNotNull {
            it.toSearchResult()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val fixUrl = getProperLink(url)
        val document = app.get(fixUrl).document

        val baseurl = fetchURL(fixUrl)
        val title = (document.selectFirst("h1.entry-title")?.text()
            ?: document.selectFirst("h1")?.text()
            ?: document.selectFirst(".titles")?.text())
            ?.trim()
            ?.substringBefore("Trailer")
            ?.substringBefore("LayarKaca")
            ?.trim()
            ?: ""

        val poster = fixUrlNull(document.selectFirst("article figure img")?.getImageAttr())
        val posterheaders = mapOf("Referer" to getBaseUrl(poster))

        val tags = document.select("meta[property=og:video:tag]").map { it.attr("content") }

        val year = (document.select("div.content h2")
            .firstOrNull { it.text().contains("Tahun:") }?.text()
            ?: document.select("div.content h3")
                .firstOrNull { it.text().contains("Tahun:") }?.text()
            ?: document.selectFirst(".year")?.text())?.filter { it.isDigit() }?.toIntOrNull()

        val tvType = if (fixUrl.contains("/series/") || fixUrl.contains("dramamu") || fixUrl.contains("nontondrama") || document.selectFirst("span.episode") != null) TvType.TvSeries else TvType.Movie
        val description = document.selectFirst("div.content blockquote")?.text()
            ?: document.selectFirst("div.content p")?.text()

        val trailer = document.selectFirst("ul.action-player li a.fancybox")?.attr("href")
            ?: document.selectFirst("a.fancybox")?.attr("href")

        val rating = (document.selectFirst("div.content h2")?.text()
            ?: document.selectFirst("div.content h3")?.text())
            ?.substringAfter("Rating: ")?.substringBefore(" /")?.trim()

        val actors = document.select("div.content blockquote p").last()?.select("a")?.map {
            Actor(it.text(), it.selectFirst("img")?.attr("src"))
        }

        val duration = (document.select("div.content h2")
            .firstOrNull { it.text().contains("Durasi:") }?.text()
            ?: document.select("div.content h3")
                .firstOrNull { it.text().contains("Durasi:") }?.text())?.filter { it.isDigit() }
            ?.toIntOrNull()

        if (tvType == TvType.TvSeries) {
            val episodes = document.select("ul.episode-list li a, div.episode-list a, a.episode-btn, a[href*=\"/episode-\"], a[href*=\"-season-\"]").mapNotNull {
                val name = it.text().trim()
                val href = fixUrl(it.attr("href"))
                val epNum = Regex("""(?:episode-|E)(\d+)""", RegexOption.IGNORE_CASE)
                    .find(href)?.groupValues?.getOrNull(1)?.toIntOrNull()
                    ?: Regex("""(?:Episode|E)\s*(\d+)""", RegexOption.IGNORE_CASE)
                        .find(name)?.groupValues?.getOrNull(1)?.toIntOrNull()
                val seasonNum = Regex("""(?:season-|S)(\d+)""", RegexOption.IGNORE_CASE)
                    .find(href)?.groupValues?.getOrNull(1)?.toIntOrNull()
                    ?: Regex("""(?:Season|S)\s*(\d+)""", RegexOption.IGNORE_CASE)
                        .find(name)?.groupValues?.getOrNull(1)?.toIntOrNull()

                newEpisode(href) {
                    this.name = name
                    this.episode = epNum
                    this.season = seasonNum
                }
            }.distinctBy { it.data }

            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.posterHeaders = posterheaders
                this.year = year
                this.plot = description
                this.tags = tags
                this.score = Score.from10(rating)
                addActors(actors)
                addTrailer(trailer)
            }
        } else {
            return newMovieLoadResponse(title, url, TvType.Movie, fixUrl) {
                this.posterUrl = poster
                this.posterHeaders = posterheaders
                this.year = year
                this.plot = description
                this.tags = tags
                this.score = Score.from10(rating)
                this.duration = duration
                addActors(actors)
                addTrailer(trailer)
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document

        val directLinks = document.select("div.action-player a, ul#load-providers a, ul#player-list li a, a.btn-watch").mapNotNull { a ->
            val dataUrl = a.attr("data-url").ifBlank { a.attr("data-src") }.trim()
            val href = a.attr("href").trim()
            val raw = when {
                dataUrl.isNotBlank() && dataUrl != "#" && !dataUrl.startsWith("javascript:") -> dataUrl
                href.isNotBlank() && href != "#" && !href.startsWith("javascript:") -> href
                else -> null
            }
            raw?.let { fixUrl(it) }
        }

        val iframes = document.select("iframe#main-player, div.embed-container iframe, iframe").mapNotNull {
            val src = it.attr("src").ifBlank { it.attr("data-src") }.trim()
            if (src.isNotBlank() && src != "#" && !src.startsWith("javascript:")) fixUrl(src) else null
        }

        val candidates = (directLinks + iframes).distinct().filter {
            !it.contains("youtube.com") && !it.contains("google.com")
        }

        candidates.amap { candidate ->
            val childCandidates = mutableListOf(candidate)
            if (candidate.contains("videonode.de") || candidate.contains("playeriframe")) {
                runCatching {
                    val childDoc = app.get(candidate, referer = data).document
                    childDoc.select("iframe").forEach { ifr ->
                        val childSrc = ifr.attr("src").ifBlank { ifr.attr("data-src") }.trim()
                        if (childSrc.isNotBlank()) childCandidates.add(fixUrl(childSrc))
                    }
                }
            }

            childCandidates.distinct().forEach { url ->
                val extracted = loadExtractor(url, data, subtitleCallback, callback)
                if (!extracted) {
                    resolvePlayeriframe(url, callback)
                }
            }
        }
        return true
    }

    private suspend fun resolvePlayeriframe(
        url: String,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val id = Regex("playeriframe\\.sbs/iframe/(?:p2p|turbovip|hydrax)/([a-zA-Z0-9]+)")
            .find(url)
            ?.groupValues
            ?.getOrNull(1)
            ?: return false

        val response = runCatching {
            app.post(
                "https://cloud.hownetwork.xyz/api2.php?id=$id",
                data = mapOf(
                    "r" to "https://playeriframe.sbs/",
                    "d" to "cloud.hownetwork.xyz"
                ),
                headers = mapOf(
                    "X-Requested-With" to "XMLHttpRequest",
                    "Origin" to "https://cloud.hownetwork.xyz",
                    "Referer" to url,
                    "User-Agent" to "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Mobile Safari/537.36"
                ),
                referer = url
            ).text
        }.getOrNull() ?: return false

        val json = runCatching { JSONObject(response) }.getOrNull() ?: return false
        val direct = json.optString("file").ifBlank { json.optString("link") }
        if (direct.isBlank()) return false

        M3u8Helper.generateM3u8("P2P", direct, "https://cloud.hownetwork.xyz")
            .forEach(callback)
        return true
    }

    private suspend fun fetchURL(url: String): String {
        val res = app.get(url, allowRedirects = false)
        val href = res.headers["location"]

        return if (href != null) {
            val it = URI(href)
            "${it.scheme}://${it.host}"
        } else {
            url
        }
    }

    private fun Element.getImageAttr(): String {
        return when {
            this.hasAttr("src") -> this.attr("src")
            this.hasAttr("data-src") -> this.attr("data-src")
            else -> this.attr("src")
        }
    }

    fun getBaseUrl(url: String?): String {
        return runCatching {
            URI(url).let { "${it.scheme}://${it.host}" }
        }.getOrDefault(mainUrl)
    }
}

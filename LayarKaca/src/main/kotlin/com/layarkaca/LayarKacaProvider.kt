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

    override var mainUrl = "https://lk21.de"
    private var seriesUrl = "https://series.lk21.de"
    private var searchurl= "https://gudangvape.com"

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
        "$mainUrl/populer/page/" to "Film Terplopuler",
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
        val posterheaders= mapOf("Referer" to getBaseUrl(posterUrl))
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
        val domain = fetchURL(mainUrl)
        val resText = app.get("$searchurl/search.php?s=$query&page=1", headers = mapOf(
            "Referer" to "$domain/",
            "Origin" to domain,
            "X-Requested-With" to "XMLHttpRequest"
        )).text
        
        val results = mutableListOf<SearchResponse>()
        try {
            val root = JSONObject(resText)
        val arr = root.getJSONArray("data")

        for (i in 0 until arr.length()) {
            val item = arr.getJSONObject(i)
            val title = item.getString("title")
            val slug = item.getString("slug")
            val type = item.getString("type")
            val posterUrl = "https://poster.lk21.party/wp-content/uploads/"+item.optString("poster")
            when (type) {
                "series" -> results.add(
                    newTvSeriesSearchResponse(title, "$seriesUrl/$slug", TvType.TvSeries) {
                        this.posterUrl = posterUrl
                    }
                )
                "movie" -> results.add(
                    newMovieSearchResponse(title, "$mainUrl/$slug", TvType.Movie) {
                        this.posterUrl = posterUrl
                    }
                )
            }
        }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return results
    }

    override suspend fun load(url: String): LoadResponse {
        val fixUrl = getProperLink(url)
        val document = app.get(fixUrl).document

        val baseurl=fetchURL(fixUrl)
        val title = document.selectFirst("div.movie-info h1")?.text()
            ?.replace(Regex("^Nonton\\s+", RegexOption.IGNORE_CASE), "")
            ?.replace(Regex("\\s+Sub Indo.*$", RegexOption.IGNORE_CASE), "")
            ?.trim() ?: ""
        val poster = document.select("meta[property=og:image]").attr("content")
        val tags = document.select("div.tag-list span.tag a[href*='/genre/']").map { it.text() }
        val posterheaders= mapOf("Referer" to getBaseUrl(poster))

        val year = Regex("\\((\\d{4})\\)").find(
            document.select("div.movie-info h1").text()
        )?.groupValues?.get(1)?.toIntOrNull()
        val tvType = if (document.selectFirst("#season-data") != null) TvType.TvSeries else TvType.Movie
        val description = document.selectFirst("div.synopsis")?.attr("data-full")?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: document.selectFirst("div.synopsis")?.text()?.trim()
        val trailer = document.selectFirst("ul.action-left > li:nth-child(3) > a")?.attr("href")
        val rating = document.selectFirst("div.info-tag strong")?.text()

        val durationText = document.select("div.info-tag span").map { it.text().trim() }
            .firstOrNull { it.contains(Regex("\\d+h|\\d+m")) }
        val duration = durationText?.let {
            val hours = Regex("(\\d+)h").find(it)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val mins = Regex("(\\d+)m").find(it)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            if (hours + mins > 0) hours * 60 + mins else null
        }
        val actors = document.select("div.detail p:contains(Bintang Film) a").map { it.text() }

        val recommendations = document.select("li.slider article").map {
            val recName = it.selectFirst("h3")?.text()?.trim().toString()
            val recHref = baseurl+it.selectFirst("a")!!.attr("href")
            val recPosterUrl = fixUrl(it.selectFirst("img")?.attr("src").toString())
            newTvSeriesSearchResponse(recName, recHref, TvType.TvSeries) {
                this.posterUrl = recPosterUrl
                this.posterHeaders = posterheaders
            }
        }

        return if (tvType == TvType.TvSeries) {
            val json = document.selectFirst("script#season-data")?.data()
            val episodes = mutableListOf<Episode>()
            if (json != null) {
                val root = JSONObject(json)
                root.keys().forEach { seasonKey ->
                    val seasonArr = root.getJSONArray(seasonKey)
                    for (i in 0 until seasonArr.length()) {
                        val ep = seasonArr.getJSONObject(i)
                        val href = fixUrl("$baseurl/"+ep.getString("slug"))
                        val episodeNo = ep.optInt("episode_no")
                        val seasonNo = ep.optInt("s")
                        episodes.add(
                            newEpisode(href) {
                                this.name = "Episode $episodeNo"
                                this.season = seasonNo
                                this.episode = episodeNo
                            }
                        )
                    }
                }
            }
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.posterHeaders = posterheaders
                this.year = year
                this.duration = duration
                this.plot = description
                this.tags = tags
                this.score = Score.from10(rating)
                this.recommendations = recommendations
                if (actors.isNotEmpty()) addActors(actors)
                addTrailer(trailer)
            }
        } else {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.posterHeaders = posterheaders
                this.year = year
                this.duration = duration
                this.plot = description
                this.tags = tags
                this.score = Score.from10(rating)
                this.recommendations = recommendations
                if (actors.isNotEmpty()) addActors(actors)
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

        val playerLinks = document.select("ul#player-list li a").mapNotNull { a ->
            val dataUrl = a.attr("data-url").trim()
            val href = a.attr("href").trim()
            val raw = when {
                dataUrl.isNotBlank() && dataUrl != "#" -> dataUrl
                href.isNotBlank() && href != "#" -> href
                else -> null
            }
            raw?.let(::fixUrl)
        }
        val mainIframe = document.selectFirst("iframe#main-player, div.embed-container iframe")
            ?.attr("src")
            ?.trim()
            ?.takeIf { it.isNotBlank() && it != "#" }
            ?.let(::fixUrl)

        (playerLinks + listOfNotNull(mainIframe)).distinct().amap { playerUrl ->
            val iframeUrl = playerUrl.getIframe()
            val candidates = listOf(playerUrl, iframeUrl).filter { it.isNotBlank() }.distinct()

            candidates.forEach { candidate ->
                Log.d("LayarKaca", candidate)
                val extracted = loadExtractor(candidate, data, subtitleCallback, callback)
                if (!extracted) {
                    val resolved = resolvePlayeriframe(candidate, callback)
                    if (!resolved) {
                        Log.d("LayarKaca", "No extractor matched: $candidate")
                    }
                }
            }
        }
        return true
    }

    private suspend fun String.getIframe(): String {
        return app.get(this, referer = this).document.select("div.embed-container iframe, iframe#main-player")
            .attr("src")
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

        val response = app.post(
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

        val json = JSONObject(response)
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
        return URI(url).let {
            "${it.scheme}://${it.host}"
        }
    }

}

package com.Pencurimovie

import com.lagradost.api.Log
import org.jsoup.nodes.Element
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.*

class Pencurimovie : MainAPI() {
    override var mainUrl = "https://ww21.pencurimovie.sbs"
    override var name = "Pencurimovie"
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.Movie, TvType.Anime, TvType.Cartoon)

    override val mainPage = mainPageOf(
        "movies" to "Latest Movies",
        "series" to "TV Series",
        "most-rating" to "Most Rating Movies",
        "top-imdb" to "Top IMDB Movies",
        "country/malaysia" to "Malaysia Movies",
        "country/indonesia" to "Indonesia Movies",
        "country/india" to "India Movies",
        "country/japan" to "Japan Movies",
        "country/thailand" to "Thailand Movies",
        "country/china" to "China Movies",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data}/page/$page", timeout = 50L).document
        val home = document.select("div.ml-item").mapNotNull { it.toSearchResult() }

        return newHomePageResponse(
            list = HomePageList(
                name = request.name,
                list = home,
                isHorizontalImages = false
            ),
            hasNext = true
        )
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val a = selectFirst("a") ?: return null
        val title = (a.attr("oldtitle").takeIf { it.isNotBlank() }
            ?: a.attr("title").takeIf { it.isNotBlank() }
            ?: a.text()).substringBefore("(")
            .trim()
        if (title.isBlank()) return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        val posterUrl = fixUrlNull(selectFirst("img")?.attr("data-original")?.takeIf { it.isNotBlank() }
            ?: selectFirst("img")?.attr("src"))
        val quality = getQualityFromString(select("span.mli-quality").text())
        return newMovieSearchResponse(title, href, TvType.Movie) {
            this.posterUrl = posterUrl
            this.quality = quality
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("${mainUrl}?s=$query", timeout = 50L).document
        return document.select("div.ml-item").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url, timeout = 50L).document
        val title = document.selectFirst("div.mvic-desc h3")?.text()?.trim()?.substringBefore("(")
            ?: document.selectFirst("h1")?.text()?.trim() ?: ""
        val poster = fixUrlNull(document.select("meta[property=og:image]").attr("content"))
        val description = document.selectFirst("div.desc p.f-desc, div.desc")?.text()?.trim()
        val tvtag = if (url.contains("series")) TvType.TvSeries else TvType.Movie
        val trailer = document.select("meta[itemprop=embedUrl]").attr("content")
        val genre = document.select("div.mvic-info p:contains(Genre)").select("a").map { it.text().trim() }
        val actors = document.select("div.mvic-info p:contains(Actors)").select("a").map { it.text().trim() }
        val year = document.select("div.mvic-info p:contains(Release)").select("a").text().filter { it.isDigit() }.toIntOrNull()
        val recommendation = document.select("div.ml-item").mapNotNull { it.toSearchResult() }

        if (tvtag == TvType.TvSeries) {
            val episodes = mutableListOf<Episode>()
            document.select("div.tvseason").amap { info ->
                val season = info.select("strong").text().substringAfter("Season").trim().toIntOrNull()
                info.select("div.les-content a").forEach {
                    val name = it.text().substringAfter("-").trim()
                    val href = fixUrl(it.attr("href"))
                    val rawEpisode = it.text().substringAfter("Episode").substringBefore("-").trim().toIntOrNull()
                    episodes.add(
                        newEpisode(href) {
                            this.episode = rawEpisode
                            this.name = name
                            this.season = season
                        }
                    )
                }
            }

            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.plot = description
                this.tags = genre
                this.year = year
                addTrailer(trailer)
                addActors(actors)
                this.recommendations = recommendation
            }
        } else {
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = genre
                this.year = year
                addTrailer(trailer)
                addActors(actors)
                this.recommendations = recommendation
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
        val iframes = document.select("div.movieplay iframe, div.player iframe, iframe").mapNotNull {
            val src = it.attr("src").ifBlank { it.attr("data-src") }.trim()
            if (src.isNotBlank() && !src.contains("youtube.com") && !src.contains("google.com")) {
                if (src.startsWith("//")) "https:$src" else src
            } else null
        }.distinct()

        iframes.amap { href ->
            loadExtractor(href, data, subtitleCallback, callback)
        }
        return true
    }
}

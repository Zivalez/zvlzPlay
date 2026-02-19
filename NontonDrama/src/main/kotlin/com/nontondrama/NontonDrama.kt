package com.nontondrama

import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.LoadResponse.Companion.addScore
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.addQuality
import com.lagradost.cloudstream3.addSub
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.fixUrl
import com.lagradost.cloudstream3.fixUrlNull
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newAnimeSearchResponse
import com.lagradost.cloudstream3.newEpisode
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newMovieLoadResponse
import com.lagradost.cloudstream3.newMovieSearchResponse
import com.lagradost.cloudstream3.newTvSeriesLoadResponse
import com.lagradost.cloudstream3.newTvSeriesSearchResponse
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Element
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class NontonDrama : MainAPI() {

    override var mainUrl = "https://tv3.nontondrama.my"

    override var name = "NontonDrama"
    override val hasMainPage = true
    override var lang = "id"
    override val supportedTypes = setOf(TvType.TvSeries, TvType.AsianDrama)

    override val mainPage =
            mainPageOf(
                    "$mainUrl/latest-series/page/" to "Series Terbaru",
                    "$mainUrl/populer/page/" to "Series Terpopuler",
                    "$mainUrl/country/south-korea/page/" to "Drama Korea",
                    "$mainUrl/country/china/page/" to "Series China",
                    "$mainUrl/series/west/page/" to "Series West",
            )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data + page).document
        val home = document.select("article.mega-item").mapNotNull { it.toSearchResult() }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst("h1.grid-title > a")?.ownText()?.trim() ?: return null
        val href = fixUrl(this.selectFirst("a")!!.attr("href"))
        val posterUrl = fixUrlNull(this.selectFirst("img")?.attr("src"))
        
        // Ensure strictly series/drama
        return if (this.selectFirst("div.last-episode") != null) {
            val episode =
                    this.selectFirst("div.last-episode span")
                            ?.text()
                            ?.filter { it.isDigit() }
                            ?.toIntOrNull()
            newAnimeSearchResponse(title, href, TvType.TvSeries) {
                this.posterUrl = posterUrl
                addSub(episode)
            }
        } else {
             newTvSeriesSearchResponse(title, href, TvType.TvSeries) {
                this.posterUrl = posterUrl
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val encodedQuery = withContext(Dispatchers.IO) {
            URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        }
        val document =
                app.get("$mainUrl/search.php?s=$query#gsc.tab=0&gsc.q=$encodedQuery&gsc.page=1")
                        .document
        return document.select("div.search-item").mapNotNull {
            val title = it.selectFirst("a")?.attr("title") ?: ""
            val href = fixUrl(it.selectFirst("a")?.attr("href") ?: return@mapNotNull null)
            val posterUrl = fixUrlNull(it.selectFirst("img.img-thumbnail")?.attr("src"))
            newTvSeriesSearchResponse(title, href, TvType.TvSeries) { this.posterUrl = posterUrl }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document

        val title = document.selectFirst("li.last > span[itemprop=name]")?.text()?.trim().toString()
        val poster = fixUrl(document.select("img.img-thumbnail").attr("src"))
        val tags = document.select("div.content > div:nth-child(5) > h3 > a").map { it.text() }

        val year =
                Regex("\\d, (\\d+)")
                        .find(document.select("div.content > div:nth-child(7) > h3").text().trim())
                        ?.groupValues
                        ?.get(1)
                        .toString()
                        .toIntOrNull()
        
        val description = document.select("div.content > blockquote").text().trim()
        val trailer = document.selectFirst("div.action-player li > a.fancybox")?.attr("href")
        val rating =
                document.selectFirst("div.content > div:nth-child(6) > h3")?.text()
        val actors =
                document.select("div.col-xs-9.content > div:nth-child(3) > h3 > a").map {
                    it.text()
                }

        val recommendations =
                document.select("div.row.item-media").map {
                    val recName = it.selectFirst("h3")?.text()?.trim().toString()
                    val recHref = it.selectFirst(".content-media > a")!!.attr("href")
                    val recPosterUrl =
                            fixUrl(
                                    it.selectFirst(".poster-media > a > img")
                                            ?.attr("src")
                                            .toString()
                            )
                    newTvSeriesSearchResponse(recName, recHref, TvType.TvSeries) {
                        this.posterUrl = recPosterUrl
                    }
                }

        // Logic for Series (Based on LayarKaca.kt, but assuming all are series here)
        val episodes =
                document.select("div.episode-list > a:matches(\\d+)")
                        .map {
                            val href = fixUrl(it.attr("href"))
                            val episode = it.text().toIntOrNull()
                            val season =
                                    it.attr("href")
                                            .substringAfter("season-")
                                            .substringBefore("-")
                                            .toIntOrNull()
                            newEpisode(href){
                                this.name = "Episode $episode"
                                this.season = season
                                this.episode = episode
                            }
                        }
                        .reversed()
        
        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
            this.posterUrl = poster
            this.year = year
            this.plot = description
            this.tags = tags
            addScore(rating)
            addActors(actors)
            this.recommendations = recommendations
            addTrailer(trailer)
        }
    }

    override suspend fun loadLinks(
            data: String,
            isCasting: Boolean,
            subtitleCallback: (SubtitleFile) -> Unit,
            callback: (ExtractorLink) -> Unit
    ): Boolean {

        val document = app.get(data).document
        document.select("ul#loadProviders > li").map { fixUrl(it.select("a").attr("href")) }.forEach {
            loadExtractor(it.getIframe(), "https://nganunganu.sbs", subtitleCallback, callback)
        }

        return true
    }

    private suspend fun String.getIframe(): String {
        return app.get(this, referer = "$mainUrl/")
                .document
                .select("div.embed iframe")
                .attr("src")
    }
}

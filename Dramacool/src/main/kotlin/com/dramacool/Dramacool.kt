package com.dramacool

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element
import java.net.URLEncoder

class Dramacool : MainAPI() {
    override var mainUrl = "https://asianctv.net"
    override var name = "Dramacool"
    override val hasMainPage = true
    override var lang = "en"
    override val hasDownloadSupport = false
    override val usesWebView = true
    override val supportedTypes = setOf(
        TvType.AsianDrama,
        TvType.TvSeries,
        TvType.Movie,
    )

    override val mainPage = mainPageOf(
        "recently-added?page=%d" to "Recently Added",
        "recently-added-movie?page=%d" to "Recently Added Movie",
        "most-popular-drama?page=%d" to "Popular Drama",
        "recently-added-kshow?page=%d" to "Recently Added KShow",
        "genre/action?page=%d" to "Action",
        "genre/romance?page=%d" to "Romance",
        "genre/drama?page=%d" to "Drama",
        "genre/comedy?page=%d" to "Comedy",
        "genre/horror?page=%d" to "Horror",
        "genre/thriller?page=%d" to "Thriller",
        "genre/mystery?page=%d" to "Mystery",
        "genre/fantasy?page=%d" to "Fantasy",
        "genre/crime?page=%d" to "Crime",
        "genre/historical?page=%d" to "Historical",
        "genre/medical?page=%d" to "Medical",
        "genre/school?page=%d" to "School",
        "genre/bl?page=%d" to "BL",
        "genre/variety?page=%d" to "Variety",
        "country/korean-drama?page=%d" to "Korean Drama",
        "country/japanese-drama?page=%d" to "Japanese Drama",
        "country/taiwanese-drama?page=%d" to "Taiwanese Drama",
        "country/hong-kong-drama?page=%d" to "Hong Kong Drama",
        "country/chinese-drama?page=%d" to "Chinese Drama",
        "country/thailand-drama?page=%d" to "Thailand Drama",
        "country/indian-drama?page=%d" to "Indian Drama",
        "country/other-asia-drama?page=%d" to "Other Asia Drama",
        "country/korean-movie?page=%d" to "Korean Movie",
        "country/japanese-movie?page=%d" to "Japanese Movie",
        "country/taiwanese-movie?page=%d" to "Taiwanese Movie",
        "country/hong-kong-movie?page=%d" to "Hong Kong Movie",
        "country/chinese-movie?page=%d" to "Chinese Movie",
        "country/thailand-movie?page=%d" to "Thailand Movie",
        "country/indian-movie?page=%d" to "Indian Movie",
        "country/american-movie?page=%d" to "American Movie",
        "country/other-asia-movie?page=%d" to "Other Asia Movie",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data.format(page)}").document
        val home = document.select("ul.list-episode-item-2 li, div.left-tab-1 ul li, ul.switch-block li, div.content-left ul li")
            .mapNotNull { it.toSearchResult(request.name.contains("Movie", true)) }
            .filterNotNull()
            .distinctBy { it.url }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(forceMovie: Boolean = false): SearchResponse? {
        val detail = selectFirst("a[href*=/drama-detail/]")
        val episode = selectFirst("a[href*=episode-][href$=.html]")
        val anchor = detail ?: episode ?: return null
        val href = fixUrlNull(anchor.attr("href")) ?: return null
        val title = anchor.attr("title").takeIf { it.isNotBlank() }
            ?: selectFirst("h3.title")?.text()?.trim()
            ?: selectFirst("img")?.attr("alt")?.trim()
            ?: anchor.text().trim()
        if (title.isBlank()) return null

        val poster = fixUrlNull(selectFirst("img")?.attr("data-original")?.takeIf { it.isNotBlank() }
            ?: selectFirst("img")?.attr("data-src")?.takeIf { it.isNotBlank() }
            ?: selectFirst("img")?.attr("src"))
        val year = selectFirst("span.year")?.text()?.toIntOrNull()
        val type = if (forceMovie) TvType.Movie else if (href.contains("episode-")) TvType.TvSeries else getTypeFromUrl(href)

        return if (type == TvType.Movie) {
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = poster
                this.year = year
            }
        } else {
            newTvSeriesSearchResponse(title, href, TvType.AsianDrama) {
                this.posterUrl = poster
                this.year = year
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val encodedQuery = URLEncoder.encode(query, "UTF-8")

        val document = app.get("$mainUrl/search?type=movies&keyword=$encodedQuery", referer = mainUrl).document
        val htmlResults = document.select("ul.list-episode-item-2 li, div.content-left ul li").mapNotNull { el ->
            val anchor = el.selectFirst("a[href*=/drama-detail/]") ?: return@mapNotNull null
            val href = fixUrlNull(anchor.attr("href")) ?: return@mapNotNull null
            val title = anchor.attr("title").takeIf { it.isNotBlank() }
                ?: el.selectFirst("h3.title")?.text()?.trim()
                ?: el.selectFirst("img")?.attr("alt")?.trim()
                ?: anchor.text().trim()
            if (title.isBlank()) return@mapNotNull null
            val poster = fixUrlNull(el.selectFirst("img")?.attr("data-original")?.takeIf { it.isNotBlank() }
                ?: el.selectFirst("img")?.attr("data-src")?.takeIf { it.isNotBlank() }
                ?: el.selectFirst("img")?.attr("src"))
            val year = Regex("\\((\\d{4})\\)").find(title)?.groupValues?.getOrNull(1)?.toIntOrNull()
            newTvSeriesSearchResponse(title, href, TvType.AsianDrama) {
                this.posterUrl = poster
                this.year = year
            }
        }.distinctBy { it.url }

        if (htmlResults.isNotEmpty()) return htmlResults

        val apiText = app.get("$mainUrl/api?a=search&keyword=$encodedQuery&type=drama", referer = mainUrl).text
        val apiResults = runCatching {
            AppUtils.parseJson<List<SearchItem>>(apiText)
        }.getOrNull() ?: return emptyList()

        return apiResults.mapNotNull { item ->
            val title = item.name ?: item.value ?: return@mapNotNull null
            val url = fixUrlNull(item.url ?: return@mapNotNull null) ?: return@mapNotNull null
            newTvSeriesSearchResponse(title, url, TvType.AsianDrama) {
                this.posterUrl = fixUrlNull(item.cover)
                this.year = Regex("\\((\\d{4})\\)").find(title)?.groupValues?.getOrNull(1)?.toIntOrNull()
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val detailUrl = if (url.contains("/drama-detail/")) url else getDetailUrl(url) ?: url
        val document = app.get(detailUrl).document
        val title = document.selectFirst("div.info h1")?.text()?.trim() ?: return null
        val poster = fixUrlNull(document.selectFirst("div.details div.img img")?.attr("src"))
        val description = document.select("div.info p").firstOrNull { it.selectFirst("span") == null }?.text()?.trim()
        val tags = document.select("div.info p:contains(Genre:) a, div.info p:contains(Country:) a").map { it.text().trim() }
        val status = getStatus(document.selectFirst("div.info p:contains(Status:)")?.ownText())
        val year = Regex("\\((\\d{4})\\)").find(title)?.groupValues?.getOrNull(1)?.toIntOrNull()
        val episodes = document.select("a[href*=episode-][href$=.html]").mapNotNull { it.toEpisode() }
            .distinctBy { it.data }
            .sortedBy { it.episode ?: 0 }
        val recommendations = document.select("div.content-right a[href*=/drama-detail/], ul.switch-block a[href*=/drama-detail/]")
            .mapNotNull { it.parent()?.toSearchResult() ?: it.toSearchResult() }
            .distinctBy { it.url }

        return if (episodes.size <= 1 && title.contains("movie", true)) {
            newMovieLoadResponse(title, detailUrl, TvType.Movie, episodes.firstOrNull()?.data ?: detailUrl) {
                this.posterUrl = poster
                this.year = year
                this.plot = description
                this.tags = tags
                this.recommendations = recommendations
            }
        } else {
            newTvSeriesLoadResponse(title, detailUrl, TvType.AsianDrama, episodes) {
                this.posterUrl = poster
                this.year = year
                this.plot = description
                this.tags = tags
                this.showStatus = status
                this.recommendations = recommendations
            }
        }
    }

    private fun Element.toEpisode(): Episode? {
        val href = fixUrlNull(attr("href")) ?: return null
        val name = selectFirst("h3.title")?.text()?.trim() ?: text().trim()
        val epNum = Regex("Episode\\s*(\\d+)", RegexOption.IGNORE_CASE).find(name)?.groupValues?.getOrNull(1)?.toIntOrNull()
        return newEpisode(href) {
            this.name = name
            this.episode = epNum
            addDate(selectFirst("span.time")?.text())
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data, referer = mainUrl).document
        val links = document.select("div.muti_link li[data-video]").mapNotNull { li ->
            fixUrlNull(li.attr("data-video").trim()).takeIf { !it.isNullOrBlank() }
        }.distinct()
        val extractorLinks = links.filterNot { it.contains("vidbasic", true) }.takeIf { it.isNotEmpty() } ?: links
        extractorLinks.amap { link ->
            loadExtractor(resolveExtractorUrl(link), data, subtitleCallback, callback)
        }
        return true
    }

    private suspend fun resolveExtractorUrl(url: String): String {
        return runCatching {
            when {
                url.contains("hglink.to", true) -> url.replace("https://hglink.to/e/", "https://hanerix.com/e/")
                    .replace("http://hglink.to/e/", "https://hanerix.com/e/")
                else -> url
            }
        }.getOrDefault(url)
    }

    private suspend fun getDetailUrl(url: String): String? {
        return app.get(url, referer = mainUrl).document
            .selectFirst("div.category a[href*=/drama-detail/], a[href*=/drama-detail/]")
            ?.attr("href")
            ?.let { fixUrl(it) }
    }

    private fun getTypeFromUrl(url: String): TvType {
        return when {
            url.contains("/country/") && url.contains("movie") -> TvType.Movie
            url.contains("movie", true) -> TvType.Movie
            else -> TvType.AsianDrama
        }
    }

    private fun getStatus(status: String?): ShowStatus? {
        return when {
            status?.contains("ongoing", true) == true -> ShowStatus.Ongoing
            status?.contains("completed", true) == true -> ShowStatus.Completed
            else -> null
        }
    }

    data class SearchItem(
        @JsonProperty("value") val value: String?,
        @JsonProperty("url") val url: String?,
        @JsonProperty("cover") val cover: String?,
        @JsonProperty("name") val name: String?,
        @JsonProperty("status") val status: String?,
    )
}

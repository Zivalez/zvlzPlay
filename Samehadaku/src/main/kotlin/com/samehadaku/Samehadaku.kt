package com.samehadaku

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addAniListId
import com.lagradost.cloudstream3.LoadResponse.Companion.addMalId
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.LoadResponse.Companion.addScore
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.amap
import kotlinx.coroutines.runBlocking
import org.jsoup.nodes.Element

class Samehadaku : MainAPI() {
    override var mainUrl = "https://v1.samehadaku.how"
    override var name = "Samehadaku"
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
            t.contains("OVA", true) || t.contains("Special", true) -> TvType.OVA
            t.contains("Movie", true) -> TvType.AnimeMovie
            else -> TvType.Anime
        }
        fun getStatus(t: String): ShowStatus = when (t) {
            "Completed" -> ShowStatus.Completed
            "Ongoing" -> ShowStatus.Ongoing
            else -> ShowStatus.Completed
        }
    }

    override val mainPage = mainPageOf(
        "anime-terbaru/page/%d" to "Terbaru",
        "genre/action/page/%d/" to "Action",
        "anime-movie/page/%d/" to "Movie",
        "genre/sci-fi/page/%d/" to "SCI-FI",
        "genre/school/page/%d/" to "School",
        "genre/fantasy/page/%d/" to "Fantasy",
        "genre/adventure/page/%d/" to "Adventure",
    )
    
    
    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data.format(page)}").document
        val items = when (request.name) {
            "Terbaru" -> document.select("li[itemtype='http://schema.org/CreativeWork']")
            else -> document.select("article.animpost")
        }
        val homeList = items.mapNotNull {
            if (request.name == "Terbaru") it.toLatestAnimeResult()
            else it.toSearchResult()
        }
        return newHomePageResponse(request.name, homeList)
    }

    private fun Element.toSearchResult(): AnimeSearchResponse? {
        val a = this.selectFirst("div.animepost a") ?: return null
        val title = a.selectFirst("div.title h2")?.text()?.removeBloat() ?: a.attr("title")?.removeBloat() ?: return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        val posterUrl = fixUrlNull(this.selectFirst("div.content-thumb img")?.attr("src"))
        val statusText = a.selectFirst("div.data > div.type")?.text()?.trim() ?: ""

        return newAnimeSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = posterUrl
            addDubStatus(statusText)
        }
    }

    private fun Element.toLatestAnimeResult(): AnimeSearchResponse? {
        val a = this.selectFirst("div.thumb a") ?: return null
        val title = this.selectFirst("h2.entry-title a")?.text()?.removeBloat() ?: a.attr("title")?.removeBloat() ?: return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        val posterUrl = fixUrlNull(a.selectFirst("img")?.attr("src"))
        val epNum = this.selectFirst("div.dtla author")?.text()?.toIntOrNull()

        return newAnimeSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = posterUrl
            addSub(epNum)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("main#main div.animepost").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val fixUrl = if (url.contains("/anime/")) url
        else app.get(url).document.selectFirst("div.nvs.nvsc a")?.attr("href")

        val document = app.get(fixUrl ?: return null).document

        val title = document.selectFirst("h1.entry-title")?.text()?.removeBloat() ?: return null
        val poster = document.selectFirst("div.thumb > img")?.attr("src")
        val tags = document.select("div.genre-info > a").map { it.text() }
        val year = document.selectFirst("div.spe > span:contains(Rilis)")?.ownText()?.let {
            Regex("\\d,\\s(\\d*)").find(it)?.groupValues?.getOrNull(1)?.toIntOrNull()
        }
        val status = getStatus(document.selectFirst("div.spe > span:contains(Status)")?.ownText() ?: return null)
        val type = getType(document.selectFirst("div.spe > span:contains(Type)")?.ownText()?.trim()?.lowercase() ?: "tv")
        val rating = document.selectFirst("span.ratingValue")?.text()?.trim()?.toDoubleOrNull()
        val description = document.select("div.desc p").text().trim()
        val trailer = document.selectFirst("div.trailer-anime iframe")?.attr("src")

        val japName = document.selectFirst("div.spe > span:contains(Japanese)")?.ownText()?.trim()
        val engTitle = document.selectFirst("div.spe > span:contains(English)")?.ownText()?.trim()
        val duration = document.selectFirst("div.spe > span:contains(Duration)")?.ownText()
            ?.filter { it.isDigit() }?.toIntOrNull()
        val studio = document.selectFirst("div.spe > span:contains(Studio) a")?.text()?.trim()

        val episodeLinks = document.select("div.lstepsiode.listeps ul li").mapNotNull {
            val header = it.selectFirst("span.lchx > a") ?: return@mapNotNull null
            val episode = Regex("Episode\\s?(\\d+)").find(header.text())?.groupValues?.getOrNull(1)?.toIntOrNull()
            val link = fixUrl(header.attr("href"))
            Pair(link, episode)
        }.reversed()

        // Fetch episode thumbnails from the first episode page
        // (anime page has no thumbnails, but episode pages do in their episode list sidebar)
        val thumbMap: Map<String, String> = if (episodeLinks.isNotEmpty()) {
            try {
                app.get(episodeLinks.first().first).document
                    .select("div.lstepsiode.listeps ul li")
                    .mapNotNull { li ->
                        val img = li.selectFirst("div.thumbnailrighteps img") ?: return@mapNotNull null
                        val href = li.selectFirst("span.lchx > a")?.attr("href")
                            ?.let { fixUrl(it) } ?: return@mapNotNull null
                        href to img.attr("src")
                    }.toMap()
            } catch (_: Exception) { emptyMap() }
        } else emptyMap()

        val episodes = episodeLinks.map { (link, epNum) ->
            newEpisode(link) {
                this.episode = epNum
                this.posterUrl = thumbMap[link]
            }
        }

        val recommendations = document.select("aside#sidebar ul li").mapNotNull { it.toSearchResult() }

        val tracker = APIHolder.getTracker(listOf(title), TrackerType.getTypes(type), year, true)

        return newAnimeLoadResponse(title, url, type) {
            this.japName = japName
            engName = engTitle ?: title
            posterUrl = tracker?.image ?: poster
            backgroundPosterUrl = tracker?.cover
            this.year = year
            this.duration = duration
            addEpisodes(DubStatus.Subbed, episodes)
            showStatus = status
            if (rating != null) addScore(rating.toString())
            plot = description
            addTrailer(trailer)
            this.tags = if (studio != null) tags + studio else tags
            this.recommendations = recommendations
            addMalId(tracker?.malId)
            addAniListId(tracker?.aniId?.toIntOrNull())
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document

        val allItems = document.select("div#downloadb li").flatMap { el ->
            val quality = el.select("strong").text()
            el.select("a").map { a -> Triple(fixUrl(a.attr("href")), quality, a.attr("href")) }
        }

        val (krakenLinks, otherLinks) = allItems.partition { it.first.contains("krakenfiles", true) }

        krakenLinks.forEach { (url, quality, _) ->
            loadFixedExtractor(url, quality, "$mainUrl/", subtitleCallback, callback)
        }

        otherLinks.amap { (url, quality, _) ->
            loadFixedExtractor(url, quality, "$mainUrl/", subtitleCallback, callback)
        }
        return true
    }

    private suspend fun loadFixedExtractor(
        url: String,
        name: String,
        referer: String? = null,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        loadExtractor(url, referer, subtitleCallback) { link ->
            runBlocking {
                callback.invoke(
                    newExtractorLink(link.name, link.name, link.url, link.type) {
                        this.referer = link.referer
                        this.quality = name.fixQuality()
                        this.headers = link.headers
                        this.extractorData = link.extractorData
                    }
                )
            }
        }
    }

    private fun String.fixQuality(): Int = when (this.uppercase()) {
        "4K" -> Qualities.P2160.value
        "FULLHD" -> Qualities.P1080.value
        "MP4HD" -> Qualities.P720.value
        else -> this.filter { it.isDigit() }.toIntOrNull() ?: Qualities.Unknown.value
    }

    private fun String.removeBloat(): String =
        this.replace(Regex("(Nonton)|(Anime)|(Subtitle\\sIndonesia)|(Sub\\sIndo)", RegexOption.IGNORE_CASE), "").trim()
}

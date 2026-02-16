package com.winbu

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addAniListId
import com.lagradost.cloudstream3.LoadResponse.Companion.addMalId
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.runBlocking
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
        TvType.OVA
    )

    companion object {
        fun getType(t: String): TvType = when {
            t.contains("OVA", true) || t.contains("Special", true) -> TvType.OVA
            t.contains("Movie", true) -> TvType.AnimeMovie
            else -> TvType.Anime
        }

        fun getStatus(t: String): ShowStatus = when {
            t.contains("Completed", true) -> ShowStatus.Completed
            t.contains("Ongoing", true) -> ShowStatus.Ongoing
            else -> ShowStatus.Completed
        }
    }

    override val mainPage = mainPageOf(
        "anime-terbaru-animasu/page/%d/" to "Terbaru",
        "genre/action/page/%d/" to "Action",
        "genre/adventure/page/%d/" to "Adventure",
        "genre/fantasy/page/%d/" to "Fantasy",
        "genre/romance/page/%d/" to "Romance",
        "genre/comedy/page/%d/" to "Comedy",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data.format(page)}").document
        val homeList = document.select("div#movies div.ml-item").mapNotNull {
            it.toSearchResult()
        }
        return newHomePageResponse(request.name, homeList)
    }

    private fun Element.toSearchResult(): AnimeSearchResponse? {
        val a = this.selectFirst("a") ?: return null
        val title = a.selectFirst("span.mli-info h2")?.text()?.trim() ?: return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        val posterUrl = fixUrlNull(a.selectFirst("img.mli-thumb")?.attr("src")
            ?: a.selectFirst("img")?.attr("data-original")
            ?: a.selectFirst("img")?.attr("src"))

        return newAnimeSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("div#movies div.ml-item").mapNotNull {
            it.toSearchResult()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        // If URL is an episode page, navigate to the anime page
        val fixUrl = if (url.contains("/anime/")) url
        else {
            val epDoc = app.get(url).document
            epDoc.selectFirst("div.breadcrumb a[href*=/anime/]")?.attr("href") ?: url
        }

        val document = app.get(fixUrl).document

        val title = document.selectFirst("h1")?.text()?.removeBloat()?.trim()
            ?: document.selectFirst("div.sheader h1")?.text()?.removeBloat()?.trim()
            ?: return null

        val poster = document.selectFirst("div.thumb img")?.attr("src")
            ?: document.selectFirst("img.mli-thumb")?.attr("src")

        val tags = document.select("div.btm-infor a[href*=/genre/]").map { it.text() }
            .ifEmpty { document.select("div.spe a[href*=/genre/]").map { it.text() } }

        val year = document.selectFirst("div.btm-infor a[href*=/season/]")?.text()?.let {
            Regex("(\\d{4})").find(it)?.groupValues?.getOrNull(1)?.toIntOrNull()
        }

        val statusText = document.selectFirst("div.btm-infor span:contains(Status)")?.ownText()
            ?: document.selectFirst("div.spe span:contains(Status)")?.ownText()
            ?: ""
        val status = getStatus(statusText)

        val typeText = document.selectFirst("div.btm-infor span:contains(Type)")?.ownText()
            ?: document.selectFirst("div.spe span:contains(Type)")?.ownText()
            ?: "tv"
        val type = getType(typeText.trim().lowercase())

        val rating = document.selectFirst("span.ratingValue")?.text()?.trim()?.toRatingInt()
            ?: document.selectFirst("div.rating strong")?.text()?.trim()?.toRatingInt()

        val description = document.selectFirst("div.synops p")?.text()?.trim()
            ?: document.selectFirst("div.desc p")?.text()?.trim()
            ?: document.selectFirst("div.mli-mvi")?.nextElementSibling()?.text()?.trim()
            ?: ""

        val trailer = document.selectFirst("iframe[src*=youtube]")?.attr("src")
            ?: document.selectFirst("iframe[src*=youtu.be]")?.attr("src")

        // Extract episodes from .tvseason .les-content
        val episodes = document.select("div.tvseason div.les-content a").mapNotNull { el ->
            val epTitle = el.text().trim()
            val epHref = fixUrlNull(el.attr("href")) ?: return@mapNotNull null
            val epNum = Regex("Episode\\s?(\\d+)", RegexOption.IGNORE_CASE)
                .find(epTitle)?.groupValues?.getOrNull(1)?.toIntOrNull()
            newEpisode(epHref) {
                this.name = epTitle
                this.episode = epNum
            }
        }.reversed()

        val recommendations = document.select("div.rekom div.ml-item, div#movies div.ml-item-rekom").mapNotNull {
            it.toSearchResult()
        }

        val tracker = APIHolder.getTracker(listOf(title), TrackerType.getTypes(type), year, true)

        return newAnimeLoadResponse(title, url, type) {
            engName = title
            posterUrl = tracker?.image ?: poster
            backgroundPosterUrl = tracker?.cover
            this.year = year
            addEpisodes(DubStatus.Subbed, episodes)
            showStatus = status
            this.rating = rating
            plot = description
            addTrailer(trailer)
            this.tags = tags
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

        // Download links in div#downloadb — identical structure to Samehadaku
        document.select("div#downloadb li").apmap { el ->
            el.select("a").apmap {
                loadFixedExtractor(
                    fixUrl(it.attr("href")),
                    el.select("strong").text(),
                    "$mainUrl/",
                    subtitleCallback,
                    callback
                )
            }
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
        "FULLHD", "1080P" -> Qualities.P1080.value
        "MP4HD", "720P" -> Qualities.P720.value
        "480P" -> Qualities.P480.value
        "360P" -> Qualities.P360.value
        else -> this.filter { it.isDigit() }.toIntOrNull() ?: Qualities.Unknown.value
    }

    private fun String.removeBloat(): String =
        this.replace(Regex("(Nonton)|(Anime)|(Subtitle\\sIndonesia)|(Sub Indo)|(Streaming)"), "").trim()
}

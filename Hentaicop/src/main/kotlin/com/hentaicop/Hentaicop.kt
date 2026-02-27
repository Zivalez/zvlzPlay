package com.hentaicop

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import org.jsoup.nodes.Element

class Hentaicop : MainAPI() {
    override var mainUrl = "https://hentaicop.com"
    override var name = "Hentaicop"
    override val supportedTypes = setOf(TvType.NSFW)
    override var lang = "id"
    override val hasMainPage = true
    override val hasSearch = true

    override val mainPage = mainPageOf(
        "$mainUrl/" to "Terbaru",
        "$mainUrl/genre/hentai/" to "Hentai",
        "$mainUrl/genre/uncensored/" to "Uncensored",
        "$mainUrl/genre/jav/" to "JAV",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (page == 1) request.data else "${request.data}page/$page/"
        val doc = app.get(url).document
        val items = doc.select("article.bs.styletwo").map { toSearchResult(it) }
        val hasNext = doc.selectFirst(".next.page-numbers") != null
        return newHomePageResponse(request.name, items, hasNext)
    }

    private fun episodeToSeriesUrl(epUrl: String): String {
        val slug = epUrl.trimEnd('/').substringAfterLast('/')
        val seriesSlug = slug.replace(Regex("-episode-\\d+$"), "")
        return "$mainUrl/series/$seriesSlug/"
    }

    private fun toSearchResult(el: Element): SearchResponse {
        val a = el.selectFirst("a") ?: return newAnimeSearchResponse("", "", TvType.NSFW)
        val epUrl = a.attr("href")
        val seriesUrl = episodeToSeriesUrl(epUrl)
        val rawTitle = a.attr("oldtitle").ifEmpty { a.attr("title") }
        val title = rawTitle.replace(Regex("\\s+Episode\\s+\\d+$", RegexOption.IGNORE_CASE), "").trim()
        val poster = el.selectFirst("img")?.attr("src")
        return newAnimeSearchResponse(title, seriesUrl, TvType.NSFW) {
            this.posterUrl = poster
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/?s=$query").document
        return doc.select("article.bs.styletwo").map { toSearchResult(it) }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url).document

        val rawTitle = doc.selectFirst(".entry-title, h1.entry-title")?.text()
            ?: doc.title().substringBefore(" - Hentaicop").removePrefix("Nonton ")
        val poster = doc.selectFirst("img.wp-post-image")?.attr("src")
        val genres = doc.select(".genxed a").map { it.text() }
        val description = doc.selectFirst(".entry-content p, .synopsis p")?.text()

        val spe = doc.select(".spe span")
        fun speValue(key: String) = spe.firstOrNull { it.selectFirst("b")?.text() == "$key:" }
            ?.run { ownText().ifEmpty { select("a").joinToString { it.text() } } }

        val status = when (speValue("Status")) {
            "Ongoing" -> ShowStatus.Ongoing
            else -> ShowStatus.Completed
        }
        val studios = doc.select(".spe a[href*='/studio/']").map { Actor(it.text()) }
        val year = speValue("Released")?.toIntOrNull()

        val episodes = doc.select(".eplister li a").reversed().mapIndexed { idx, a ->
            val epNum = a.selectFirst(".epl-num")?.text()?.toIntOrNull() ?: (idx + 1)
            val epTitle = a.selectFirst(".epl-title")?.text() ?: "Episode $epNum"
            Episode(data = a.attr("href"), name = epTitle, episode = epNum)
        }

        return newAnimeLoadResponse(rawTitle, url, TvType.NSFW) {
            this.posterUrl = poster
            this.tags = genres
            this.plot = description
            this.showStatus = status
            this.year = year
            addEpisodes(DubStatus.Subbed, episodes)
            addActors(studios)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document
        val iframeSrc = doc.selectFirst("iframe[src*='hepidrive']")?.attr("src") ?: return false

        val embedHtml = app.get(iframeSrc, referer = data).text
        val m3u8Raw = Regex(""""file":"(https:[^"]+\.m3u8[^"]*)"""").find(embedHtml)?.groupValues?.get(1)
            ?: return false
        val m3u8Url = m3u8Raw.replace("\\/", "/")

        M3u8Helper.generateM3u8(name, m3u8Url, iframeSrc).forEach(callback)
        return true
    }
}

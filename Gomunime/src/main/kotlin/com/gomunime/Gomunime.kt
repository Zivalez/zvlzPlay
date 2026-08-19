package com.gomunime

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addAniListId
import com.lagradost.cloudstream3.LoadResponse.Companion.addMalId
import com.lagradost.cloudstream3.utils.ExtractorLink
import org.jsoup.nodes.Element
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Locale

class Gomunime : MainAPI() {
    override var mainUrl = "https://gomunime.top"
    override var name = "Gomunime"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val hasDownloadSupport = true
    override var lang = "id"
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA,
    )

    override val mainPage = mainPageOf(
        "$mainUrl/status/ongoing?page=%d" to "Ongoing",
        "$mainUrl/status/completed?page=%d" to "Completed",
        "$mainUrl/type/movie?page=%d" to "Movie",
        "$mainUrl/koleksi/anime-skor-mal-tertinggi?page=%d" to "Top Rated",
        "$mainUrl/genre/action?page=%d" to "Action",
        "$mainUrl/genre/fantasy?page=%d" to "Fantasy",
        "$mainUrl/genre/isekai?page=%d" to "Isekai",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (page == 1) request.data.replace("?page=%d", "") else request.data.format(page)
        val document = app.get(url).document
        val home = document.select("a.card-netflix, article.bs").mapNotNull { it.toSearchResult() }
        return newHomePageResponse(request.name, home)
    }

    override suspend fun quickSearch(query: String): List<SearchResponse>? = search(query)

    override suspend fun search(query: String): List<SearchResponse>? {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("a.card-netflix, article.bs").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document
        val title = document.selectFirst("h1")?.text()?.trim() ?: return null

        val poster = document.selectFirst("img[alt*=\"$title\" i], div.aspect-poster img, div.single-info .thumb img")
            ?.let { it.attr("src").ifBlank { it.attr("data-src") } }
            ?.takeIf { it.isNotBlank() }

        val bodyText = document.body().text().replace(Regex("\\s+"), " ").trim()
        val statusText = when {
            bodyText.contains("Ongoing", true) -> "Ongoing"
            bodyText.contains("Completed", true) || bodyText.contains("Tamat", true) -> "Completed"
            else -> "Completed"
        }
        val typeText = when {
            url.contains("movie", true) || bodyText.contains("Movie", true) -> "Movie"
            else -> "TV"
        }
        val year = Regex("""\b(19\d\d|20\d\d)\b""").find(bodyText)?.groupValues?.getOrNull(1)?.toIntOrNull()

        val description = document.selectFirst("p.text-ink-200, div.prose, p.leading-relaxed, div.desc, div.entry-content")?.text()?.trim()
        val tags = document.select("a[href*=\"/genre/\"]").map { it.text().trim() }.filter { it.isNotBlank() }.distinct()

        val episodes = extractEpisodeLinks(url, document)
        val tracker = APIHolder.getTracker(listOf(title), TrackerType.getTypes(getType(typeText)), year, true)

        return newAnimeLoadResponse(title, url, getType(typeText)) {
            engName = title
            posterUrl = tracker?.image ?: poster
            backgroundPosterUrl = tracker?.cover
            this.year = year
            this.tags = tags
            this.plot = description
            showStatus = getStatus(statusText)
            addEpisodes(DubStatus.Subbed, episodes)
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
        return loadGomunimeLinks(data, subtitleCallback, callback)
    }

    private fun Element.toSearchResult(): AnimeSearchResponse? {
        val a = if (tagName() == "a") this else selectFirst("a[href]") ?: return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        val img = a.selectFirst("img")
        val title = img?.attr("alt")?.trim()?.takeIf { it.isNotBlank() }
            ?: selectFirst("h1, h2, h3, h4, div.tt, span.title")?.text()?.trim()?.takeIf { it.isNotBlank() }
            ?: a.text().trim().takeIf { it.isNotBlank() }
            ?: return null
        val posterUrl = img?.let { fixUrlNull(it.attr("src").ifBlank { it.attr("data-src") }) }
        val isMovie = href.contains("movie", true)

        return newAnimeSearchResponse(title, href, if (isMovie) TvType.AnimeMovie else TvType.Anime) {
            this.posterUrl = posterUrl
        }
    }

    private fun extractEpisodeLinks(url: String, document: org.jsoup.nodes.Document): List<Episode> {
        val slug = URI(url).path.trim('/').substringAfterLast('/')

        val list = document.select("a[href*=\"-episode-\"]")
            .mapNotNull { a ->
                val href = fixUrlNull(a.attr("href")) ?: return@mapNotNull null
                val epNum = Regex("""episode-(\d+)""", RegexOption.IGNORE_CASE)
                    .find(href)?.groupValues?.getOrNull(1)?.toIntOrNull()
                    ?: Regex("""Episode\s*(\d+)""", RegexOption.IGNORE_CASE)
                        .find(a.text())?.groupValues?.getOrNull(1)?.toIntOrNull()
                newEpisode(href) {
                    this.episode = epNum
                    this.name = "Episode ${epNum ?: ""}".trim()
                }
            }
            .distinctBy { it.data }
            .sortedBy { it.episode ?: Int.MAX_VALUE }

        if (list.isNotEmpty()) return list

        if (url.contains("movie", true)) {
            return listOf(
                newEpisode(url) {
                    this.name = "Movie"
                    this.episode = 1
                }
            )
        }

        return emptyList()
    }

    private fun getType(text: String): TvType = when {
        text.contains("movie", true) -> TvType.AnimeMovie
        text.contains("ova", true) || text.contains("special", true) -> TvType.OVA
        else -> TvType.Anime
    }

    private fun getStatus(text: String): ShowStatus = when {
        text.contains("ongoing", true) -> ShowStatus.Ongoing
        text.contains("completed", true) -> ShowStatus.Completed
        else -> ShowStatus.Completed
    }

    data class ServerOption(
        val name: String,
        val url: String,
    )
}

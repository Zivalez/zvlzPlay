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
        "$mainUrl/anime/page/%d/?status=&type=&order=update" to "Terbaru",
        "$mainUrl/anime/page/%d/?status=ongoing&type=&order=update" to "Ongoing",
        "$mainUrl/anime/page/%d/?status=completed&type=&order=update" to "Completed",
        "$mainUrl/anime/page/%d/?status=&type=&order=popular" to "Popular",
        "$mainUrl/genres/action/page/%d/" to "Action",
        "$mainUrl/genres/fantasy/page/%d/" to "Fantasy",
        "$mainUrl/genres/isekai/page/%d/" to "Isekai",
        "$mainUrl/genres/reincarnation/page/%d/" to "Reincarnation",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data.format(page)).document
        val home = document.select("article.bs").mapNotNull { it.toSearchResult() }
        return newHomePageResponse(request.name, home)
    }

    override suspend fun quickSearch(query: String): List<SearchResponse>? = search(query)

    override suspend fun search(query: String): List<SearchResponse>? {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("article.bs").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document
        val title = document.selectFirst("h1.entry-title")?.text()?.trim() ?: return null

        val poster = document.selectFirst("div.single-info .thumb img")
            ?.attr("src")
            ?.takeIf { it.isNotBlank() }

        val bodyText = document.body().text().replace(Regex("\\s+"), " ").trim()
        val statusText = Regex("""Status:\s*([A-Za-z]+)""").find(bodyText)?.groupValues?.getOrNull(1) ?: ""
        val typeText = Regex("""Type:\s*([A-Za-z]+)""").find(bodyText)?.groupValues?.getOrNull(1) ?: "TV"
        val releaseText = Regex("""Released on:\s*([A-Za-z]+\s+\d{1,2},\s+\d{4})""")
            .find(bodyText)?.groupValues?.getOrNull(1)
        val year = releaseText?.let {
            runCatching {
                SimpleDateFormat("MMMM d, yyyy", Locale.US).parse(it)?.let { date ->
                    SimpleDateFormat("yyyy", Locale.US).format(date).toIntOrNull()
                }
            }.getOrNull()
        }

        val description = document.selectFirst("div.desc.mindes")?.text()?.trim()
            ?: document.selectFirst("div.entry-content")?.text()?.trim()

        val tags = Regex("""bergenre\s+([^.]*)\.""", RegexOption.IGNORE_CASE)
            .find(bodyText)
            ?.groupValues
            ?.getOrNull(1)
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?: emptyList()

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
        val a = selectFirst("a[href]") ?: return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        val title = selectFirst("div.tt h3, div.tt h2, h3")?.text()?.trim()
            ?: a.attr("title")?.trim()?.takeIf { it.isNotBlank() }
            ?: return null
        val posterUrl = selectFirst("img.ts-post-image")?.let { img ->
            fixUrlNull(img.attr("data-original").takeIf { it.isNotBlank() } ?: img.attr("src"))
        }
        val type = getType(selectFirst("div.typez")?.text()?.trim() ?: "TV")
        val epNum = selectFirst("span.epx")?.text()?.replace(Regex("\\D"), "")?.toIntOrNull()

        return when (type) {
            TvType.AnimeMovie -> newAnimeSearchResponse(title, href, TvType.AnimeMovie) {
                this.posterUrl = posterUrl
                addSub(epNum)
            }
            else -> newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = posterUrl
                addSub(epNum)
            }
        }
    }

    private fun extractEpisodeLinks(url: String, document: org.jsoup.nodes.Document): List<Episode> {
        val slug = runCatching {
            URI(url).path.trim('/').substringAfter("anime/")
        }.getOrNull()?.takeIf { it.isNotBlank() } ?: return emptyList()

        return document.select("a[href*=\"$slug-episode-\"]")
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
            .distinctBy { "${it.name}-${it.episode}" }
            .sortedBy { it.episode ?: Int.MAX_VALUE }
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

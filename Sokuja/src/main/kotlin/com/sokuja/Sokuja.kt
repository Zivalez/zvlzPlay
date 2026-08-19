package com.sokuja

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import java.net.URLDecoder
import kotlin.math.roundToInt

class Sokuja : MainAPI() {
    override var mainUrl = "https://x6.sokuja.uk"
    override var name = "Sokuja"
    override val hasQuickSearch = true
    override val hasMainPage = true
    override var lang = "id"
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA
    )

    companion object {
        fun getType(t: String?): TvType {
            return if (t != null && (t.contains("OVA", true) || t.contains("Special", true))) TvType.OVA
            else if (t != null && t.contains("Movie", true)) TvType.AnimeMovie
            else TvType.Anime
        }

        fun getStatus(t: String?): ShowStatus {
            return if (t != null && t.contains("Ongoing", true)) ShowStatus.Ongoing
            else ShowStatus.Completed
        }
    }

    override val mainPage = mainPageOf(
        "$mainUrl/?page=" to "Update Terbaru",
        "$mainUrl/anime/?status=ongoing&order=update&page=" to "Ongoing Anime",
        "$mainUrl/anime/?status=completed&order=update&page=" to "Completed Anime",
        "$mainUrl/anime/?type=movie&order=update&page=" to "Anime Movies",
    )

    private fun cleanPoster(src: String?): String? {
        if (src.isNullOrBlank()) return null
        val url = if (src.contains("_next/image")) {
            Regex("url=([^&]+)").find(src)?.groupValues?.getOrNull(1)?.let {
                try {
                    URLDecoder.decode(it, "UTF-8")
                } catch (e: Exception) {
                    null
                }
            } ?: return null
        } else src
        return fixUrlNull(url)
    }

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        val req = app.get(request.data + page)
        val document = req.document
        val home = document.select("a.group.block, a.group").mapNotNull { a ->
            val href = a.attr("href")
            if (!href.contains("subtitle-indonesia")) return@mapNotNull null
            val title = a.selectFirst("img")?.attr("alt")?.trim()?.takeIf { it.isNotEmpty() }
            if (title == null) return@mapNotNull null
            val poster = cleanPoster(a.selectFirst("img")?.attr("src"))
            val epNum = Regex("episode-(\\d+)").find(href)?.groupValues?.getOrNull(1)?.toIntOrNull()
            val typeText = a.selectFirst("span.uppercase")?.text()?.trim()
            val tvType = getType(typeText)
            val animeUrl = if (href.contains("/anime/")) fixUrl(href)
            else {
                val slug = href.trimEnd('/').substringAfterLast('/')
                    .replace(Regex("-episode-\\d+"), "")
                fixUrl("/anime/$slug/")
            }
            newAnimeSearchResponse(title, animeUrl, tvType) {
                this.posterUrl = poster
                addSub(epNum)
            }
        }.distinctBy { it.url }
        return newHomePageResponse(request.name, home)
    }

    override suspend fun quickSearch(query: String): List<SearchResponse>? = search(query)

    override suspend fun search(query: String): List<SearchResponse>? {
        val req = app.get(
            "$mainUrl/api/search?q=${java.net.URLEncoder.encode(query, "UTF-8")}&limit=20",
            headers = mapOf("Accept" to "application/json")
        )
        return req.parsedSafe<SearchResult>()?.results?.mapNotNull {
            val slug = it.slug ?: return@mapNotNull null
            newAnimeSearchResponse(
                it.title ?: return@mapNotNull null,
                fixUrl("/anime/$slug/"),
                getType(it.type)
            ) {
                this.posterUrl = fixUrlNull(it.thumbnailUrl) ?: fixUrlNull(it.coverUrl)
                this.year = it.year
                this.score = Score.from10(it.score)
                addSub(it.episodeCount)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document

        val animeSlug = url.trimEnd('/').substringAfterLast('/')
        val episodePrefix = "/$animeSlug-episode-"

        val title = document.selectFirst("h1")?.text()?.trim()
            ?: document.selectFirst("meta[property=og:title]")?.attr("content")?.trim()
            ?: throw ErrorLoadingException("Title not found")

        val poster = document.selectFirst("meta[property=og:image]")?.attr("content")
        val typeText = document.selectFirst("span.uppercase")?.text()?.trim()
        val type = getType(typeText)
        val year = Regex("(\\d{4})").find(
            document.selectFirst(".text-gray-400, span.text-xs")?.text() ?: ""
        )?.groupValues?.getOrNull(1)?.toIntOrNull()

        val statusText = document.select("span").mapNotNull {
            it.text().trim().takeIf { t -> t == "Ongoing" || t == "Completed" }
        }.firstOrNull()
        val status = getStatus(statusText)

        val tags = document.select("a[href^=/genre/]").mapNotNull {
            it.attr("href").substringAfter("/genre/").trimEnd('/').takeIf { v -> v.isNotEmpty() }
        }.distinct()

        val plot = document.selectFirst("meta[property=og:description]")?.attr("content")
            ?: document.selectFirst("meta[name=description]")?.attr("content")

        val episodes = document.select("a[href*=-episode-]").mapNotNull { a ->
            val href = a.attr("href")
            if (!href.startsWith(episodePrefix) || href.contains("/anime/")) return@mapNotNull null
            val name = a.selectFirst("span")?.text()?.trim() ?: return@mapNotNull null
            val epNum = Regex("episode\\s*([\\d.]+)").find(name)?.groupValues?.getOrNull(1)
                ?.toDoubleOrNull()?.roundToInt()
                ?: Regex("-episode-(\\d+)").find(href)?.groupValues?.getOrNull(1)?.toIntOrNull()
            newEpisode(fixUrl(href)) {
                this.name = name
                this.episode = epNum
            }
        }.distinctBy { it.data }.sortedBy { it.episode ?: 0 }

        return newAnimeLoadResponse(title, url, type, comingSoonIfNone = false) {
            engName = title
            posterUrl = poster
            this.year = year
            addEpisodes(DubStatus.Subbed, episodes)
            showStatus = status
            this.plot = plot
            this.tags = tags
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        val text = document.outerHtml()
        val episodeId = Regex("\"episodeId\"\\s*:\\s*(\\d+)").find(text)?.groupValues?.getOrNull(1)
            ?: return false

        val mirrors = app.get(
            "$mainUrl/api/video-mirrors?e=$episodeId",
            headers = mapOf("Accept" to "application/json")
        ).parsedSafe<MirrorResult>()?.mirrors ?: return false

        var found = false
        mirrors.forEach { m ->
            val embedUrl = m.embedUrl ?: return@forEach
            if (m.embedType == "mp4" || m.embedType == "raw") {
                found = true
                callback(
                    newExtractorLink(
                        source = m.serverName ?: name,
                        name = "${m.serverName ?: name} ${m.quality ?: ""}".trim(),
                        url = embedUrl,
                        type = ExtractorLinkType.VIDEO
                    ) {
                        this.referer = mainUrl
                        this.quality = m.quality?.toQualityInt() ?: Qualities.Unknown.value
                    }
                )
            }
        }
        return found
    }

    private fun String?.toQualityInt(): Int {
        return when {
            this == null -> Qualities.Unknown.value
            contains("2160", true) || contains("4k", true) -> Qualities.P2160.value
            contains("1080", true) -> Qualities.P1080.value
            contains("720", true) -> Qualities.P720.value
            contains("480", true) -> Qualities.P480.value
            contains("360", true) -> Qualities.P360.value
            else -> Qualities.Unknown.value
        }
    }

    data class SearchResult(
        @JsonProperty("results") val results: List<SearchItem> = emptyList()
    )

    data class SearchItem(
        @JsonProperty("id") val id: Int? = null,
        @JsonProperty("slug") val slug: String? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("thumbnailUrl") val thumbnailUrl: String? = null,
        @JsonProperty("coverUrl") val coverUrl: String? = null,
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("status") val status: String? = null,
        @JsonProperty("year") val year: Int? = null,
        @JsonProperty("score") val score: String? = null,
        @JsonProperty("episodeCount") val episodeCount: Int? = null
    )

    data class MirrorResult(
        @JsonProperty("mirrors") val mirrors: List<Mirror> = emptyList()
    )

    data class Mirror(
        @JsonProperty("id") val id: Int? = null,
        @JsonProperty("serverName") val serverName: String? = null,
        @JsonProperty("embedUrl") val embedUrl: String? = null,
        @JsonProperty("embedType") val embedType: String? = null,
        @JsonProperty("quality") val quality: String? = null
    )
}
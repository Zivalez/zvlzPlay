package com.hentaihaven

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import android.util.Base64
import okhttp3.MultipartBody
import org.jsoup.nodes.Element

class Hentaihaven : MainAPI() {
    override var mainUrl = "https://hentaihaven.com"
    override var name = "HentaiHaven"
    override val hasMainPage = true
    override var lang = "en"
    override val hasDownloadSupport = false
    override val supportedTypes = setOf(TvType.NSFW)

    override val mainPage = mainPageOf(
        "$mainUrl/page/%d/" to "New Hentai",
        "$mainUrl/genre/uncensored-hentai/page/%d/" to "Uncensored Hentai",
        "$mainUrl/genre/milf/page/%d/" to "MILF Hentai",
        "$mainUrl/genre/bbw/page/%d/" to "BBW Hentai",
        "$mainUrl/genre/femdom/page/%d/" to "Femdom Hentai",
        "$mainUrl/genre/harem/page/%d/" to "Harem Hentai",
        "$mainUrl/genre/ecchi/page/%d/" to "Ecchi Hentai",
        "$mainUrl/genre/bdsm/page/%d/" to "BDSM Hentai",
        "$mainUrl/genre/yuri/page/%d/" to "Yuri Hentai",
        "$mainUrl/genre/tentacle/page/%d/" to "Tentacle Hentai",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data.format(page)).document
        val home = document.select("div.item").mapNotNull { it.toSearchResult() }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): AnimeSearchResponse? {
        val a = this.selectFirst("a[href*='/video/']") ?: return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        val title = a.selectFirst(".title")?.text()?.trim() ?: return null
        val posterUrl = fixUrlNull(a.selectFirst("img")?.attr("src"))
        return newAnimeSearchResponse(title, href, TvType.NSFW) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        val document = app.get("$mainUrl/?s=$query&post_type=anime").document
        return document.select("div.item").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document

        val title = document.selectFirst("h1.htitle")?.text()?.trim() ?: return null
        val poster = document.selectFirst(".hentai_cover img")?.attr("src")
        val synopsis = document.select(".vraven_text p")
            .filter { it.text().length > 20 }
            .joinToString("\n\n") { it.text().trim() }
            .ifBlank { null }
        val genres = document.select("a[href*='/genre/']")
            .map { it.text().trim() }
            .filter { it.isNotBlank() }
            .distinct()

        val episodes = document.select("li.hentai__chapter a[href*='/episode-']").mapNotNull { a ->
            val epUrl = fixUrlNull(a.attr("href")) ?: return@mapNotNull null
            val epTitle = a.selectFirst("span.title")?.text()?.trim() ?: ""
            val epNum = Regex("(\\d+)").find(epTitle)?.groupValues?.getOrNull(1)?.toIntOrNull()
            val epThumbnail = fixUrlNull(a.selectFirst("img")?.attr("src"))
            newEpisode(epUrl) {
                name = epTitle
                episode = epNum
                posterUrl = epThumbnail
            }
        }.reversed()

        return newAnimeLoadResponse(title, url, TvType.NSFW) {
            posterUrl = poster
            plot = synopsis
            this.tags = genres
            addEpisodes(DubStatus.Subbed, episodes)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document

        val iframeSrc = document.selectFirst("iframe[src*='player.php']")?.attr("src")
            ?: return false

        val dataParam = iframeSrc.substringAfter("data=").substringBefore("&")
            .takeIf { it.isNotBlank() } ?: return false

        val decoded = base64Decode(dataParam)
        val parts = decoded.split("|::|:")
        if (parts.size < 2) return false

        val a = parts[0]
        val b = Base64.encodeToString(parts[1].toByteArray(), Base64.NO_WRAP)

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("action", "zarat_get_data_player_ajax")
            .addFormDataPart("a", a)
            .addFormDataPart("b", b)
            .build()

        val apiResponse = app.post(
            "$mainUrl/wp-content/plugins/player-logic/api.php",
            requestBody = requestBody,
            referer = data
        ).text

        val m3u8Url = tryParseJson<ApiResponse>(apiResponse)
            ?.data?.sources?.firstOrNull()?.src
            ?: return false

        M3u8Helper.generateM3u8(
            name,
            m3u8Url,
            "$mainUrl/",
        ).forEach(callback)

        return true
    }

    data class VideoSource(
        @JsonProperty("src") val src: String? = null,
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("label") val label: String? = null,
    )

    data class ApiData(
        @JsonProperty("sources") val sources: List<VideoSource>? = null,
    )

    data class ApiResponse(
        @JsonProperty("status") val status: Boolean? = null,
        @JsonProperty("data") val data: ApiData? = null,
    )
}

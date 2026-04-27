// Kuramanime\src\main\kotlin\com\kuramanime\Kuramanime.kt

package com.kuramanime

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addAniListId
import com.lagradost.cloudstream3.LoadResponse.Companion.addMalId
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.network.WebViewResolver
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Kuramanime : MainAPI() {
    override var mainUrl = "https://v17.kuramanime.ink"
    override var name = "Kuramanime"
    override val hasQuickSearch = false
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie, TvType.OVA)

    // Add User-Agent to bypass potential bot checks
    private val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    private val commonHeaders = mapOf(
        "User-Agent" to userAgent,
        "Referer" to mainUrl,
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Language" to "en-US,en;q=0.5",
        "Sec-Ch-Ua" to "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"",
        "Sec-Ch-Ua-Mobile" to "?0",
        "Sec-Ch-Ua-Platform" to "\"Windows\"",
        "Sec-Fetch-Dest" to "document",
        "Sec-Fetch-Mode" to "navigate",
        "Sec-Fetch-Site" to "same-origin",
        "Sec-Fetch-User" to "?1",
        "Upgrade-Insecure-Requests" to "1"
    )

    companion object {
        private const val TAG = "Kuramanime"

        fun getType(t: String, s: Int): TvType {
            return if (t.contains("OVA", true) || t.contains("Special")) TvType.OVA
            else if (t.contains("Movie", true) && s == 1) TvType.AnimeMovie else TvType.Anime
        }

        fun getStatus(t: String): ShowStatus {
            return when (t) {
                "Selesai Tayang" -> ShowStatus.Completed
                "Sedang Tayang" -> ShowStatus.Ongoing
                else -> ShowStatus.Completed
            }
        }
    }

    override val mainPage =
            mainPageOf(
                    "$mainUrl/anime/ongoing?order_by=updated&page=" to "Sedang Tayang",
                    "$mainUrl/anime/finished?order_by=updated&page=" to "Selesai Tayang",
                    "$mainUrl/properties/season/summer-2022?order_by=most_viewed&page=" to
                            "Dilihat Terbanyak Musim Ini",
                    "$mainUrl/anime/movie?order_by=updated&page=" to "Film Layar Lebar",
            )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data + page, headers = commonHeaders).document

        val home =
                document.select("div.col-lg-4.col-md-6.col-sm-6").mapNotNull { it.toSearchResult() }

        return newHomePageResponse(request.name, home)
    }

    private fun getProperAnimeLink(uri: String): String {
        return if (uri.contains("/episode")) {
            Regex("(.*)/episode/.+").find(uri)?.groupValues?.get(1).toString() + "/"
        } else {
            uri
        }
    }

    private fun Element.toSearchResult(): AnimeSearchResponse? {
        val href = getProperAnimeLink(fixUrl(this.selectFirst("a")!!.attr("href")))
        val title = this.selectFirst("h5 a")?.text() ?: return null
        val posterUrl = fixUrl(this.select("div.product__item__pic.set-bg").attr("data-setbg"))
        val episode =
                this.select("div.ep span").text().let {
                    Regex("Ep\\s(\\d+)\\s/").find(it)?.groupValues?.getOrNull(1)?.toIntOrNull()
                }

        return newAnimeSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = posterUrl
            addSub(episode)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val link = "$mainUrl/anime?search=$query&order_by=latest"
        val document = app.get(link, headers = commonHeaders).document

        return document.select("div#animeList div.product__item").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url, headers = commonHeaders).document

        val title = document.selectFirst(".anime__details__title > h3")!!.text().trim()
        val poster = document.selectFirst(".anime__details__pic")?.attr("data-setbg")
        val tags =
                document.select(
                                "div.anime__details__widget > div > div:nth-child(2) > ul > li:nth-child(1)"
                        )
                        .text()
                        .trim()
                        .replace("Genre: ", "")
                        .split(", ")

        val year =
                Regex("\\D")
                        .replace(
                                document.select(
                                                "div.anime__details__widget > div > div:nth-child(1) > ul > li:nth-child(5)"
                                        )
                                        .text()
                                        .trim()
                                        .replace("Musim: ", ""),
                                ""
                        )
                        .toIntOrNull()
        val status =
                getStatus(
                        document.select(
                                        "div.anime__details__widget > div > div:nth-child(1) > ul > li:nth-child(3)"
                                )
                                .text()
                                .trim()
                                .replace("Status: ", "")
                )
        val description = document.select(".anime__details__text > p").text().trim()

        val episodes = mutableListOf<Episode>()

        for (i in 1..10) {
            val doc = app.get("$url?page=$i", headers = commonHeaders).document
            val eps =
                    Jsoup.parse(doc.select("#episodeLists").attr("data-content"))
                            .select("a.btn.btn-sm.btn-danger")
                            .mapNotNull {
                                val name = it.text().trim()
                                val episode =
                                        Regex("(\\d+[.,]?\\d*)")
                                                .find(name)
                                                ?.groupValues
                                                ?.getOrNull(0)
                                                ?.toIntOrNull()
                                val link = it.attr("href")
                                newEpisode(link){
                                    this.episode = episode
                                }
                            }
            if (eps.isEmpty()) break else episodes.addAll(eps)
        }

        val type =
                getType(
                        document.selectFirst("div.col-lg-6.col-md-6 ul li:contains(Tipe:) a")
                                ?.text()
                                ?.lowercase()
                                ?: "tv",
                        episodes.size
                )
        val recommendations =
                document.select("div#randomList > a").mapNotNull {
                    val epHref = it.attr("href")
                    val epTitle = it.select("h5.sidebar-title-h5.px-2.py-2").text()
                    val epPoster =
                            it.select(".product__sidebar__view__item.set-bg").attr("data-setbg")
                    newAnimeSearchResponse(epTitle, epHref, TvType.Anime) {
                        this.posterUrl = epPoster
                        addDubStatus(dubExist = false, subExist = true)
                    }
                }

        val tracker = APIHolder.getTracker(listOf(title), TrackerType.getTypes(type), year, true)

        return newAnimeLoadResponse(title, url, type) {
            engName = title
            posterUrl = tracker?.image ?: poster
            backgroundPosterUrl = tracker?.cover
            this.year = year
            addEpisodes(DubStatus.Subbed, episodes)
            showStatus = status
            plot = description
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
        // Step 1: Initial GET — kemungkinan dapat halaman v1 (decoy)
        val initialResponse = app.get(data, headers = commonHeaders)
        var document = initialResponse.document

        // Step 2: Detect halaman decoy via DOM struktur.
        val isDecoy = document.select("#animeDownloadLink .reload-error").isNotEmpty() ||
            document.select("#animeDownloadLink a[href]").isEmpty()
        Log.d(TAG, "loadLinks: data=$data, isDecoy=$isDecoy")

        var streamCaptured = false

        if (isDecoy) {
            // Strategi 1: Capture stream URL (m3u8 atau direct video) dari traffic WebView.
            // Pattern Idlix yang terbukti work: WebView load page, leviathan.js eksekusi,
            // m3u8/video URL ke-fire saat player init → kita intercept URL-nya.
            try {
                val streamResolver = WebViewResolver(
                    interceptUrl = Regex("""\.m3u8(\?|$)|kuramadrive\.com/k(drive|turbo)/[a-zA-Z0-9_\-]+(/|\?|$)|r2\.cloudflarestorage\.com"""),
                    additionalUrls = listOf(Regex("""kuramadrive\.com|cloudflarestorage""")),
                    userAgent = userAgent,
                    useOkhttp = false,
                    timeout = 30_000L
                )
                val streamResp = app.get(
                    data,
                    headers = commonHeaders,
                    interceptor = streamResolver,
                    cookies = initialResponse.cookies
                )
                val streamUrl = streamResp.url
                Log.d(TAG, "stream WebView: code=${streamResp.code}, url=$streamUrl")

                if (streamUrl.isNotBlank() && streamUrl != data && (
                        streamUrl.contains(".m3u8") ||
                            streamUrl.contains("kuramadrive.com/k") ||
                            streamUrl.contains("cloudflarestorage.com")
                        )) {
                    val type = when {
                        streamUrl.contains(".m3u8") -> ExtractorLinkType.M3U8
                        else -> ExtractorLinkType.VIDEO
                    }
                    callback.invoke(
                        newExtractorLink(name, "Kuramadrive", streamUrl, type) {
                            this.referer = mainUrl
                            this.quality = Qualities.Unknown.value
                        }
                    )
                    streamCaptured = true
                    Log.d(TAG, "stream captured: $streamUrl")
                }
            } catch (e: Exception) {
                Log.e(TAG, "stream WebView failed: ${e.message}")
            }

            // Strategi 2: Capture v2 HTML untuk download links section.
            try {
                val v2Resolver = WebViewResolver(
                    interceptUrl = Regex("""/anime/\d+/[^/]+/episode/\d+\?[^"']*page=\d+"""),
                    userAgent = userAgent,
                    useOkhttp = false,
                    timeout = 25_000L
                )
                val v2Resp = app.get(
                    data,
                    headers = commonHeaders,
                    interceptor = v2Resolver,
                    cookies = initialResponse.cookies
                )
                val webDoc = v2Resp.document
                val linkCount = webDoc.select("#animeDownloadLink a[href]").size
                Log.d(TAG, "v2 WebView: code=${v2Resp.code}, bodyLen=${v2Resp.text.length}, links=$linkCount")
                if (linkCount > 0) {
                    document = webDoc
                }
            } catch (e: Exception) {
                Log.e(TAG, "v2 WebView failed: ${e.message}")
            }
        }

        // Step 4: Extract video player URL dari document (kalau ada).
        val player = document.selectFirst("video#player")
        val directLink = player?.attr("src").orEmpty()
        if (directLink.contains("r2.cloudflarestorage.com")) {
            callback.invoke(
                newExtractorLink(
                    name,
                    "Kuramadrive R2",
                    directLink,
                    ExtractorLinkType.VIDEO
                ) {
                    this.referer = mainUrl
                    this.quality = Qualities.Unknown.value
                }
            )
            streamCaptured = true
        }
        val hlsSrc = player?.attr("data-hls-src").orEmpty()
        if (hlsSrc.isNotBlank() && hlsSrc.startsWith("http")) {
            callback.invoke(
                newExtractorLink(
                    name,
                    "Kuramadrive HLS",
                    hlsSrc,
                    ExtractorLinkType.M3U8
                ) {
                    this.referer = mainUrl
                    this.quality = Qualities.Unknown.value
                }
            )
            streamCaptured = true
        }

        // Step 5: Download Links — iterasi <h6> + <a> bergantian.
        val downloadSection = document.selectFirst("div#animeDownloadLink")
        var currentQuality = "Unknown"
        var dlCount = 0

        downloadSection?.children()?.forEach { element ->
            when (element.tagName().lowercase()) {
                "h6" -> currentQuality = element.text().trim()
                "a" -> {
                    val url = element.attr("href")
                    if (url.isNotBlank() && !url.startsWith("#")) {
                        dlCount++
                        when {
                            url.contains("dropbox.com") -> {
                                handleDropbox(url, currentQuality, callback)
                            }
                            url.contains("mypikpak.com") -> {
                                // Skip — butuh auth
                            }
                            else -> loadFixedExtractor(
                                url,
                                currentQuality,
                                mainUrl,
                                subtitleCallback,
                                callback
                            )
                        }
                    }
                }
            }
        }

        Log.d(TAG, "loadLinks done: streamCaptured=$streamCaptured, dlLinks=$dlCount")
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

    private fun String.fixQuality(): Int {
        return when {
            this.contains("4K", true) -> Qualities.P2160.value
            this.contains("1080", true) -> Qualities.P1080.value
            this.contains("720", true) -> Qualities.P720.value
            this.contains("480", true) -> Qualities.P480.value
            this.contains("360", true) -> Qualities.P360.value
            else -> Qualities.Unknown.value
        }
    }

    /**
     * Dropbox download URL: convert `dl=0` (preview page) → `dl=1` (direct file).
     * Format input: `https://www.dropbox.com/scl/fi/.../filename.mkv?rlkey=...&dl=0`
     * Setelah `dl=1`, Dropbox akan redirect ke `dl.dropboxusercontent.com/...` direct file.
     * Player CloudStream bisa langsung handle URL final tersebut sebagai VIDEO.
     */
    private suspend fun handleDropbox(
        url: String,
        quality: String,
        callback: (ExtractorLink) -> Unit
    ) {
        val direct = when {
            url.contains("dl=0") -> url.replace("dl=0", "dl=1")
            url.contains("dl=1") -> url
            url.contains("?") -> "$url&dl=1"
            else -> "$url?dl=1"
        }
        callback.invoke(
            newExtractorLink(
                name,
                "Dropbox - $quality",
                direct,
                ExtractorLinkType.VIDEO
            ) {
                this.referer = mainUrl
                this.quality = quality.fixQuality()
            }
        )
    }
}

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

        val initialResponse = app.get(data, headers = commonHeaders)
        var document = initialResponse.document

        val isDecoy = document.select("#animeDownloadLink a[href]").isEmpty() ||
            document.select("video#player source[src]").isEmpty()
        Log.d(TAG, "loadLinks: data=$data, isDecoy=$isDecoy")

        if (isDecoy) {
            val v2Doc = tryWebViewBypass(data, initialResponse.cookies)
            if (v2Doc != null) {
                document = v2Doc
                Log.d(TAG, "WebView bypass success")
            } else {
                Log.d(TAG, "WebView bypass failed")
            }
        }

        var streamCount = 0
        document.select("video#player source[src]").forEach { source ->
            val src = source.attr("src")
            val isStreamable = src.contains(".mp4", true) ||
                src.contains("kuramadrive", true) ||
                src.contains("cloudflarestorage", true) ||
                Regex("""\.my\.id/""").containsMatchIn(src)
            if (src.isBlank() || !isStreamable) return@forEach

            val size = source.attr("size")
            val qualityLabel = if (size.isNotBlank()) "${size}p" else "Auto"
            val q = qualityLabel.fixQuality()

            callback.invoke(
                newExtractorLink(
                    name,
                    "Kuramadrive Stream - $qualityLabel",
                    src,
                    ExtractorLinkType.VIDEO
                ) {
                    this.referer = mainUrl
                    this.quality = q
                }
            )
            streamCount++
        }

        val hlsSrc = document.selectFirst("video#player")?.attr("data-hls-src").orEmpty()
        if (hlsSrc.isNotBlank() && hlsSrc.startsWith("http")) {
            callback.invoke(
                newExtractorLink(name, "Kuramadrive HLS", hlsSrc, ExtractorLinkType.M3U8) {
                    this.referer = mainUrl
                    this.quality = Qualities.Unknown.value
                }
            )
            streamCount++
        }

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
                            url.contains("dropbox.com") ->
                                handleDropbox(url, currentQuality, callback)
                            url.contains("mypikpak.com") -> {
                            }
                            else -> loadFixedExtractor(
                                url, currentQuality, mainUrl, subtitleCallback, callback
                            )
                        }
                    }
                }
            }
        }

        Log.d(TAG, "loadLinks done: streams=$streamCount, dlLinks=$dlCount")
        return streamCount > 0 || dlCount > 0
    }

    private suspend fun tryWebViewBypass(
        url: String,
        @Suppress("UNUSED_PARAMETER") cookies: Map<String, String>
    ): org.jsoup.nodes.Document? {
        val capturedHtml = java.util.concurrent.atomic.AtomicReference<String?>(null)
        val callbackInvocations = java.util.concurrent.atomic.AtomicInteger(0)

        val script = """
            (function() {
                try {
                    var sources = document.querySelectorAll('video#player source[src]');
                    var dlLinks = document.querySelectorAll('#animeDownloadLink a[href]');
                    if (sources.length > 0 && dlLinks.length > 0) {
                        return document.documentElement.outerHTML;
                    }
                    return null;
                } catch(e) { return 'ERR:' + e.message; }
            })()
        """.trimIndent()

        val resolver = WebViewResolver(
            interceptUrl = Regex("""__KURAMANIME_WV_NEVER_MATCH__"""),
            additionalUrls = listOf(Regex(""".*""")),
            userAgent = userAgent,
            useOkhttp = false,
            script = script,
            scriptCallback = { result ->
                callbackInvocations.incrementAndGet()
                val rLen = result.length
                if (rLen in 1..4999 && result != "null" && result != "\"null\"" && capturedHtml.get() == null) {
                    Log.d(TAG, "scriptCallback partial (len=$rLen): ${result.take(200)}")
                }
                if (result.length > 1000 && result != "null" && !result.startsWith("\"ERR:")) {
                    val decoded = try {
                        org.json.JSONArray("[$result]").getString(0)
                    } catch (e: Exception) {
                        Log.e(TAG, "scriptCallback JSON decode failed: ${e.message}")
                        null
                    }
                    if (decoded != null && decoded.contains("<source", ignoreCase = true)) {
                        if (capturedHtml.get() == null) {
                            Log.d(TAG, "scriptCallback: captured v2 HTML, len=${decoded.length}")
                        }
                        capturedHtml.set(decoded)
                    }
                }
            },
            timeout = 30_000L
        )

        try {
            resolver.resolveUsingWebView(
                url = url,
                referer = mainUrl,
                headers = commonHeaders,
                method = "GET",
                requestCallBack = { capturedHtml.get() != null }
            )
        } catch (e: Exception) {
            Log.e(TAG, "tryWebViewBypass exception: ${e.message}")
        }

        val html = capturedHtml.get()
        Log.d(TAG, "tryWebViewBypass done: htmlLen=${html?.length ?: 0}, scriptCalls=${callbackInvocations.get()}")
        if (html.isNullOrEmpty()) return null

        val doc = Jsoup.parse(html, url)
        val hasSources = doc.select("video#player source[src]").isNotEmpty()
        val hasDlLinks = doc.select("#animeDownloadLink a[href]").isNotEmpty()
        Log.d(TAG, "tryWebViewBypass parsed: hasSources=$hasSources, hasDlLinks=$hasDlLinks")

        return if (hasSources || hasDlLinks) doc else null
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
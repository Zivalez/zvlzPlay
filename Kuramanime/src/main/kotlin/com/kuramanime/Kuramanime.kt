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
        // ============================================================
        // FAKTA TEKNIS (terkonfirmasi via inspeksi network + JS):
        // ============================================================
        // 1. Halaman v1 (decoy) di-render saat GET awal — section
        //    #animeDownloadLink berisi <span class="reload-error">.
        // 2. Bypass mengharuskan POST ke URL yang sama dengan query
        //    `?Ub3BzhijicHXZdv=<URL_TOKEN>&C2XAPerzX1BM7V9=kuramadrive&page=1`
        //    + body `authorization=<COMPUTED_TOKEN>` (form-urlencoded).
        // 3. URL_TOKEN didapat dari GET /assets/Ks6sqSgloPTlHMl.txt
        //    dengan header x-fuck-id (BISA direplikasi native).
        // 4. COMPUTED_TOKEN di-generate client-side oleh leviathan.js
        //    yang heavy-obfuscated → TIDAK BISA direplikasi native.
        //    Token ini SESSION-SCOPED (tetap sama selama session aktif).
        // 5. Response v2 HTML berisi:
        //    - <video><source>...</source></video> dengan 3 quality MP4
        //      di rotating R2 domain (anisphia/asuna/amiya/iino/chisato/
        //      kitasan/komari/horikita).my.id/kdrive/<hash>/...mp4
        //    - <div id="animeDownloadLink"> dengan link kdrive/dropbox/dll.
        //
        // STRATEGI: WebView wajib karena harus eksekusi leviathan.js.
        // ============================================================

        val initialResponse = app.get(data, headers = commonHeaders)
        var document = initialResponse.document

        // Detect halaman decoy berdasarkan struktur DOM:
        // - v1 decoy: #animeDownloadLink berisi .reload-error span (kosong dari <a>)
        // - v2 real:  #animeDownloadLink berisi <h6>+<a> bergantian, +
        //             <video#player> punya >=1 <source> dengan src berisi /kdrive/
        val isDecoy = document.select("#animeDownloadLink a[href]").isEmpty() ||
            document.select("video#player source[src*=/kdrive/]").isEmpty()
        Log.d(TAG, "loadLinks: data=$data, isDecoy=$isDecoy")

        if (isDecoy) {
            // Bypass via WebView. Regex match request response yang membawa v2 HTML
            // (POST ke episode URL dengan query token + page=N). WebView load page,
            // leviathan.js execute → POST fire → kita intercept response.
            //
            // Catatan: jika WebViewResolver di environment ini cuma capture navigation
            // (bukan XHR), regex tetap perlu dicoba — match XHR atau navigation
            // sama-sama akan return v2 HTML.
            val v2Doc = tryWebViewBypass(data, initialResponse.cookies)
            if (v2Doc != null) {
                document = v2Doc
                Log.d(TAG, "WebView bypass success")
            } else {
                Log.d(TAG, "WebView bypass failed — falling back to v1 doc (no links expected)")
            }
        }

        // ============================================================
        // EXTRACT STREAMS dari <source> elements (v2 punya 3 quality)
        // ============================================================
        // Pattern domain: rotating *.my.id/kdrive/<hash>/Filename-XXXp.mp4
        // Contoh: https://iino.my.id/kdrive/l48Ep4svpECT/Kuramanime-TOBATLR-04-720p.mp4?lud=...&pid=...&sid=...
        // URL ini sudah DIRECT — gak perlu di-extract lagi, langsung emit sebagai VIDEO.
        var streamCount = 0
        document.select("video#player source").forEach { source ->
            val src = source.attr("src")
            if (src.isBlank() || !src.contains("/kdrive/")) return@forEach

            val size = source.attr("size") // "720", "480", "360"
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

        // HLS source (alternative — kadang ada untuk stream live/long anime)
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

        // ============================================================
        // EXTRACT DOWNLOAD LINKS dari #animeDownloadLink
        // ============================================================
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
                                // Skip — butuh auth login
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

    /**
     * Bypass v1 decoy → v2 real page menggunakan WebViewResolver.
     *
     * Kenapa pendekatan interceptor biasa GAGAL:
     *   - WebViewResolver sebagai okhttp Interceptor cuma capture URL match,
     *     dan body yang di-return adalah body GET awal (= v1 decoy 67KB).
     *   - Body POST response (= v2 90KB) TIDAK accessible via shouldInterceptRequest
     *     karena WebView abstraksi response body dari WebViewClient callback.
     *
     * Pendekatan yang BENAR (terbukti via reading SDK source):
     *   - Pakai parameter `script` + `scriptCallback` di WebViewResolver yang
     *     internally pakai `view.evaluateJavascript(script) { scriptCallback(...) }`.
     *   - Script di-eksekusi pada SETIAP shouldInterceptRequest (banyak kali selama
     *     page load). Kita poll DOM, return HTML lengkap kalau v2 sudah ter-inject.
     *   - Pakai `resolveUsingWebView(...)` langsung (bukan via app.get interceptor)
     *     karena script param hanya bekerja di flow ini.
     */
    private suspend fun tryWebViewBypass(
        url: String,
        cookies: Map<String, String>
    ): org.jsoup.nodes.Document? {
        val capturedHtml = java.util.concurrent.atomic.AtomicReference<String?>(null)

        // JS yang dieksekusi via evaluateJavascript pada tiap request load.
        // - Kalau DOM masih v1: return null (skip)
        // - Kalau DOM sudah v2 (ada <source src=".../kdrive/...">): return outerHTML
        // evaluateJavascript akan JSON-encode hasilnya sebelum di-pass ke callback.
        val script = """
            (function() {
                try {
                    var sources = document.querySelectorAll('video#player source[src*="/kdrive/"]');
                    var dlLinks = document.querySelectorAll('#animeDownloadLink a[href]');
                    if (sources.length > 0 && dlLinks.length > 0) {
                        return document.documentElement.outerHTML;
                    }
                    return null;
                } catch(e) { return null; }
            })()
        """.trimIndent()

        val resolver = WebViewResolver(
            // interceptUrl gak akan match — biarkan WebView jalan sampai timeout
            // atau sampai requestCallBack return true.
            interceptUrl = Regex("""__KURAMANIME_WV_NEVER_MATCH__"""),
            // Match-all → requestCallBack di-check tiap resource load → destroy
            // WebView dini setelah scriptCallback berhasil capture v2 HTML.
            additionalUrls = listOf(Regex(""".*""")),
            userAgent = userAgent,
            useOkhttp = false,
            script = script,
            scriptCallback = { result ->
                // result adalah JSON-encoded string. JS return null → result = "null".
                // JS return "<html>..." → result = "\"<html>...\"" (escaped).
                if (result != null && result.length > 5000 && result != "null") {
                    val decoded = try {
                        // Decode JSON string. JSONArray trick handles all escape sequences.
                        org.json.JSONArray("[$result]").getString(0)
                    } catch (e: Exception) {
                        null
                    }
                    if (decoded != null && decoded.contains("/kdrive/")) {
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
            // Pakai resolveUsingWebView langsung (bukan interceptor).
            // requestCallBack return true → destroy WebView segera setelah HTML ter-capture.
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
        Log.d(TAG, "tryWebViewBypass done: htmlLen=${html?.length ?: 0}")
        if (html.isNullOrEmpty()) return null

        val doc = Jsoup.parse(html, url)
        val hasSources = doc.select("video#player source[src*=/kdrive/]").isNotEmpty()
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
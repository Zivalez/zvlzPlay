// zvlzPlay\Kuramanime\src\main\kotlin\com\kuramanime\Kuramanime.kt

package com.kuramanime

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
    override var mainUrl = "https://v15.kuramanime.tel"
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
        var document = app.get(data, headers = commonHeaders).document

        // Check for protection error (Halaman Decoy v1)
        if (document.select("#animeDownloadLink .reload-error").isNotEmpty() || document.select("#animeVideoPlayer .reload-error").isNotEmpty()) {
            var bypassed = false
            
            // Fast Path: Dinamis bypass token dari file config JS
            try {
                // 1. Cari identifier JS dari attribute data-kk
                val jsId = document.selectFirst("[data-kk]")?.attr("data-kk")
                
                if (jsId != null) {
                    val jsUrl = "$mainUrl/assets/js/$jsId.js"
                    val jsContent = app.get(jsUrl, headers = commonHeaders).text
                    
                    // 2. Ekstrak config parameter pake Regex
                    val routeParam = Regex("""MIX_AUTH_ROUTE_PARAM:\s*['"]([^'"]+)['"]""").find(jsContent)?.groupValues?.get(1)
                    val pageTokenKey = Regex("""MIX_PAGE_TOKEN_KEY:\s*['"]([^'"]+)['"]""").find(jsContent)?.groupValues?.get(1)
                    val streamServerKey = Regex("""MIX_STREAM_SERVER_KEY:\s*['"]([^'"]+)['"]""").find(jsContent)?.groupValues?.get(1)
                    
                    if (routeParam != null && pageTokenKey != null && streamServerKey != null) {
                        // 3. Ambil token dinamis dari file .txt
                        val tokenUrl = "$mainUrl/assets/$routeParam"
                        val token = app.get(tokenUrl, headers = commonHeaders).text.trim()
                        
                        if (token.isNotEmpty() && token.length < 50) {
                            // 4. Hit POST buat dapetin halaman aslinya (v2)
                            val postUrl = "$data?$pageTokenKey=$token&$streamServerKey=kuramadrive&page=1"
                            val postHeaders = commonHeaders + mapOf("X-Requested-With" to "XMLHttpRequest")
                            val postResponse = app.post(postUrl, headers = postHeaders)
                            
                            if (postResponse.code == 200 && !postResponse.text.contains("Terjadi kesalahan")) {
                                document = postResponse.document
                                bypassed = true
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore, lanjut ke WebView fallback
            }

            // Slow Path: WebView fallback
            if (!bypassed) {
                val resolver = WebViewResolver(
                    Regex("""episode/\d+\?.*"""),
                    userAgent = userAgent
                )
                val webViewResponse = app.get(data, headers = commonHeaders, interceptor = resolver)
                if (webViewResponse.code == 200) {
                    document = webViewResponse.document
                }
            }
        }

        // 1. Direct Stream (Ekstrak dari tag <source> di dalem <video>)
        document.select("video#player source").forEach { source ->
            val srcUrl = source.attr("src")
            val size = source.attr("size").toIntOrNull() ?: Qualities.Unknown.value
            
            if (srcUrl.isNotBlank() && srcUrl.contains("r2.cloudflarestorage.com")) {
                callback.invoke(
                    newExtractorLink(
                        name,
                        "Kuramadrive Direct ${size}p",
                        srcUrl,
                        referer = mainUrl,
                        quality = size,
                        type = ExtractorLinkType.VIDEO
                    )
                )
            }
        }

        // 2. Download Links (Iterate children statefully)
        val downloadSection = document.selectFirst("div#animeDownloadLink")
        var currentQuality = "Unknown"

        downloadSection?.children()?.forEach { element ->
            when (element.tagName()) {
                "h6" -> {
                    currentQuality = element.text().trim()
                }
                "a" -> {
                    val url = element.attr("href")
                    if (url.isNotBlank()) {
                        loadFixedExtractor(url, currentQuality, mainUrl, subtitleCallback, callback)
                    }
                }
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
}
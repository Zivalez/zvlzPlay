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
    override var mainUrl = "https://v17.kuramanime.ink"
    override var name = "Kuramanime"
    override val hasQuickSearch = false
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie, TvType.OVA)

    // Add User-Agent to bypass potential bot checks
    private val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36"
    private val commonHeaders = mapOf(
        "User-Agent" to userAgent,
        "Referer" to "https://v17.kuramanime.ink/",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Language" to "id-ID,id;q=0.9,en;q=0.8",
        "Sec-Ch-Ua" to "\"Google Chrome\";v=\"147\", \"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"147\"",
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

        // Check if we need to bypass Kuramanime's protection
        val initialDownloadSection = document.selectFirst("div#animeDownloadLink")
        val isProtected = initialDownloadSection?.select(".reload-error").isNotEmpty() ?: true
        
        if (isProtected) {
            var bypassed = false
            
            // Try standard bypass first
            try {
                val pageHtml = document.html()
                
                // Extract auth token from page
                val authToken = document.selectFirst("input[name=authorization]")?.attr("value")
                    ?: document.selectFirst("[data-auth-token]")?.attr("data-auth-token")
                    ?: "kJuHHkaqcBFXiGMHQf6bJw8YAyDcwGD8Ur"
                
                // Extract token file path - try multiple patterns
                var tokenFilePath: String? = null
                
                // Pattern 1: Look for quoted hex-like paths (e.g., "Ks6sqSgloPTlHMl.txt")
                val hexPattern = Regex("""['\"]([a-zA-Z0-9]{12,}\.txt)['\"]""")
                tokenFilePath = hexPattern.find(pageHtml)?.groupValues?.get(1)
                
                // Pattern 2: Look for /assets/ references
                if (tokenFilePath == null) {
                    val assetPattern = Regex("""/assets/([a-zA-Z0-9]+\.txt)""")
                    tokenFilePath = assetPattern.find(pageHtml)?.groupValues?.get(1)
                }
                
                // Pattern 3: Search in script content for token references
                if (tokenFilePath == null) {
                    val scripts = document.select("script")
                    for (script in scripts) {
                        val content = script.html()
                        val match = Regex("""['\"]([a-zA-Z0-9]{12,}\.txt)['\"]""").find(content)
                        if (match != null) {
                            tokenFilePath = match.groupValues[1]
                            break
                        }
                    }
                }
                
                // Fetch and validate token
                if (tokenFilePath != null && tokenFilePath.isNotEmpty()) {
                    val tokenUrl = "$mainUrl/assets/$tokenFilePath"
                    val tokenResponse = app.get(tokenUrl, headers = commonHeaders)
                    val token = tokenResponse.text.trim()
                    
                    // Validate token: should be alphanumeric, 5-30 chars
                    if (token.isNotEmpty() && token.length in 5..30 && token.all { it.isLetterOrDigit() }) {
                        val pageTokenKey = "Ub3BzhijicHXZdv"
                        val streamServerKey = "C2XAPerzX1BM7V9"
                        val bypassUrl = "$data?$pageTokenKey=$token&$streamServerKey=kuramadrive&page=1"
                        
                        val bypassHeaders = commonHeaders + mapOf(
                            "X-Requested-With" to "XMLHttpRequest",
                            "Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8"
                        )

                        val bypassResponse = app.post(bypassUrl, headers = bypassHeaders, data = mapOf("authorization" to authToken))
                        
                        if (bypassResponse.code == 200) {
                            document = bypassResponse.document
                            bypassed = true
                        }
                    }
                }
            } catch (e: Exception) {
                // Continue to fallback
            }

            // Fallback: Use WebView if standard bypass failed
            if (!bypassed) {
                try {
                    val resolver = WebViewResolver(
                        Regex("""episode/\d+"""),
                        userAgent = userAgent
                    )
                    val webViewResponse = app.get(data, headers = commonHeaders, interceptor = resolver)
                    if (webViewResponse.code == 200) {
                        document = webViewResponse.document
                    }
                } catch (e: Exception) {
                    // WebView also failed, continue with current document
                }
            }
        }

        // 1. Direct Stream Sources (video#player) - Priority source
        val videoPlayer = document.selectFirst("video#player")
        if (videoPlayer != null) {
            videoPlayer.select("source").forEach { source ->
                val sourceUrl = source.attr("src")
                val sourceId = source.attr("id")
                
                if (sourceUrl.isNotEmpty() && sourceUrl.startsWith("http")) {
                    val quality = when {
                        sourceId.contains("720") -> Qualities.P720.value
                        sourceId.contains("480") -> Qualities.P480.value
                        sourceId.contains("360") -> Qualities.P360.value
                        else -> Qualities.Unknown.value
                    }
                    
                    val qualityLabel = when {
                        sourceId.contains("720") -> "720p"
                        sourceId.contains("480") -> "480p"
                        sourceId.contains("360") -> "360p"
                        else -> "Unknown"
                    }
                    
                    callback.invoke(
                        newExtractorLink(
                            name,
                            "Kuramanime Direct $qualityLabel",
                            sourceUrl,
                            ExtractorLinkType.VIDEO
                        ) {
                            this.referer = mainUrl
                            this.quality = quality
                            this.headers = mapOf("User-Agent" to userAgent)
                        }
                    )
                }
            }
        }

        // 2. Download Links - Secondary sources
        val downloadSection = document.selectFirst("div#animeDownloadLink")
        if (downloadSection != null) {
            var currentQuality = "Unknown"
            var qualityIndex = 0
            
            downloadSection.children()?.forEach { element ->
                when (element.tagName()) {
                    "h6" -> {
                        currentQuality = element.text().trim()
                        qualityIndex++
                    }
                    "a" -> {
                        val url = element.attr("href")
                        if (url.isNotBlank() && url.startsWith("http")) {
                            loadFixedExtractor(url, currentQuality, mainUrl, subtitleCallback, callback)
                        }
                    }
                }
            }
        }

        return true
    }

    private suspend fun loadFixedExtractor(
        url: String,
        qualityLabel: String,
        referer: String? = null,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        try {
            loadExtractor(url, referer, subtitleCallback) { link ->
                runBlocking {
                    callback.invoke(
                        newExtractorLink(
                            link.name,
                            link.name,
                            link.url,
                            link.type
                        ) {
                            this.referer = link.referer
                            this.quality = qualityLabel.fixQuality()
                            this.headers = link.headers
                            this.extractorData = link.extractorData
                        }
                    )
                }
            }
        } catch (e: Exception) {
            // Silently skip if extractor fails
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

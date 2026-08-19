package com.loklok

import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.amap
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.nicehttp.RequestBodyTypes
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.security.MessageDigest

class Loklok : MainAPI() {
    override var name = "Loklok"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val instantLinkLoading = true
    override val hasQuickSearch = true
    override val usesWebView = true
    override var lang = "en"
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.AsianDrama,
    )

    companion object {
        private const val TAG = "Loklok"
        private val h5ApiUrl = "https://h5-api.loklok.site/cms/web"
        private val h5ApiUrlV2 = "https://h5-api.loklok.site/cms/v2/h5"
        private const val H5_SITE = "https://h5.loklok.site"
        private const val IMAGE_PROXY = "https://images.weserv.nl"
        private const val BROWSER_UA = "Mozilla/5.0 (Linux; Android 14; Pixel 8 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.6422.165 Mobile Safari/537.36"

        private val deviceId = generateDeviceId()

        private fun generateDeviceId(length: Int = 16): String {
            val chars = ('a'..'f') + ('0'..'9')
            return (1..length).map { chars.random() }.joinToString("")
        }

        private fun generateSign(timestamp: Long): String {
            val raw = "FrontEnd${timestamp}5I7MD1O9GI"
            return md5(raw)
        }

        private fun md5(input: String): String {
            val md = MessageDigest.getInstance("MD5")
            return md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
        }

        private fun buildApiHeaders(): String {
            val timestamp = System.currentTimeMillis()
            val sign = generateSign(timestamp)
            return """
                "lang": "en",
                "versioncode": "11132",
                "clienttype": "web_h5",
                "platform": "web",
                "deviceid": "$deviceId",
                "timestamp": "$timestamp",
                "sign": "$sign"
            """.trimIndent()
        }
    }

    private fun encode(input: String): String =
        java.net.URLEncoder.encode(input, "utf-8").replace("+", "%20")

    private fun MediaItem.toSearchResponse(): SearchResponse? {
        return newMovieSearchResponse(
            title ?: name ?: return null,
            UrlData(id, category ?: domainType).toJson(),
            TvType.Movie,
        ) {
            this.posterUrl = (imageUrl ?: coverVerticalUrl)?.let {
                "$IMAGE_PROXY/?url=${encode(it)}&w=175&h=246&fit=cover&output=webp"
            }
        }
    }



    private suspend fun apiGet(path: String): String {
        val url = "$h5ApiUrl/$path"
        Log.d(TAG, "apiGet: $url")

        val timestamp = System.currentTimeMillis()
        val sign = generateSign(timestamp)

        val headers = mapOf(
            "lang" to "en",
            "versioncode" to "11132",
            "clienttype" to "web_h5",
            "platform" to "web",
            "deviceid" to deviceId,
            "timestamp" to timestamp.toString(),
            "sign" to sign,
            "User-Agent" to BROWSER_UA,
            "Accept" to "application/json, text/plain, */*",
            "Accept-Language" to "en-US,en;q=0.9",
            "Origin" to H5_SITE,
            "Referer" to "$H5_SITE/",
            "Sec-Ch-Ua" to "\"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"",
            "Sec-Ch-Ua-Mobile" to "?1",
            "Sec-Ch-Ua-Platform" to "\"Android\"",
            "Sec-Fetch-Dest" to "empty",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Site" to "same-site",
        )

        val res = app.get(url, headers = headers)
        Log.d(TAG, "apiGet response code: ${res.code}")

        if (res.code == 200) {
            return res.text
        }

        Log.d(TAG, "OkHttp blocked (${res.code}), trying WebView fetch")

        val wvResult = webViewApiCall(url, headers)
        if (wvResult != null) {
            Log.d(TAG, "WebView fetch success, len=${wvResult.length}")
            return wvResult
        }

        throw Exception("API call failed for $path")
    }

    private suspend fun apiPost(path: String, bodyJson: String, useV2: Boolean = false): String {
        val base = if (useV2) h5ApiUrlV2 else h5ApiUrl
        val url = "$base/$path"
        Log.d(TAG, "apiPost: $url")

        val timestamp = System.currentTimeMillis()
        val sign = generateSign(timestamp)

        val headers = mapOf(
            "lang" to "en",
            "versioncode" to "11132",
            "clienttype" to "web_h5",
            "platform" to "web",
            "deviceid" to deviceId,
            "timestamp" to timestamp.toString(),
            "sign" to sign,
            "User-Agent" to BROWSER_UA,
            "Accept" to "application/json, text/plain, */*",
            "Accept-Language" to "en-US,en;q=0.9",
            "Content-Type" to "application/json",
            "Origin" to H5_SITE,
            "Referer" to "$H5_SITE/",
            "Sec-Ch-Ua" to "\"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"",
            "Sec-Ch-Ua-Mobile" to "?1",
            "Sec-Ch-Ua-Platform" to "\"Android\"",
            "Sec-Fetch-Dest" to "empty",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Site" to "same-site",
        )

        val body = bodyJson.toRequestBody(RequestBodyTypes.JSON.toMediaTypeOrNull())
        val res = app.post(url, requestBody = body, headers = headers)
        Log.d(TAG, "apiPost response code: ${res.code}")

        if (res.code == 200) {
            return res.text
        }

        Log.d(TAG, "OkHttp POST blocked (${res.code}), trying WebView")

        val wvResult = webViewApiCall(url, headers, "POST", bodyJson)
        if (wvResult != null) {
            Log.d(TAG, "WebView POST success, len=${wvResult.length}")
            return wvResult
        }

        throw Exception("POST API call failed for $path")
    }

    private suspend fun webViewApiCall(
        url: String,
        headers: Map<String, String>,
        method: String = "GET",
        body: String? = null
    ): String? {
        val captured = java.util.concurrent.atomic.AtomicReference<String?>(null)

        val headersJs = headers.entries.joinToString(",\n") { (k, v) ->
            "'${k}': '${v.replace("'", "\\'")}'"
        }

        val fetchOptions = if (method == "POST" && body != null) {
            """
                method: 'POST',
                headers: {$headersJs},
                body: '${body.replace("'", "\\'")}'
            """.trimIndent()
        } else {
            """
                method: 'GET',
                headers: {$headersJs}
            """.trimIndent()
        }

        val script = """
            (function() {
                try {
                    if (window.__loklokApiResult) {
                        return window.__loklokApiResult;
                    }
                    if (!window.__loklokFetching) {
                        window.__loklokFetching = true;
                        fetch('$url', { $fetchOptions })
                        .then(function(r) { return r.text(); })
                        .then(function(t) { 
                            window.__loklokApiResult = t; 
                        })
                        .catch(function(e) { 
                            window.__loklokApiResult = 'ERR:' + e.message; 
                        });
                    }
                    return null;
                } catch(e) { return 'ERR:' + e.message; }
            })()
        """.trimIndent()

        val resolver = WebViewResolver(
            interceptUrl = Regex("""__LOKLOK_WV_NEVER_MATCH__"""),
            additionalUrls = listOf(
                Regex(""".*\.js.*"""),
                Regex(""".*\.css.*"""),
                Regex(""".*\.png.*"""),
                Regex(""".*\.jpg.*"""),
                Regex(""".*event-tracking-project.*""")
            ),
            userAgent = BROWSER_UA,
            useOkhttp = false,
            script = script,
            scriptCallback = { result ->
                if (result != null && result.length > 5 && result != "null") {
                    if (result.startsWith("\"ERR:") || result.startsWith("ERR:")) {
                        Log.e(TAG, "WebView fetch error: $result")
                        captured.set(result) // Set to prevent timeout, will be handled below
                    } else {
                    val decoded = try {
                        org.json.JSONArray("[$result]").getString(0)
                    } catch (e: Exception) {
                        Log.e(TAG, "scriptCallback JSON decode failed: ${e.message}")
                        null
                    }
                    if (decoded != null && (decoded.contains("\"code\"") || decoded.contains("\"data\""))) {
                        if (captured.get() == null) {
                            Log.d(TAG, "scriptCallback: captured JSON, len=${decoded.length}")
                        }
                        captured.set(decoded)
                    }
                    }
                }
            },
            timeout = 30_000L
        )

        try {
            resolver.resolveUsingWebView(
                url = "$H5_SITE/",
                referer = H5_SITE,
                method = "GET",
                requestCallBack = { captured.get() != null }
            )
        } catch (e: Exception) {
            Log.e(TAG, "webViewApiCall exception: ${e.message}")
        }

        val finalResult = captured.get()
        if (finalResult != null && (finalResult.startsWith("\"ERR:") || finalResult.startsWith("ERR:"))) {
            Log.e(TAG, "webViewApiCall failed with: $finalResult")
            return null
        }

        return finalResult
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val home = ArrayList<HomePageList>()
        for (i in 0..6) {
            val response = runCatching {
                val json = apiGet("homePage/getHome?page=$i")
                parseJson<LoklokHomeResponse>(json)
            }.onFailure {
                Log.e(TAG, "getMainPage page=$i failed: ${it.message}")
            }.getOrNull()

            response?.data?.recommendItems.orEmpty()
                .filterNot { it.homeSectionType == "BLOCK_GROUP" }
                .filterNot { it.homeSectionType == "BANNER" }
                .mapNotNull { section ->
                    val header = section.homeSectionName ?: return@mapNotNull null
                    val media = section.media?.mapNotNull { it.toSearchResponse() }
                        .orEmpty().ifEmpty { return@mapNotNull null }
                    home.add(HomePageList(header, media))
                }
        }
        if (home.isEmpty()) throw ErrorLoadingException("Loklok API is blocked by Akamai CDN. The API works in browser but OkHttp is fingerprinted.")
        return newHomePageResponse(home)
    }

    override suspend fun quickSearch(query: String): List<SearchResponse>? = performSearch(query)

    override suspend fun search(query: String): List<SearchResponse>? = performSearch(query)

    private suspend fun performSearch(query: String): List<SearchResponse>? {
        val bodyJson = mapOf(
            "searchKeyWord" to query,
            "size" to "50",
            "sort" to "",
            "searchType" to "",
        ).toJson()

        val results = runCatching {
            val json = apiPost("search/v1/searchWithKeyWord", bodyJson, useV2 = true)
            parseJson<LoklokSearchResponse>(json)?.data?.searchResults
        }.getOrNull() ?: runCatching {
            val json = apiPost("search/searchWithKeyWord", bodyJson, useV2 = false)
            parseJson<LoklokSearchResponse>(json)?.data?.searchResults
        }.getOrNull()

        return results?.mapNotNull { it.toSearchResponse() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val data = parseJson<UrlData>(url)

        val res = runCatching {
            val json = apiGet("movieDrama/get?id=${data.id}&category=${data.category}")
            parseJson<DetailResponse>(json)?.data
        }.onFailure {
            Log.e(TAG, "load failed: ${it.message}")
        }.getOrNull() ?: throw ErrorLoadingException("Failed to load details")

        val actors = res.starList?.mapNotNull {
            Actor(it.localName ?: return@mapNotNull null, it.image)
        }

        val episodes = res.episodeVo?.map { ep ->
            val definitions = ep.definitionList?.map {
                DefinitionRef(it.code, it.description)
            }
            val subtitles = ep.subtitlingList?.map {
                SubtitleRef(it.languageAbbr, it.language, it.subtitlingUrl)
            }
            newEpisode(
                EpisodeData(
                    data.id.toString(),
                    data.category,
                    ep.id,
                    definitions,
                    subtitles
                ).toJson()
            ) {
                this.episode = ep.seriesNo
            }
        } ?: throw ErrorLoadingException("No episodes found")

        val recommendations = res.likeList?.mapNotNull { it.toSearchResponse() }

        val type = when {
            res.areaList?.firstOrNull()?.id == 44 && res.tagNameList?.contains("Anime") == true -> TvType.Anime
            data.category == 0 -> TvType.Movie
            else -> TvType.TvSeries
        }

        return newTvSeriesLoadResponse(
            res.name ?: return null,
            url,
            if (data.category == 0) TvType.Movie else type,
            episodes
        ) {
            this.posterUrl = res.coverVerticalUrl
            this.backgroundPosterUrl = res.coverHorizontalUrl
            this.year = res.year
            this.plot = res.introduction
            this.tags = res.tagNameList
            this.score = Score.from10(res.score)
            addActors(actors)
            this.recommendations = recommendations
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val res = parseJson<EpisodeData>(data)

        res.definitionList?.amap { video ->
            val json = runCatching {
                val text = apiGet("media/previewInfo?category=${res.category}&contentId=${res.id}&episodeId=${res.epId}&definition=${video.code}")
                parseJson<PreviewResponse>(text)?.data
            }.onFailure {
                Log.e(TAG, "loadLinks previewInfo failed: ${it.message}")
            }.getOrNull()

            val mediaUrl = json?.mediaUrl ?: return@amap null

            callback.invoke(
                newExtractorLink(
                    this.name,
                    this.name,
                    mediaUrl,
                    ExtractorLinkType.M3U8
                ) {
                    this.quality = getQualityFromDefinition(json.currentDefinition ?: video.code ?: "")
                }
            )
        }

        res.subtitlingList?.map { sub ->
            subtitleCallback.invoke(
                newSubtitleFile(
                    getLanguageName(sub.languageAbbr ?: return@map),
                    sub.subtitlingUrl ?: return@map
                )
            )
        }

        return true
    }

    private fun getQualityFromDefinition(quality: String): Int {
        return when (quality.uppercase()) {
            "GROOT_FD", "360P" -> Qualities.P360.value
            "GROOT_LD", "480P" -> Qualities.P480.value
            "GROOT_SD", "720P" -> Qualities.P720.value
            "GROOT_HD", "1080P" -> Qualities.P1080.value
            else -> Qualities.Unknown.value
        }
    }

    private fun getLanguageName(abbr: String): String {
        return when (abbr) {
            "in_ID" -> "Indonesian"
            "pt" -> "Portuguese"
            "ms" -> "Malay"
            "vi" -> "Vietnamese"
            "th" -> "Thai"
            "zh-Hans", "zh_CN" -> "Chinese (Simplified)"
            "zh-Hant", "zh_TW" -> "Chinese (Traditional)"
            "ar" -> "Arabic"
            "es" -> "Spanish"
            "fr" -> "French"
            "de" -> "German"
            "ja" -> "Japanese"
            "ko" -> "Korean"
            else -> abbr.split("_").first().let {
                SubtitleHelper.fromTwoLettersToLanguage(it) ?: it
            }
        }
    }
}

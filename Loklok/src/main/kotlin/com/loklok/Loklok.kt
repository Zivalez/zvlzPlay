package com.loklok

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.amap
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.newHomePageResponse
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
    override var lang = "en"
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.AsianDrama,
    )

    companion object {
        private const val TAG = "Loklok"
        private val mobileApiUrl = decodeReversedBase64("dg==LnQ=b2s=a2w=bG8=aS4=YXA=ZS0=aWw=b2I=LW0=Z2E=Ly8=czo=dHA=aHQ=") + "/" + base64Decode("Y21zL2FwcA==")
        private val h5ApiUrl = "https://h5-api.loklok.site/cms/web"
        private val h5ApiUrlV2 = "https://h5-api.loklok.site/cms/v2/h5"
        private const val IMAGE_PROXY = "https://images.weserv.nl"

        private val deviceId = generateDeviceId()

        private val mobileHeaders = mutableMapOf(
            "lang" to "en",
            "versioncode" to "999999999",
            "clienttype" to "ios17",
            "deviceid" to deviceId,
        )

        private fun decodeReversedBase64(api: String): String {
            return api.chunked(4).map { base64Decode(it) }.reversed().joinToString("")
        }

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

        private fun getH5Headers(): Map<String, String> {
            val timestamp = System.currentTimeMillis()
            return mapOf(
                "lang" to "en",
                "versioncode" to "32",
                "clienttype" to "H5",
                "deviceid" to deviceId,
                "timestamp" to timestamp.toString(),
                "sign" to generateSign(timestamp),
            )
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

    private suspend fun apiGet(path: String): com.lagradost.nicehttp.NiceResponse {
        val h5Url = "$h5ApiUrl/$path"
        val mobileUrl = "$mobileApiUrl/$path"

        return runCatching {
            Log.d(TAG, "Trying H5 API: $h5Url")
            val res = app.get(h5Url, headers = getH5Headers())
            Log.d(TAG, "H5 API response code: ${res.code}")
            if (res.code != 200) throw Exception("H5 API returned ${res.code}")
            res
        }.getOrElse { e ->
            Log.d(TAG, "H5 API failed: ${e.message}, trying mobile API")
            runCatching {
                val res = app.get(mobileUrl, headers = mobileHeaders)
                Log.d(TAG, "Mobile API response code: ${res.code}")
                res
            }.getOrElse { e2 ->
                Log.e(TAG, "Both APIs failed. H5: ${e.message}, Mobile: ${e2.message}")
                throw e2
            }
        }
    }

    private suspend fun apiPost(path: String, body: okhttp3.RequestBody, useV2: Boolean = false): com.lagradost.nicehttp.NiceResponse {
        val h5Base = if (useV2) h5ApiUrlV2 else h5ApiUrl
        val h5Url = "$h5Base/$path"
        val mobileUrl = "$mobileApiUrl/$path"

        return runCatching {
            Log.d(TAG, "Trying H5 POST: $h5Url")
            val res = app.post(h5Url, requestBody = body, headers = getH5Headers())
            Log.d(TAG, "H5 POST response code: ${res.code}")
            if (res.code != 200) throw Exception("H5 POST returned ${res.code}")
            res
        }.getOrElse { e ->
            Log.d(TAG, "H5 POST failed: ${e.message}, trying mobile API")
            runCatching {
                val res = app.post(mobileUrl, requestBody = body, headers = mobileHeaders)
                Log.d(TAG, "Mobile POST response code: ${res.code}")
                res
            }.getOrElse { e2 ->
                Log.e(TAG, "Both POST APIs failed. H5: ${e.message}, Mobile: ${e2.message}")
                throw e2
            }
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val home = ArrayList<HomePageList>()
        for (i in 0..6) {
            val response = runCatching {
                apiGet("homePage/getHome?page=$i").parsedSafe<LoklokHomeResponse>()
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
        if (home.isEmpty()) throw ErrorLoadingException("Loklok might be geoblocked in your region. Try using a VPN.")
        return newHomePageResponse(home)
    }

    override suspend fun quickSearch(query: String): List<SearchResponse>? = performSearch(query)

    override suspend fun search(query: String): List<SearchResponse>? = performSearch(query)

    private suspend fun performSearch(query: String): List<SearchResponse>? {
        val body = mapOf(
            "searchKeyWord" to query,
            "size" to "50",
            "sort" to "",
            "searchType" to "",
        ).toJson().toRequestBody(RequestBodyTypes.JSON.toMediaTypeOrNull())

        val results = runCatching {
            apiPost("search/v1/searchWithKeyWord", body, useV2 = true)
                .parsedSafe<LoklokSearchResponse>()?.data?.searchResults
        }.onFailure {
            Log.e(TAG, "search failed: ${it.message}")
        }.getOrNull()

        return results?.mapNotNull { it.toSearchResponse() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val data = parseJson<UrlData>(url)

        val res = runCatching {
            apiGet("movieDrama/get?id=${data.id}&category=${data.category}")
                .parsedSafe<DetailResponse>()?.data
        }.onFailure {
            Log.e(TAG, "load failed: ${it.message}")
        }.getOrNull() ?: throw ErrorLoadingException("Failed to load details. Loklok might be geoblocked.")

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
                apiGet("media/previewInfo?category=${res.category}&contentId=${res.id}&episodeId=${res.epId}&definition=${video.code}")
                    .parsedSafe<PreviewResponse>()?.data
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
                SubtitleFile(
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

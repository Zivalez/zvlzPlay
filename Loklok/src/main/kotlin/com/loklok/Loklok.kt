package com.loklok

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
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

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val home = ArrayList<HomePageList>()
        for (i in 0..6) {
            val response = runCatching {
                app.get("$mobileApiUrl/homePage/getHome?page=$i", headers = mobileHeaders)
                    .parsedSafe<LoklokHomeResponse>()
            }.getOrNull() ?: runCatching {
                app.get("$h5ApiUrl/homePage/getHome?page=$i", headers = getH5Headers())
                    .parsedSafe<LoklokHomeResponse>()
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
        return HomePageResponse(home)
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
            app.post(
                "$mobileApiUrl/search/v1/searchWithKeyWord",
                requestBody = body,
                headers = mobileHeaders
            ).parsedSafe<LoklokSearchResponse>()?.data?.searchResults
        }.getOrNull() ?: runCatching {
            app.post(
                "$h5ApiUrlV2/search/searchWithKeyWord",
                requestBody = body,
                headers = getH5Headers()
            ).parsedSafe<LoklokSearchResponse>()?.data?.searchResults
        }.getOrNull()

        return results?.mapNotNull { it.toSearchResponse() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val data = parseJson<UrlData>(url)

        val res = runCatching {
            app.get(
                "$mobileApiUrl/movieDrama/get?id=${data.id}&category=${data.category}",
                headers = mobileHeaders
            ).parsedSafe<DetailResponse>()?.data
        }.getOrNull() ?: runCatching {
            app.get(
                "$h5ApiUrl/movieDrama/get?id=${data.id}&category=${data.category}",
                headers = getH5Headers()
            ).parsedSafe<DetailResponse>()?.data
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
            Episode(
                data = EpisodeData(
                    data.id.toString(),
                    data.category,
                    ep.id,
                    definitions,
                    subtitles
                ).toJson(),
                episode = ep.seriesNo
            )
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
            this.rating = res.score.toRatingInt()
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

        res.definitionList?.apmap { video ->
            val json = runCatching {
                app.get(
                    "$mobileApiUrl/media/previewInfo?category=${res.category}&contentId=${res.id}&episodeId=${res.epId}&definition=${video.code}",
                    headers = mobileHeaders,
                ).parsedSafe<PreviewResponse>()?.data
            }.getOrNull() ?: runCatching {
                val postBody = mapOf(
                    "category" to (res.category ?: 0),
                    "contentId" to (res.id ?: ""),
                    "episodeId" to (res.epId ?: 0),
                    "definition" to (video.code ?: "")
                ).toJson().toRequestBody(RequestBodyTypes.JSON.toMediaTypeOrNull())
                app.post(
                    "$h5ApiUrl/h5/movieDrama/previewInfo",
                    requestBody = postBody,
                    headers = getH5Headers(),
                ).parsedSafe<PreviewResponse>()?.data
            }.getOrNull()

            val mediaUrl = json?.mediaUrl ?: return@apmap null

            callback.invoke(
                ExtractorLink(
                    this.name,
                    this.name,
                    mediaUrl,
                    "",
                    getQualityFromDefinition(json.currentDefinition ?: video.code ?: ""),
                    isM3u8 = true,
                )
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

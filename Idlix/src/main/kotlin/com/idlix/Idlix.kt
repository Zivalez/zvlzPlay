package com.idlix

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.LoadResponse.Companion.addImdbId
import com.lagradost.cloudstream3.LoadResponse.Companion.addTMDbId
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.Normalizer

class Idlix : MainAPI() {
    companion object {
        private const val TAG = "Idlix"
        private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    }

    override var mainUrl = base64Decode("aHR0cHM6Ly96Mi5pZGxpeGt1LmNvbQ==")
    override var name = "Idlix"
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.AsianDrama
    )

    override val mainPage = mainPageOf(
        "$mainUrl/api/movies?page=%d&limit=36&sort=createdAt" to "Movie Terbaru",
        "$mainUrl/api/series?page=%d&limit=36&sort=createdAt" to "TV Series Terbaru",
        "$mainUrl/api/browse?page=%d&limit=36&sort=latest&network=prime-video" to "Amazon Prime",
        "$mainUrl/api/browse?page=%d&limit=36&sort=latest&network=apple-tv-plus" to "Apple TV+",
        "$mainUrl/api/browse?page=%d&limit=36&sort=latest&network=disney-plus" to "Disney+",
        "$mainUrl/api/browse?page=%d&limit=36&sort=latest&network=hbo" to "HBO",
        "$mainUrl/api/browse?page=%d&limit=36&sort=latest&network=netflix" to "Netflix",
    )

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        val rawData = request.data
        val requestUrl = if (rawData.contains("%d")) rawData.format(page) else rawData

        val res = app.get(requestUrl, timeout = 10000L).parsedSafe<ApiResponse>()
            ?: return newHomePageResponse(request.name, emptyList())
        val home = res.data.mapNotNull { item ->
            val title = item.title ?: return@mapNotNull null
            val poster = item.posterPath?.let { "https://image.tmdb.org/t/p/w342$it" }
            if (item.contentType == "movie") {
                val movieUrl = "$mainUrl/api/movies/${item.slug}"
                newMovieSearchResponse(title, movieUrl, TvType.Movie) {
                    this.posterUrl = poster
                    this.year = item.releaseDate?.substringBefore("-")?.toIntOrNull()
                    this.quality = getSearchQuality(item.quality)
                    this.score = Score.from10(item.voteAverage?.toString())
                }
            } else {
                val seriesUrl = "$mainUrl/api/series/${item.slug}"
                newTvSeriesSearchResponse(title, seriesUrl, TvType.TvSeries) {
                    this.posterUrl = poster
                    this.year = item.releaseDate?.substringBefore("-")?.toIntOrNull()
                    this.score = Score.from10(item.voteAverage?.toString())
                    this.quality = getSearchQuality(item.quality)
                }
            }
        }

        return newHomePageResponse(request.name, home)
    }

    override suspend fun quickSearch(query: String): List<SearchResponse>? = search(query, 1)?.items

    override suspend fun search(query: String, page: Int): SearchResponseList? {
        val url = "$mainUrl/api/search?q=$query&page=$page&limit=8"
        val res = app.get(url).parsedSafe<SearchApiResponse>() ?: return null
        val items = res.results
        val results = items.mapNotNull { item ->
            val title = item.title ?: return@mapNotNull null
            val poster = item.posterPath?.let { "https://image.tmdb.org/t/p/w342$it" }
            val year = (item.releaseDate ?: item.firstAirDate)?.substringBefore("-")?.toIntOrNull()

            val link = when (item.contentType) {
                "movie" -> "$mainUrl/api/movies/${item.slug}"
                "tv_series", "series" -> "$mainUrl/api/series/${item.slug}"
                else -> return@mapNotNull null
            }

            val rating = item.voteAverage?.toString()

            if (item.contentType == "movie") {
                newMovieSearchResponse(title, link, TvType.Movie) {
                    this.posterUrl = poster
                    this.year = year
                    this.quality = getSearchQuality(item.quality)
                    this.score = Score.from10(rating)
                }
            } else {
                newTvSeriesSearchResponse(title, link, TvType.TvSeries) {
                    this.posterUrl = poster
                    this.year = year
                    this.score = Score.from10(rating)
                }
            }
        }

        return results.toNewSearchResponseList()
    }

    override suspend fun load(url: String): LoadResponse {
        val response = app.get(url, timeout = 10000L)
        val data = response.parsedSafe<DetailResponse>()
            ?: throw ErrorLoadingException("Invalid JSON")

        val title = data.title ?: "Unknown"
        val poster = data.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
        val backdrop = data.backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }

        val year = (data.releaseDate ?: data.firstAirDate)
            ?.substringBefore("-")
            ?.toIntOrNull()

        val tags = data.genres?.mapNotNull { it.name } ?: emptyList()
        val logoUrl = data.logoPath?.let { "https://image.tmdb.org/t/p/w500$it" }
        val actors = data.cast?.map {
            Actor(it.name ?: "", it.profilePath?.let { p -> "https://image.tmdb.org/t/p/w185$p" })
        } ?: emptyList()

        val trailer = data.trailerUrl
        val rating = data.voteAverage

        val relatedUrl = if (data.seasons != null) {
            "$mainUrl/api/series/${data.slug}/related"
        } else {
            "$mainUrl/api/movies/${data.slug}/related"
        }

        val recommendations = try {
            app.get(relatedUrl, referer = mainUrl)
                .parsedSafe<ApiResponse>()?.data?.mapNotNull { item ->
                    val recTitle = item.title ?: return@mapNotNull null
                    val recPoster = item.posterPath?.let { "https://image.tmdb.org/t/p/w342$it" }

                    val link = if (item.contentType == "movie") {
                        "$mainUrl/api/movies/${item.slug}"
                    } else {
                        "$mainUrl/api/series/${item.slug}"
                    }

                    if (item.contentType == "movie") {
                        newMovieSearchResponse(recTitle, link, TvType.Movie) {
                            this.posterUrl = recPoster
                            this.year = (item.releaseDate ?: item.firstAirDate)
                                ?.substringBefore("-")
                                ?.toIntOrNull()
                        }
                    } else {
                        newTvSeriesSearchResponse(recTitle, link, TvType.TvSeries) {
                            this.posterUrl = recPoster
                            this.year = (item.releaseDate ?: item.firstAirDate)
                                ?.substringBefore("-")
                                ?.toIntOrNull()
                        }
                    }
                } ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }

        return if (data.seasons != null) {
            val episodes = mutableListOf<Episode>()
            val primarySeason = data.defaultSeason ?: data.firstSeason

            primarySeason?.episodes?.forEach { ep ->
                episodes.add(
                    newEpisode(
                        LoadData(
                            id = ep.id ?: return@forEach,
                            type = "episode",
                            seriesSlug = data.slug,
                            seasonNum = primarySeason.seasonNumber,
                            episodeNum = ep.episodeNumber
                        ).toJson()
                    ) {
                        this.name = ep.name
                        this.season = primarySeason.seasonNumber
                        this.episode = ep.episodeNumber
                        this.description = ep.overview
                        this.runTime = ep.runtime
                        this.score = Score.from10(ep.voteAverage?.toString())
                        addDate(ep.airDate)
                        this.posterUrl = ep.stillPath?.let { "https://image.tmdb.org/t/p/w300$it" }
                    }
                )
            }

            data.seasons.forEach { season ->
                val seasonNum = season.seasonNumber ?: return@forEach
                if (seasonNum == primarySeason?.seasonNumber) return@forEach
                val seasonUrl = "$mainUrl/api/series/${data.slug}/season/$seasonNum"

                val seasonData = try {
                    val res = app.get(seasonUrl, referer = mainUrl)
                    res.parsedSafe<SeasonWrapper>()?.season
                } catch (_: Exception) {
                    null
                }

                seasonData?.episodes?.forEach { ep ->
                    episodes.add(
                        newEpisode(
                            LoadData(
                                id = ep.id ?: return@forEach,
                                type = "episode",
                                seriesSlug = data.slug,
                                seasonNum = seasonNum,
                                episodeNum = ep.episodeNumber
                            ).toJson()
                        ) {
                            this.name = ep.name
                            this.season = seasonNum
                            this.episode = ep.episodeNumber
                            this.description = ep.overview
                            this.runTime = ep.runtime
                            this.score = Score.from10(ep.voteAverage?.toString())
                            addDate(ep.airDate)
                            this.posterUrl = ep.stillPath?.let { "https://image.tmdb.org/t/p/w300$it" }
                        }
                    )
                }
            }

            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.logoUrl = logoUrl
                this.year = year
                this.plot = data.overview
                this.tags = tags
                this.score = Score.from10(rating?.toString())
                addActors(actors)
                addTrailer(trailer)
                addTMDbId(data.tmdbId)
                addImdbId(data.imdbId)
                this.recommendations = recommendations
            }
        } else {
            newMovieLoadResponse(
                title,
                url,
                TvType.Movie,
                LoadData(
                    id = data.id ?: "",
                    type = "movie"
                ).toJson()
            ) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.logoUrl = logoUrl
                this.year = year
                this.plot = data.overview
                this.tags = tags
                this.score = Score.from10(rating?.toString())
                addActors(actors)
                addTrailer(trailer)
                addTMDbId(data.tmdbId)
                addImdbId(data.imdbId)
                this.recommendations = recommendations
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val parsed = runCatching { AppUtils.parseJson<LoadData>(data) }.getOrNull() ?: return false
        val kind = when (parsed.type) {
            "movie"   -> "movie"
            "episode" -> "episode"
            else      -> return false
        }

        val headers = mapOf(
            "Referer" to "$mainUrl/",
            "Origin" to mainUrl,
            "User-Agent" to USER_AGENT,
            "Accept" to "*/*",
            "Content-Type" to "application/json",
        )

        val playInfoUrl = "$mainUrl/api/watch/play-info/$kind/${parsed.id}"
        val playInfoBody = """{"targetId":"${parsed.id}","targetType":"$kind"}""".toRequestBody("application/json".toMediaType())
        var playInfoRes = app.get(
            playInfoUrl,
            headers = headers
        ).parsedSafe<Res>()

        if (playInfoRes == null) {
            playInfoRes = app.post(
                playInfoUrl,
                requestBody = playInfoBody,
                headers = headers
            ).parsedSafe<Res>()
        }

        if (playInfoRes == null) {
            playInfoRes = app.post(
                "$mainUrl/api/gate",
                requestBody = playInfoBody,
                headers = headers
            ).parsedSafe<Res>()
        }

        val finalPlayInfo = playInfoRes ?: return false

        val delayTime = maxOf(0L, finalPlayInfo.unlockAt - finalPlayInfo.serverNow) + 1500L
        if (delayTime > 0) {
            kotlinx.coroutines.delay(delayTime)
        }

        val claimBody = """{"gateToken":"${finalPlayInfo.gateToken}"}""".toRequestBody("application/json".toMediaType())
        var claimData: RedeemRes? = null
        for (i in 0 until 6) {
            var res = app.post(
                "$mainUrl/api/watch/session/claim",
                requestBody = claimBody,
                headers = headers
            ).parsedSafe<RedeemRes>()

            if (res == null || res.claim.isNullOrBlank()) {
                res = app.post(
                    "$mainUrl/api/session/claim",
                    requestBody = claimBody,
                    headers = headers
                ).parsedSafe<RedeemRes>()
            }

            if (res != null && !res.claim.isNullOrBlank()) {
                claimData = res
                break
            }

            if (res?.kind == "pending") {
                val remaining = res.remainingMs ?: 1500L
                kotlinx.coroutines.delay(maxOf(1000L, remaining + 500L))
            } else {
                kotlinx.coroutines.delay(1000L)
            }
        }

        val finalClaimData = claimData ?: return false
        val rawRedeemUrl = finalClaimData.redeemUrl ?: "$mainUrl/api/watch/session/redeem"
        val redeemUrl = if (rawRedeemUrl.startsWith("http")) rawRedeemUrl else if (rawRedeemUrl.startsWith("//")) "https:$rawRedeemUrl" else "$mainUrl/${rawRedeemUrl.trimStart('/')}"
        val claimToken = finalClaimData.claim ?: return false

        val redeemBody = """{"claim":"$claimToken"}""".toRequestBody("application/json".toMediaType())
        var iframeRes = app.post(
            redeemUrl,
            requestBody = redeemBody,
            headers = headers
        ).parsedSafe<Iframe>()

        if (iframeRes == null) {
            iframeRes = app.post(
                "$mainUrl/api/watch/session/redeem",
                requestBody = redeemBody,
                headers = headers
            ).parsedSafe<Iframe>()
        }

        if (iframeRes == null) {
            iframeRes = app.post(
                "$mainUrl/api/session/redeem",
                requestBody = redeemBody,
                headers = headers
            ).parsedSafe<Iframe>()
        }

        val finalIframe = iframeRes ?: return false

        val rawStreamUrl = finalIframe.url ?: finalIframe.streamUrl ?: finalIframe.file ?: finalIframe.src
        val streamUrl = if (rawStreamUrl.isNullOrBlank()) null else if (rawStreamUrl.startsWith("http")) rawStreamUrl else if (rawStreamUrl.startsWith("//")) "https:$rawStreamUrl" else "$mainUrl/${rawStreamUrl.trimStart('/')}"
        if (!streamUrl.isNullOrBlank()) {
            callback.invoke(
                newExtractorLink(
                    name,
                    name,
                    streamUrl,
                    ExtractorLinkType.M3U8
                ) {
                    this.referer = "$mainUrl/"
                }
            )
        }

        finalIframe.subtitles?.forEach { sub ->
            if (sub.path.isNotBlank()) {
                val subUrl = if (sub.path.startsWith("http")) sub.path else if (sub.path.startsWith("//")) "https:${sub.path}" else "$mainUrl/${sub.path.trimStart('/')}"
                subtitleCallback.invoke(
                    newSubtitleFile(sub.label, subUrl)
                )
            }
        }

        return true
    }
}

fun getSearchQuality(check: String?): SearchQuality? {
    val s = check ?: return null
    val u = Normalizer.normalize(s, Normalizer.Form.NFKC).lowercase()
    val patterns = listOf(
        Regex("\\b(4k|ds4k|uhd|2160p)\\b", RegexOption.IGNORE_CASE) to SearchQuality.FourK,
        Regex("\\b(hdts|hdcam|hdtc)\\b", RegexOption.IGNORE_CASE) to SearchQuality.HdCam,
        Regex("\\b(camrip|cam[- ]?rip)\\b", RegexOption.IGNORE_CASE) to SearchQuality.CamRip,
        Regex("\\b(cam)\\b", RegexOption.IGNORE_CASE) to SearchQuality.Cam,
        Regex("\\b(web[- ]?dl|webrip|webdl)\\b", RegexOption.IGNORE_CASE) to SearchQuality.WebRip,
        Regex("\\b(bluray|bdrip|blu[- ]?ray)\\b", RegexOption.IGNORE_CASE) to SearchQuality.BlueRay,
        Regex("\\b(1440p|qhd)\\b", RegexOption.IGNORE_CASE) to SearchQuality.BlueRay,
        Regex("\\b(1080p|fullhd)\\b", RegexOption.IGNORE_CASE) to SearchQuality.HD,
        Regex("\\b(720p)\\b", RegexOption.IGNORE_CASE) to SearchQuality.SD,
        Regex("\\b(hdrip|hdtv)\\b", RegexOption.IGNORE_CASE) to SearchQuality.HD,
        Regex("\\b(dvd)\\b", RegexOption.IGNORE_CASE) to SearchQuality.DVD,
        Regex("\\b(hq)\\b", RegexOption.IGNORE_CASE) to SearchQuality.HQ,
        Regex("\\b(rip)\\b", RegexOption.IGNORE_CASE) to SearchQuality.CamRip
    )

    for ((regex, quality) in patterns) if (regex.containsMatchIn(u)) return quality
    return null
}

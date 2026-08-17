package com.idlix

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiResponse(
    val data: List<ApiItem> = emptyList(),
    val pagination: Pagination? = null,
    val meta: Meta? = null
)

data class ApiItem(
    val id: String? = null,
    val title: String? = null,
    val slug: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val firstAirDate: String? = null,
    val voteAverage: Any? = null,
    val viewCount: Any? = null,
    val quality: String? = null,
    val country: String? = null,
    val runtime: Int? = null,
    val createdAt: String? = null,
    val numberOfSeasons: Int? = null,
    val numberOfEpisodes: Int? = null,
    val contentType: String? = null,
    val commentCount: Int? = null,
    val originalLanguage: String? = null,
    val popularity: Any? = null,
    val popularityScore: Any? = null,
    val genres: List<APIGenre>? = null,
    val hasVideo: Boolean? = null,
    val isPublished: Boolean? = null
)

data class APIGenre(
    val id: String? = null,
    val name: String? = null,
    val slug: String? = null
)

data class Pagination(
    val page: Int? = null,
    val limit: Int? = null,
    val total: Int? = null,
    val totalPages: Int? = null
)

data class Meta(
    val genre: String? = null,
    val country: String? = null,
    val year: String? = null,
    val network: String? = null,
    val sort: String? = null
)

data class DetailResponse(
    val id: String? = null,
    val title: String? = null,
    val slug: String? = null,
    val imdbId: String? = null,
    val tmdbId: String? = null,
    val overview: String? = null,
    val tagline: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val logoPath: String? = null,
    val backdrops: List<String>? = null,
    val releaseDate: String? = null,
    val firstAirDate: String? = null,
    val runtime: Int? = null,
    val voteAverage: Any? = null,
    val popularity: Any? = null,
    val popularityScore: Any? = null,
    val originalLanguage: String? = null,
    val country: String? = null,
    val status: String? = null,
    val trailerUrl: String? = null,
    val quality: String? = null,
    val director: String? = null,
    val genres: List<Genre>? = null,
    val cast: List<Cast>? = null,
    val seasons: List<Season>? = null,
    val firstSeason: Season? = null,
    val defaultSeason: Season? = null,
    val viewCount: Any? = null,
    val isPublished: Boolean? = null
)

data class Genre(
    val id: String? = null,
    val name: String? = null,
    val slug: String? = null
)

data class Cast(
    val id: String? = null,
    val name: String? = null,
    val character: String? = null,
    val profilePath: String? = null
)

data class Season(
    val id: String? = null,
    val seasonNumber: Int? = null,
    val name: String? = null,
    val posterPath: String? = null,
    val episodeCount: Int? = null,
    val episodes: List<ApiEpisode>? = null
)

data class ApiEpisode(
    val id: String? = null,
    val episodeNumber: Int? = null,
    val name: String? = null,
    val overview: String? = null,
    val stillPath: String? = null,
    val airDate: String? = null,
    val runtime: Int? = null,
    val voteAverage: Any? = null
)

data class SeasonWrapper(
    val season: Season? = null
)

data class SearchApiResponse(
    val results: List<SearchApiResult> = emptyList(),
    val total: Long? = null,
)

data class SearchApiResult(
    val id: String? = null,
    val contentType: String? = null,
    val title: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val genres: List<String>? = null,
    val originalLanguage: String? = null,
    val voteAverage: Any? = null,
    val viewCount: Any? = null,
    val popularity: Any? = null,
    val popularityScore: Any? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val slug: String? = null,
    val firstAirDate: String? = null,
    val numberOfSeasons: Long? = null,
    val releaseDate: String? = null,
    val quality: String? = null,
)

data class ChallengeResponse(
    val challenge: String,
    val signature: String,
    val difficulty: Int
)

data class LoadData(
    val id: String,
    val type: String,
    val seriesSlug: String? = null,
    val seasonNum: Int? = null,
    val episodeNum: Int? = null
)

data class Res(
    @JsonProperty("gateToken") val gateToken: String,
    @JsonProperty("serverNow") val serverNow: Long,
    @JsonProperty("unlockAt") val unlockAt: Long
)

data class RedeemRes(
    @JsonProperty("kind") val kind: String? = null,
    @JsonProperty("claim") val claim: String? = null,
    @JsonProperty("redeemUrl") val redeemUrl: String? = null,
    @JsonProperty("videoId") val videoId: String? = null,
    @JsonProperty("title") val title: String? = null,
    @JsonProperty("durationSec") val durationSec: Long? = null,
    @JsonProperty("viewerTier") val viewerTier: String? = null,
    @JsonProperty("maxHeight") val maxHeight: Long? = null,
    @JsonProperty("serverNow") val serverNow: Long? = null,
    @JsonProperty("unlockAt") val unlockAt: Long? = null,
    @JsonProperty("remainingMs") val remainingMs: Long? = null
)

data class Iframe(
    @JsonProperty("code") val code: String? = null,
    @JsonProperty("url") val url: String? = null,
    @JsonProperty("streamUrl") val streamUrl: String? = null,
    @JsonProperty("file") val file: String? = null,
    @JsonProperty("src") val src: String? = null,
    @JsonProperty("expiresAt") val expiresAt: Long? = null,
    @JsonProperty("subtitles") val subtitles: List<Subtitle>? = emptyList(),
    @JsonProperty("videoId") val videoId: String? = null
)

data class Subtitle(
    @JsonProperty("lang") val lang: String,
    @JsonProperty("label") val label: String,
    @JsonProperty("path") val path: String
)

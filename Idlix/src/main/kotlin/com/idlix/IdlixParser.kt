package com.idlix

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
    val voteAverage: String? = null,
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
    val results: List<SearchApiResult>,
    val total: Long,
)

data class SearchApiResult(
    val id: String,
    val contentType: String,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val genres: List<String>,
    val originalLanguage: String,
    val voteAverage: Double,
    val viewCount: Long,
    val popularity: Double,
    val posterPath: String,
    val backdropPath: String,
    val slug: String,
    val firstAirDate: String?,
    val numberOfSeasons: Long?,
    val releaseDate: String?,
    val quality: String?,
)

data class ChallengeResponse(
    val challenge: String,
    val signature: String,
    val difficulty: Int
)

data class LoadData(
    val id: String,
    val type: String // "movie" or "episode"
)

// NEW: Token redemption flow data classes
data class PlayInfoResponse(
    val kind: String? = null,
    val claim: String? = null,
    val redeemUrl: String? = null,
    val videoId: String? = null,
    val title: String? = null,
    val durationSec: Int? = null
)

data class RedeemResponse(
    val code: String? = null,
    val url: String? = null,
    val expiresAt: Long? = null,
    val videoId: String? = null,
    val subtitles: List<SubtitleInfo>? = null
)

data class SubtitleInfo(
    val lang: String? = null,
    val url: String? = null,
    val label: String? = null
)

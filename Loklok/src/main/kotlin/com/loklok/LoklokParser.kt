package com.loklok

import com.fasterxml.jackson.annotation.JsonProperty

data class LoklokHomeResponse(
    @JsonProperty("data") val data: HomeData? = null,
)

data class HomeData(
    @JsonProperty("recommendItems") val recommendItems: ArrayList<HomeSection>? = arrayListOf(),
)

data class HomeSection(
    @JsonProperty("homeSectionName") val homeSectionName: String? = null,
    @JsonProperty("homeSectionType") val homeSectionType: String? = null,
    @JsonProperty("recommendContentVOList") val media: ArrayList<MediaItem>? = arrayListOf(),
)

data class MediaItem(
    @JsonProperty("id") val id: Any? = null,
    @JsonProperty("category") val category: Int? = null,
    @JsonProperty("domainType") val domainType: Int? = null,
    @JsonProperty("imageUrl") val imageUrl: String? = null,
    @JsonProperty("coverVerticalUrl") val coverVerticalUrl: String? = null,
    @JsonProperty("coverHorizontalUrl") val coverHorizontalUrl: String? = null,
    @JsonProperty("title") val title: String? = null,
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("jumpAddress") val jumpAddress: String? = null,
    @JsonProperty("score") val score: String? = null,
    @JsonProperty("year") val year: Int? = null,
)

data class LoklokSearchResponse(
    @JsonProperty("data") val data: SearchData? = null,
)

data class SearchData(
    @JsonProperty("searchResults") val searchResults: ArrayList<MediaItem>? = arrayListOf(),
)

data class DetailResponse(
    @JsonProperty("data") val data: MediaDetail? = null,
)

data class MediaDetail(
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("introduction") val introduction: String? = null,
    @JsonProperty("year") val year: Int? = null,
    @JsonProperty("category") val category: String? = null,
    @JsonProperty("coverVerticalUrl") val coverVerticalUrl: String? = null,
    @JsonProperty("coverHorizontalUrl") val coverHorizontalUrl: String? = null,
    @JsonProperty("score") val score: String? = null,
    @JsonProperty("starList") val starList: ArrayList<StarItem>? = arrayListOf(),
    @JsonProperty("areaList") val areaList: ArrayList<AreaItem>? = arrayListOf(),
    @JsonProperty("episodeVo") val episodeVo: ArrayList<EpisodeItem>? = arrayListOf(),
    @JsonProperty("likeList") val likeList: ArrayList<MediaItem>? = arrayListOf(),
    @JsonProperty("tagNameList") val tagNameList: ArrayList<String>? = arrayListOf(),
    @JsonProperty("episodeCount") val episodeCount: String? = null,
)

data class StarItem(
    @JsonProperty("image") val image: String? = null,
    @JsonProperty("localName") val localName: String? = null,
)

data class AreaItem(
    @JsonProperty("id") val id: Int? = null,
    @JsonProperty("name") val name: String? = null,
)

data class EpisodeItem(
    @JsonProperty("id") val id: Int? = null,
    @JsonProperty("seriesNo") val seriesNo: Int? = null,
    @JsonProperty("definitionList") val definitionList: ArrayList<DefinitionItem>? = arrayListOf(),
    @JsonProperty("subtitlingList") val subtitlingList: ArrayList<SubtitleItem>? = arrayListOf(),
)

data class DefinitionItem(
    @JsonProperty("code") val code: String? = null,
    @JsonProperty("description") val description: String? = null,
)

data class SubtitleItem(
    @JsonProperty("languageAbbr") val languageAbbr: String? = null,
    @JsonProperty("language") val language: String? = null,
    @JsonProperty("subtitlingUrl") val subtitlingUrl: String? = null,
)

data class PreviewResponse(
    @JsonProperty("data") val data: PreviewData? = null,
)

data class PreviewData(
    @JsonProperty("mediaUrl") val mediaUrl: String? = null,
    @JsonProperty("currentDefinition") val currentDefinition: String? = null,
    @JsonProperty("totalDuration") val totalDuration: Long? = null,
)

data class UrlData(
    val id: Any? = null,
    val category: Int? = null,
)

data class EpisodeData(
    val id: String? = null,
    val category: Int? = null,
    val epId: Int? = null,
    val definitionList: List<DefinitionRef>? = arrayListOf(),
    val subtitlingList: List<SubtitleRef>? = arrayListOf(),
)

data class DefinitionRef(
    val code: String? = null,
    val description: String? = null,
)

data class SubtitleRef(
    val languageAbbr: String? = null,
    val language: String? = null,
    val subtitlingUrl: String? = null,
)

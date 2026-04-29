package com.iptv

import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LiveSearchResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newLiveSearchResponse
import com.lagradost.cloudstream3.newLiveStreamLoadResponse
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink

// Data classes for iptv-org API responses
data class Channel(
    val id: String? = null,
    val name: String? = null,
    val country: String? = null,
    val logo: String? = null,
    val categories: List<String>? = null
)

data class Stream(
    val channel: String? = null,
    val url: String? = null,
    val quality: String? = null,
    val referrer: String? = null,
    val user_agent: String? = null
)

class IPTVProvider : MainAPI() {
    override var mainUrl = "https://iptv-org.github.io"
    override var name = "IPTV Indonesia"
    override val supportedTypes = setOf(TvType.Live)
    override var lang = "id"
    override val hasMainPage = true

    private val indonesiaChannels = "indonesia"
    private val allChannels = "all"

    override val mainPage = mainPageOf(
        "$mainUrl/api/channels.json?country=ID" to indonesiaChannels,
        "$mainUrl/api/channels.json" to allChannels
    )
    private val isHorizontal = true

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val apiUrl = request.data
        val channels = app.get(apiUrl).parsedSafe<List<Channel>>() ?: emptyList()
        val filteredChannels = channels.filter { it.country == "ID" }
        
        val searchResponses = filteredChannels.mapNotNull { channel ->
            if (!channel.id.isNullOrEmpty() && !channel.name.isNullOrEmpty()) {
                newLiveSearchResponse(
                    channel.name!!,
                    channel.id!!,
                    TvType.Live,
                    fix = false
                ) { posterUrl = channel.logo ?: "" }
            } else null
        }
        
        return newHomePageResponse(
            listOf(
                HomePageList(
                    request.name,
                    searchResponses,
                    isHorizontalImages = isHorizontal
                )
            ),
            hasNext = false
        )
    }

    override suspend fun load(url: String): LoadResponse {
        val channelId = url
        val apiUrl = "$mainUrl/api/channels.json"
        val channels = app.get(apiUrl).parsedSafe<List<Channel>>() ?: emptyList()
        
        val channel = channels.find { it.id == channelId }
        
        if (channel == null) {
            throw RuntimeException("Channel not found: $channelId")
        }
        
        val name = channel.name ?: "Unknown Channel"
        val logo = channel.logo ?: ""
        val country = channel.country ?: ""
        val categories = channel.categories?.joinToString(", ") ?: ""
        
        val tags = listOfNotNull(
            if (country.isNotEmpty()) "Country: $country" else null,
            if (categories.isNotEmpty()) categories else null
        )

        // Get stream URL from iptv-org streams API
        val streamApiUrl = "$mainUrl/api/streams.json?channel=$channelId"
        
        return newLiveStreamLoadResponse(
            name,
            streamApiUrl,
            streamApiUrl
        ) {
            posterUrl = logo
            this@newLiveStreamLoadResponse.tags = tags
        }
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        val apiUrl = "$mainUrl/api/channels.json?country=ID"
        val channels = app.get(apiUrl).parsedSafe<List<Channel>>() ?: emptyList()
        
        return channels.filter { 
            !it.name.isNullOrEmpty() && it.name!!.contains(query, ignoreCase = true)
        }.mapNotNull { channel ->
            if (!channel.id.isNullOrEmpty()) {
                newLiveSearchResponse(
                    channel.name!!,
                    channel.id!!,
                    TvType.Live,
                    fix = false
                ) { posterUrl = channel.logo ?: "" }
            } else null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        return loadExtractor(data, subtitleCallback, callback)
    }

    class IPTVExtractor : ExtractorApi() {
        override val mainUrl = "https://iptv-org.github.io"
        override val name = "IPTV"
        override val requiresReferer = false

        override suspend fun getUrl(
            url: String,
            referer: String?,
            subtitleCallback: (SubtitleFile) -> Unit,
            callback: (ExtractorLink) -> Unit
        ) {
            val streams = app.get(url).parsedSafe<List<Stream>>() ?: emptyList()
            
            streams.forEach { stream ->
                if (!stream.url.isNullOrEmpty()) {
                    val quality = getQualityFromName(stream.quality ?: "SD")
                    callback.invoke(
                        newExtractorLink(
                            this.name,
                            "IPTV Stream",
                            stream.url
                        ) {
                            this.type = ExtractorLinkType.M3U8
                            this.quality = quality
                            this.referer = stream.referrer ?: ""
                            if (!stream.user_agent.isNullOrEmpty()) {
                                this.headers = mapOf("User-Agent" to stream.user_agent)
                            }
                        }
                    )
                }
            }
        }
    }
}

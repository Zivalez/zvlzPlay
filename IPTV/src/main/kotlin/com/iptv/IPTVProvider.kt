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
import com.lagradost.cloudstream3.fixUrl
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
import java.lang.RuntimeException

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
        val response = app.get(apiUrl).textLarge
        val channels = parseChannelsFromJson(response)
        
        return newHomePageResponse(
            listOf(
                HomePageList(
                    request.name,
                    channels,
                    isHorizontalImages = isHorizontal
                )
            ),
            hasNext = false
        )
    }

    private fun parseChannelsFromJson(json: String): List<LiveSearchResponse> {
        // Parse JSON response from iptv-org API
        // Format: [{"id":"ChannelID.id","name":"Channel Name","country":"ID","logo":"url",...}]
        val lines = json.split("\n")
        val channels = mutableListOf<LiveSearchResponse>()
        
        for (line in lines) {
            if (line.contains("\"id\"") && line.contains("\"country\":\"ID\"")) {
                try {
                    val id = extractJsonValue(line, "id")
                    val name = extractJsonValue(line, "name")
                    val logo = extractJsonValue(line, "logo") ?: ""
                    
                    if (!id.isNullOrEmpty() && !name.isNullOrEmpty()) {
                        channels.add(
                            newLiveSearchResponse(
                                name!!,
                                id!!,
                                TvType.Live,
                                fix = false
                            ) { posterUrl = logo }
                        )
                    }
                } catch (e: Exception) {
                    // Skip invalid entries
                }
            }
        }
        
        return channels
    }

    private fun extractJsonValue(line: String, key: String): String? {
        val pattern = "\"$key\":\"([^\"]+)\""
        val regex = Regex(pattern)
        val match = regex.find(line)
        return match?.groupValues?.get(1)
    }

    override suspend fun load(url: String): LoadResponse {
        val channelId = url
        val apiUrl = "$mainUrl/api/channels.json"
        val response = app.get(apiUrl).textLarge
        
        // Find channel data from JSON
        val channelData = findChannelData(response, channelId)
        
        if (channelData == null) {
            throw RuntimeException("Channel not found: $channelId")
        }
        
        val name = channelData["name"] ?: "Unknown Channel"
        val logo = channelData["logo"] ?: ""
        val country = channelData["country"] ?: ""
        val categories = channelData["categories"] ?: ""
        
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

    private fun findChannelData(json: String, channelId: String): Map<String, String>? {
        val lines = json.split("\n")
        for (line in lines) {
            if (line.contains("\"id\":\"$channelId\"")) {
                val data = mutableMapOf<String, String>()
                data["id"] = extractJsonValue(line, "id") ?: ""
                data["name"] = extractJsonValue(line, "name") ?: ""
                data["logo"] = extractJsonValue(line, "logo") ?: ""
                data["country"] = extractJsonValue(line, "country") ?: ""
                data["categories"] = extractJsonValue(line, "categories") ?: ""
                return data
            }
        }
        return null
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        val apiUrl = "$mainUrl/api/channels.json?country=ID"
        val response = app.get(apiUrl).text
        val channels = parseChannelsFromJson(response)
        
        // Filter by query
        return channels.filter { 
            it.name.contains(query, ignoreCase = true) 
        }.map { it as SearchResponse }
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

        data class StreamData(
            val channel: String?,
            val url: String?,
            val quality: String?,
            val referrer: String?,
            val user_agent: String?
        )

        override suspend fun getUrl(
            url: String,
            referer: String?,
            subtitleCallback: (SubtitleFile) -> Unit,
            callback: (ExtractorLink) -> Unit
        ) {
            // URL is the streams API endpoint
            val response = app.get(url).textLarge
            val streams = parseStreamsFromJson(response)
            
            streams.forEach { stream ->
                if (stream.url != null && stream.url.isNotEmpty()) {
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
                            if (stream.user_agent != null && stream.user_agent.isNotEmpty()) {
                                this.headers = mapOf("User-Agent" to stream.user_agent)
                            }
                        }
                    )
                }
            }
        }

        private fun parseStreamsFromJson(json: String): List<StreamData> {
            val lines = json.split("\n")
            val streams = mutableListOf<StreamData>()
            
            for (line in lines) {
                if (line.contains("\"url\"")) {
                    try {
                        val channel = extractJsonValue(line, "channel")
                        val url = extractJsonValue(line, "url")
                        val quality = extractJsonValue(line, "quality")
                        val referrer = extractJsonValue(line, "referrer")
                        val userAgent = extractJsonValue(line, "user_agent")
                        
                        if (url != null && url.isNotEmpty()) {
                            streams.add(StreamData(channel, url, quality, referrer, userAgent))
                        }
                    } catch (e: Exception) {
                        // Skip invalid entries
                    }
                }
            }
            
            return streams
        }

        private fun extractJsonValue(line: String, key: String): String? {
            val pattern = "\"$key\":\"?([^\"]+)\"?"
            val regex = Regex(pattern)
            val match = regex.find(line)
            return match?.groupValues?.get(1)
        }
    }
}

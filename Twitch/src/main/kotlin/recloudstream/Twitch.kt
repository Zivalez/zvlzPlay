package recloudstream

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
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.json.JSONObject
import org.jsoup.nodes.Element
import java.lang.RuntimeException
import java.net.URLEncoder

class Twitch : MainAPI() {
    override var mainUrl = "https://twitchtracker.com"
    override var name = "Twitch"
    override val supportedTypes = setOf(TvType.Live)

    override var lang = "uni"

    override val hasMainPage = true
    private val gamesName = "games"

    override val mainPage = mainPageOf(
        "$mainUrl/channels/live" to "Top global live streams",
        "$mainUrl/games" to gamesName
    )
    private val isHorizontal = true

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        return when (request.name) {
            gamesName -> newHomePageResponse(parseGames(), hasNext = false)
            else -> {
                val doc = app.get(request.data, params = mapOf("page" to page.toString())).document
                val channels = doc.select("table#channels tr").mapNotNull { element ->
                    element.toLiveSearchResponse()
                }
                newHomePageResponse(
                    listOf(
                        HomePageList(
                            request.name,
                            channels,
                            isHorizontalImages = isHorizontal
                        )
                    ),
                    hasNext = true
                )
            }
        }
    }

    private fun Element.toLiveSearchResponse(): LiveSearchResponse? {
        val anchor = this.select("a")
        val href = anchor.attr("href").trim()
        if (href.isBlank() || href == "#") return null
        val linkName = href.substringAfterLast("/").trim()
        if (linkName.isBlank()) return null
        val name = anchor.firstOrNull { it.text().isNotBlank() }?.text()?.trim() ?: linkName
        val image = this.select("img").attr("src").takeIf { it.isNotBlank() }
        return newLiveSearchResponse(
            name,
            linkName,
            TvType.Live,
            fix = false
        ) { posterUrl = image }
    }

    private suspend fun parseGames(): List<HomePageList> {
        val doc = app.get("$mainUrl/games").document
        return doc.select("div.ranked-item")
            .take(5)
            .mapNotNull { element ->
                val game = element.select("div.ri-name > a")
                val url = fixUrl(game.attr("href"))
                val name = game.text().trim()
                val searchResponses = parseGame(url).ifEmpty { return@mapNotNull null }
                HomePageList(name, searchResponses, isHorizontalImages = isHorizontal)
            }
    }

    private suspend fun parseGame(url: String): List<LiveSearchResponse> {
        val doc = app.get(url).document
        return doc.select("td.cell-slot.sm").mapNotNull { element ->
            element.toLiveSearchResponse()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val realUrl = url.substringAfterLast("/").trim()
        val doc = app.get("$mainUrl/$realUrl", referer = mainUrl).document
        val name = doc.select("div#app-title").text().trim().ifBlank { realUrl }
        val rank = doc.select("div.rank-badge > span").last()?.text()?.toIntOrNull()
        val image = doc.select("div#app-logo > img").attr("src").takeIf { it.isNotBlank() }
        val poster = doc.select("div.embed-responsive > img").attr("src").ifEmpty { image }
        val description = doc.select("div[style='word-wrap:break-word;font-size:12px;']").text().trim()
        val language = doc.select("a.label.label-soft").text().ifEmpty { null }
        val isLive = doc.select("div.live-indicator-container").isNotEmpty()

        val tags = listOfNotNull(
            isLive.let { if (it) "Live" else "Offline" },
            language,
            rank?.let { "Rank: $it" },
        )

        val twitchUrl = "https://twitch.tv/$realUrl"

        return newLiveStreamLoadResponse(
            name, twitchUrl, twitchUrl
        ) {
            plot = description
            posterUrl = image
            backgroundPosterUrl = poster
            this@newLiveStreamLoadResponse.tags = tags
        }
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        val document = runCatching {
            app.get("$mainUrl/search", params = mapOf("q" to query), referer = mainUrl).document
        }.getOrNull()
        val results = document?.select("table.tops tr")?.mapNotNull { it.toLiveSearchResponse() }
        if (!results.isNullOrEmpty()) return results

        return listOf(
            newLiveSearchResponse(
                query,
                query.lowercase().replace(" ", "_"),
                TvType.Live,
                fix = false
            )
        )
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val channel = data.substringAfter("twitch.tv/").substringBefore("/").substringBefore("?").trim()
        if (channel.isBlank()) return false

        val gqlQuery = mapOf(
            "query" to """
            {
                streamPlaybackAccessToken(
                    channelName: "$channel",
                    params: {
                        platform: "web",
                        playerBackend: "mediaplayer",
                        playerType: "site"
                    }
                ) {
                    value
                    signature
                }
            }
            """.trimIndent()
        )

        val tokenResponse = runCatching {
            app.post(
                "https://gql.twitch.tv/gql",
                headers = mapOf(
                    "Client-ID" to "kimne78kx3ncx6brgo4mv6wki5h1ko",
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
                ),
                json = gqlQuery
            ).text
        }.getOrNull() ?: return false

        val json = runCatching { JSONObject(tokenResponse) }.getOrNull() ?: return false
        val tokenData = json.optJSONObject("data")?.optJSONObject("streamPlaybackAccessToken") ?: return false
        val sig = tokenData.optString("signature")
        val token = tokenData.optString("value")
        if (sig.isBlank() || token.isBlank()) return false

        val encodedToken = URLEncoder.encode(token, "UTF-8")
        val playlistUrl = "https://usher.ttvnw.net/api/channel/hls/$channel.m3u8?sig=$sig&token=$encodedToken&player=twitchweb&p=${(100000..999999).random()}&type=any&allow_source=true&allow_audio_only=true"

        M3u8Helper.generateM3u8(
            name = this.name,
            streamUrl = playlistUrl,
            referer = "https://www.twitch.tv/"
        ).forEach(callback)

        return true
    }

    class TwitchExtractor : ExtractorApi() {
        override val mainUrl = "https://twitch.tv/"
        override val name = "Twitch"
        override val requiresReferer = false

        override suspend fun getUrl(
            url: String,
            referer: String?,
            subtitleCallback: (SubtitleFile) -> Unit,
            callback: (ExtractorLink) -> Unit
        ) {
            val channel = url.substringAfter("twitch.tv/").substringBefore("/").substringBefore("?").trim()
            if (channel.isBlank()) return

            val gqlQuery = mapOf(
                "query" to """
                {
                    streamPlaybackAccessToken(
                        channelName: "$channel",
                        params: {
                            platform: "web",
                            playerBackend: "mediaplayer",
                            playerType: "site"
                        }
                    ) {
                        value
                        signature
                    }
                }
                """.trimIndent()
            )

            val tokenResponse = runCatching {
                app.post(
                    "https://gql.twitch.tv/gql",
                    headers = mapOf(
                        "Client-ID" to "kimne78kx3ncx6brgo4mv6wki5h1ko",
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
                    ),
                    json = gqlQuery
                ).text
            }.getOrNull() ?: return

            val json = runCatching { JSONObject(tokenResponse) }.getOrNull() ?: return
            val tokenData = json.optJSONObject("data")?.optJSONObject("streamPlaybackAccessToken") ?: return
            val sig = tokenData.optString("signature")
            val token = tokenData.optString("value")
            if (sig.isBlank() || token.isBlank()) return

            val encodedToken = URLEncoder.encode(token, "UTF-8")
            val playlistUrl = "https://usher.ttvnw.net/api/channel/hls/$channel.m3u8?sig=$sig&token=$encodedToken&player=twitchweb&p=${(100000..999999).random()}&type=any&allow_source=true&allow_audio_only=true"

            M3u8Helper.generateM3u8(
                name = this.name,
                streamUrl = playlistUrl,
                referer = "https://www.twitch.tv/"
            ).forEach(callback)
        }
    }
}

package com.iptv

import com.lagradost.cloudstream3.ErrorLoadingException
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newLiveSearchResponse
import com.lagradost.cloudstream3.newLiveStreamLoadResponse
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink

private const val TAG = "IPTV"

data class IptvChannel(
    val name: String,
    val url: String,
    val logo: String? = null,
    val group: String? = null,
    val tvgId: String? = null,
    val userAgent: String? = null,
    val referrer: String? = null,
)

class IPTV : MainAPI() {
    override var mainUrl = "https://iptv-org.github.io"
    override var name = "IPTV Indonesia"
    override var lang = "id"
    override val supportedTypes = setOf(TvType.Live)
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasQuickSearch = true

    private val playlistUrl = "$mainUrl/iptv/countries/id.m3u"

    @Volatile private var cache: List<IptvChannel>? = null
    @Volatile private var cacheTime: Long = 0L
    private val cacheTtlMs: Long = 60 * 60 * 1000L

    private suspend fun loadAll(): List<IptvChannel> {
        val now = System.currentTimeMillis()
        cache?.let { if (now - cacheTime < cacheTtlMs) return it }

        val text = try {
            app.get(playlistUrl).text
        } catch (e: Exception) {
            println("$TAG: loadAll: failed to fetch playlist - ${e.message}")
            return cache ?: emptyList()
        }

        val parsed = parseM3U(text)
        if (parsed.isEmpty()) {
            println("$TAG: loadAll: parser returned 0 channels (response size=${text.length})")
            return cache ?: emptyList()
        }
        cache = parsed
        cacheTime = now
        println("$TAG: loadAll: parsed ${parsed.size} channels")
        return parsed
    }

    private val groupOrder = listOf(
        "News", "General", "Entertainment", "Movies", "Series",
        "Music", "Sports", "Kids", "Education", "Documentary",
        "Lifestyle", "Culture", "Travel", "Cooking", "Comedy",
        "Religious", "Business", "Auto", "Outdoor", "Animation",
        "Family", "Weather", "Legislative", "Shop"
    )

    private val popularNames = listOf(
        "Indosiar", "SCTV", "MNC TV", "Trans 7", "Trans TV",
        "GTV", "RCTI", "RCTI HD", "ANTV", "iNews", "tvOne",
        "Kompas TV", "NET.", "Metro TV", "RTV", "MDTV"
    )

    private fun normalizeForMatch(s: String): String =
        s.lowercase().replace(Regex("[\\s.\\-_]"), "")

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val all = loadAll()
        if (all.isEmpty()) {
            throw ErrorLoadingException("Tidak dapat memuat daftar channel IPTV. Cek koneksi internet.")
        }

        val popularRow = popularNames.mapNotNull { wanted ->
            val target = normalizeForMatch(wanted)
            all.firstOrNull { normalizeForMatch(it.name) == target }
                ?: all.firstOrNull { normalizeForMatch(it.name).startsWith(target) }
                ?: all.firstOrNull { normalizeForMatch(it.name).contains(target) }
        }.distinctBy { it.url }

        val grouped = all.groupBy { it.group?.takeIf { g -> g.isNotBlank() } ?: "Lainnya" }

        val groupRows = grouped.entries
            .sortedWith(
                compareBy(
                    { groupOrder.indexOf(it.key).let { idx -> if (idx == -1) Int.MAX_VALUE else idx } },
                    { it.key.lowercase() }
                )
            )
            .map { (group, items) ->
                HomePageList(
                    name = group,
                    list = items.map { it.toLiveSearchResponse() },
                    isHorizontalImages = true
                )
            }

        val rows = buildList {
            if (popularRow.isNotEmpty()) {
                add(
                    HomePageList(
                        name = "Popular",
                        list = popularRow.map { it.toLiveSearchResponse() },
                        isHorizontalImages = true
                    )
                )
            }
            addAll(groupRows)
        }

        return newHomePageResponse(rows, hasNext = false)
    }

    private fun IptvChannel.toLiveSearchResponse(): SearchResponse =
        newLiveSearchResponse(name, toJson(), TvType.Live, fix = false) {
            posterUrl = logo
        }

    override suspend fun search(query: String): List<SearchResponse> {
        if (query.isBlank()) return emptyList()
        return loadAll()
            .filter {
                it.name.contains(query, ignoreCase = true) ||
                    (it.tvgId?.contains(query, ignoreCase = true) == true) ||
                    (it.group?.contains(query, ignoreCase = true) == true)
            }
            .map { entry ->
                newLiveSearchResponse(
                    entry.name,
                    entry.toJson(),
                    TvType.Live,
                    fix = false
                ) { posterUrl = entry.logo }
            }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun load(url: String): LoadResponse {
        val entry = runCatching { parseJson<IptvChannel>(url) }.getOrNull()
            ?: throw ErrorLoadingException("Data channel tidak valid")

        val plotLines = listOfNotNull(
            entry.group?.let { "Kategori: $it" },
            entry.tvgId?.let { "Channel ID: $it" }
        )

        return newLiveStreamLoadResponse(entry.name, url, url) {
            posterUrl = entry.logo
            plot = plotLines.joinToString("\n").ifBlank { null }
            tags = listOfNotNull(entry.group)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val entry = runCatching { parseJson<IptvChannel>(data) }.getOrNull() ?: return false
        println("$TAG: loadLinks: name=${entry.name} url=${entry.url}")

        val type = when {
            entry.url.contains(".mpd", ignoreCase = true) -> ExtractorLinkType.DASH
            else -> ExtractorLinkType.M3U8
        }

        val extraHeaders = buildMap<String, String> {
            entry.userAgent?.let { put("User-Agent", it) }
        }

        callback.invoke(
            newExtractorLink(name, entry.name, entry.url, type) {
                this.referer = entry.referrer ?: ""
                this.quality = Qualities.Unknown.value
                if (extraHeaders.isNotEmpty()) this.headers = extraHeaders
            }
        )
        return true
    }

    private fun parseM3U(content: String): List<IptvChannel> {
        val lines = content.lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toList()

        if (lines.firstOrNull()?.startsWith("#EXTM3U", ignoreCase = true) != true) {
            println("$TAG: parseM3U: missing #EXTM3U header")
            return emptyList()
        }

        val out = mutableListOf<IptvChannel>()
        var i = 1
        while (i < lines.size) {
            val line = lines[i]
            if (!line.startsWith("#EXTINF", ignoreCase = true)) {
                i++
                continue
            }

            val attrs = parseExtInfAttrs(line)
            val title = extractExtInfTitle(line)
                .ifBlank { attrs["tvg-name"] ?: "Unknown" }

            var ua: String? = attrs["http-user-agent"]
            var ref: String? = attrs["http-referrer"]

            var j = i + 1
            while (j < lines.size && lines[j].startsWith("#")) {
                val opt = lines[j]
                if (opt.startsWith("#EXTVLCOPT", ignoreCase = true)) {
                    val payload = opt.substringAfter(":", "")
                    val eq = payload.indexOf('=')
                    if (eq != -1) {
                        val key = payload.substring(0, eq).trim().lowercase()
                        val value = payload.substring(eq + 1).trim()
                            .removeSurrounding("\"")
                        when (key) {
                            "http-user-agent" -> ua = value
                            "http-referrer" -> ref = value
                        }
                    }
                }
                j++
            }

            if (j >= lines.size) break

            val (url, urlUa, urlRef) = parseUrlLine(lines[j])
            if (url.isBlank()) {
                i = j + 1
                continue
            }

            val cleanedName = title
                .replace(Regex("""\s*\(\d+[pi]?\)""", RegexOption.IGNORE_CASE), "")
                .replace(Regex("""\s*\[[^\]]+]"""), "")
                .trim()
                .ifBlank { title }

            out.add(
                IptvChannel(
                    name = cleanedName,
                    url = url,
                    logo = attrs["tvg-logo"]?.takeIf { it.isNotBlank() },
                    group = attrs["group-title"]?.takeIf { it.isNotBlank() },
                    tvgId = attrs["tvg-id"]?.takeIf { it.isNotBlank() },
                    userAgent = urlUa ?: ua,
                    referrer = urlRef ?: ref,
                )
            )
            i = j + 1
        }
        return out
    }

    private val attrRegex = Regex("""([A-Za-z0-9_-]+)="([^"]*)"""")

    private fun parseExtInfAttrs(line: String): Map<String, String> {
        return attrRegex.findAll(line)
            .associate { it.groupValues[1].lowercase() to it.groupValues[2] }
    }

    private fun extractExtInfTitle(line: String): String {
        val matches = attrRegex.findAll(line).toList()
        val tailStart = if (matches.isNotEmpty()) matches.last().range.last + 1 else 0
        val tail = line.substring(tailStart)
        return tail.substringAfter(",", "").trim()
    }

    private fun parseUrlLine(line: String): Triple<String, String?, String?> {
        val pipeIdx = line.indexOf('|')
        if (pipeIdx == -1) return Triple(line, null, null)
        val url = line.substring(0, pipeIdx).trim()
        val params = line.substring(pipeIdx + 1).split('&')
            .mapNotNull {
                val eq = it.indexOf('=')
                if (eq == -1) null
                else it.substring(0, eq).trim().lowercase() to it.substring(eq + 1).trim()
            }
            .toMap()
        return Triple(url, params["user-agent"], params["referer"] ?: params["referrer"])
    }
}

package com.kingv2

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.nicehttp.RequestBodyTypes
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.parseJson

class KingV2 : MainAPI() {
    override var mainUrl = "https://185.169.252.47"
    override var name = "KingV2"
    override var lang = "id"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.NSFW)

    override val mainPage = mainPageOf(
        "${mainUrl}/category/bokep-indo/page/%d/" to "Terbaru",
        "${mainUrl}/category/bokep-jilbab/page/%d/" to "Jilbab",
        "${mainUrl}/category/bokep-smp/page/%d/" to "SMP",
        "${mainUrl}/category/bokep-viral/page/%d/" to "Viral"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val home = mutableListOf<SearchResponse>()
        val rawData = request.data
        val requestUrl = if (rawData.contains("%d")) {
            val formatted = rawData.format(page)
            if (page == 1) formatted.replace("/page/1/", "/") else formatted
        } else {
            if (page == 1) rawData else rawData.trimEnd('/') + "/page/$page/"
        }

        val document = app.get(requestUrl).document
        var items = document.select("div.video-block, div.col-6")

        if (items.isEmpty()) {
            // fallback to generic anchors on the page
            items = document.select("a.thumb")
        }

        items.forEach { el ->
            val a = el.selectFirst("a.thumb, a.infos, a.group") ?: return@forEach
            val hrefRaw = a.attr("href").trim()
            val href = if (hrefRaw.startsWith("http")) hrefRaw else "$mainUrl/${hrefRaw.trimStart('/') }"
            val title = a.attr("title").ifBlank { a.selectFirst("span.title")?.text() ?: a.attr("aria-label") ?: "" }
            val poster = a.selectFirst("img")?.attr("data-src") ?: a.selectFirst("img")?.attr("src")
            val duration = el.selectFirst(".duration")?.text()

            home.add(newMovieSearchResponse(title, href, TvType.Movie, false) { this.posterUrl = poster })
        }

        return newHomePageResponse(listOf(HomePageList(request.name, home, isHorizontalImages = true)))
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=${query.replace(" ", "+") }"
        val doc = app.get(url).document
        val items = doc.select("div.video-block, a.thumb, .video-loop a.thumb")
        return items.mapNotNull { el ->
            val a = el.selectFirst("a.thumb, a.infos") ?: return@mapNotNull null
            val hrefRaw = a.attr("href").trim()
            val href = if (hrefRaw.startsWith("http")) hrefRaw else "$mainUrl/${hrefRaw.trimStart('/') }"
            val title = a.attr("title") ?: a.selectFirst("span.title")?.text() ?: ""
            val poster = a.selectFirst("img")?.attr("data-src") ?: a.selectFirst("img")?.attr("src")
            newMovieSearchResponse(title, href, TvType.Movie, false) { this.posterUrl = poster }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val slug = url.trimEnd('/').substringAfterLast('/')
        val doc = app.get(url).document

        val title = doc.selectFirst("h1")?.text()?.trim() ?: ""
        val ld = doc.select("script[type=application/ld+json]").mapNotNull{ it.data() }.firstOrNull()
        var poster = doc.selectFirst("meta[property=og:image]")?.attr("content") ?: doc.selectFirst("img.video-img")?.attr("data-src")

        // get playlist from iframe embed or meta ld
        var playlist: String? = null
        playlist = doc.selectFirst("meta[itemprop=embedURL]")?.attr("content")
        if (playlist.isNullOrBlank()) playlist = doc.selectFirst("iframe")?.attr("data-litespeed-src")?.let { if (it.startsWith("//")) "https:$it" else it }

        // try JSON-LD VideoObject contentUrl
        if (playlist.isNullOrBlank() && ld != null) {
            try {
                val parsed = parseJson<Map<String, Any>>(ld)
                val content = parsed["contentUrl"]
                if (content is String) playlist = content
                val thumb = parsed["thumbnailUrl"]
                if (thumb is String) poster = thumb
            } catch (_: Exception) {}
        }

        // Normalize playlist (if embed link points to a player, we may need to resolve it)

        val durationStr = doc.selectFirst(".duration")?.text() ?: doc.selectFirst("meta[itemprop=duration]")?.attr("content")
        val duration = durationStr?.split(":")?.mapNotNull { it.toIntOrNull() }?.let { parts ->
            when (parts.size) {
                3 -> parts[0] * 60 + parts[1]
                2 -> parts[0]
                1 -> parts[0]
                else -> 0
            }
        } ?: doc.selectFirst("meta[property=video:duration]")?.attr("content")?.toIntOrNull()?.let { (it+59)/60 } ?: 0

        return newMovieLoadResponse(title, url, TvType.Movie, LoadData(id = slug, detailPath = "${"view/"+slug}" ).toJson()) {
            this.posterUrl = poster
            this.plot = doc.selectFirst("meta[property=og:description]")?.attr("content") ?: doc.selectFirst(".video-description p")?.text()
            this.duration = duration
            this.tags = doc.select("a[href*='/category/']").map { it.text().trim() }.filter { it.isNotEmpty() }.distinct()
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val media = parseJson<LoadData>(data)
        val candidates = mutableListOf<String>()
        media.detailPath?.let { dp -> if (dp.startsWith("http")) candidates.add(dp) else candidates.add("$mainUrl/${dp.trimStart('/')}/") }
        media.id?.let { if (it.isNotBlank()) { candidates.add("$mainUrl/view/${it.trimStart('/')}/") ; candidates.add("$mainUrl/${it.trimStart('/')}/") } }

        var playlist: String? = null
        for (c in candidates) {
            try {
                val doc = app.get(c).document
                // check for data-playlist on video tag
                playlist = doc.selectFirst("video#bokep-player")?.attr("data-playlist")
                if (!playlist.isNullOrBlank()) break
                // check for iframe embed src
                val iframe = doc.selectFirst("iframe")?.attr("data-litespeed-src") ?: doc.selectFirst("iframe")?.attr("src")
                if (!iframe.isNullOrBlank()) {
                    // if iframe points to a stream host, try to fetch and parse embed
                    val embedDoc = app.get(if (iframe.startsWith("//")) "https:$iframe" else iframe).document
                    playlist = embedDoc.selectFirst("meta[itemprop=contentUrl]")?.attr("content") ?: embedDoc.selectFirst("meta[property=og:video]")?.attr("content")
                }
                if (!playlist.isNullOrBlank()) break
            } catch (e: Exception) {}
        }

        if (playlist.isNullOrBlank()) return false

        M3u8Helper.generateM3u8(name, playlist, "$mainUrl/").forEach(callback)
        return true
    }

    data class LoadData(
        val id: String? = null,
        val detailPath: String? = null,
        val title: String? = null
    )
}

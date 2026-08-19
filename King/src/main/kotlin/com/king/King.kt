package com.king

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.nicehttp.RequestBodyTypes
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.parseJson

class King : MainAPI() {
    override var mainUrl = "https://kingbokep.net"
    override var name = "King"
    override var lang = "id"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.NSFW)

    override val mainPage = mainPageOf(
        "$mainUrl/page/%d/" to "Terbaru",
        "$mainUrl/category/viral/page/%d/" to "Viral"
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

        var items = document.select("li.video-card")

        if (items.isEmpty() && page == 1) {
            val fallbackUrl = when {
                rawData.contains("%d") -> rawData.format(1).replace("/page/1/", "/")
                rawData.endsWith("/") -> rawData
                else -> rawData + "/"
            }
            try {
                val fallbackDoc = app.get(fallbackUrl).document
                items = fallbackDoc.select("li.video-card")
            } catch (e: Exception) {
            }
        }

        items.forEach { el ->
            val a = el.selectFirst("a.group") ?: return@forEach
            val hrefRaw = a.attr("href").trim()
            val href = if (hrefRaw.startsWith("http")) hrefRaw else "$mainUrl/${hrefRaw.trimStart('/') }"
            val title = a.attr("title").ifBlank { a.selectFirst("span.video-card-title")?.text() ?: "" }
            val poster = a.selectFirst("img")?.attr("data-src") ?: a.selectFirst("img")?.attr("src")
            val duration = el.selectFirst(".video-card-badge .text-xs")?.text()

            home.add(
                newMovieSearchResponse(title, href, TvType.Movie, false) {
                    this.posterUrl = poster
                }
            )
        }

        val isHorizontal = true
        return newHomePageResponse(listOf(HomePageList(request.name, home, isHorizontalImages = true)))
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/search/?keyword=${query.replace(" ", "+") }"
        val doc = app.get(url).document
        val items = doc.select("li.video-card")
        return items.mapNotNull { el ->
            val a = el.selectFirst("a.group") ?: return@mapNotNull null
            val hrefRaw = a.attr("href").trim()
            val href = if (hrefRaw.startsWith("http")) hrefRaw else "$mainUrl/${hrefRaw.trimStart('/') }"
            val title = a.attr("title").ifBlank { a.selectFirst("span.video-card-title")?.text() ?: "" }
            val poster = a.selectFirst("img")?.attr("data-src") ?: a.selectFirst("img")?.attr("src")
            newMovieSearchResponse(title, href, TvType.Movie, false) { this.posterUrl = poster }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val slug = url.trimEnd('/').substringAfterLast('/')
        val doc = app.get(url).document

        val title = doc.selectFirst("h1")?.text()?.trim() ?: ""

        val videoEl = doc.selectFirst("video#bokep-player")
        val playlist = videoEl?.attr("data-playlist")
        var poster = videoEl?.attr("poster")

        if (poster.isNullOrBlank()) {
            val ld = doc.select("script[type=application/ld+json]")
                .mapNotNull { it.data() }
                .firstOrNull { it.contains("\"thumbnailUrl\"") }
            if (ld != null) {
                try {
                    val parsed = parseJson<Map<String, Any>>(ld)
                    val thumbAny = parsed["thumbnailUrl"]
                    if (thumbAny is String) poster = thumbAny
                } catch (e: Exception) {
                }
            }
        }

        if (poster.isNullOrBlank()) poster = doc.selectFirst("meta[property=og:image]")?.attr("content")

        val durationStr = doc.selectFirst("[data-pagefind-meta=duration]")?.text()

        val duration = durationStr?.split(":")?.mapNotNull { it.toIntOrNull() }?.let { parts ->
            when (parts.size) {
                3 -> parts[0] * 60 + parts[1]
                2 -> parts[0]
                1 -> parts[0]
                else -> 0
            }
        } ?: run {
            val videoDurationSec = doc.selectFirst("meta[property=video:duration]")?.attr("content")?.toIntOrNull()
            if (videoDurationSec != null) (videoDurationSec + 59) / 60 else 0
        }

        val tags = doc.select("a[href*='/category/']").map { it.text().trim() }.filter { it.isNotEmpty() }.distinct()

        var year: Int? = null
        val publishedMeta = doc.selectFirst("meta[property=article:published_time]")?.attr("content")
        if (publishedMeta != null) {
            year = publishedMeta.substringBefore("T").substringBefore("-").toIntOrNull()
        }
        if (year == null) {
            val dataTanggal = doc.selectFirst("[data-tanggal]")?.attr("data-tanggal")?.toLongOrNull()
            if (dataTanggal != null) {
                try {
                    year = java.time.Instant.ofEpochMilli(dataTanggal).atZone(java.time.ZoneId.systemDefault()).year
                } catch (e: Exception) {
                }
            }
        }

        val tvType = TvType.Movie

        return newMovieLoadResponse(title, url, tvType, LoadData(id = slug, detailPath = "view/$slug").toJson()) {
            this.posterUrl = poster
            this.plot = doc.selectFirst("meta[name=description]")?.attr("content")
            this.year = year
            this.duration = duration
            this.tags = tags
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

        media.detailPath?.let {
            val dp = it.trim()
            if (dp.startsWith("http")) candidates.add(dp)
            else candidates.add("$mainUrl/${dp.trimStart('/')}/")
        }

        media.id?.let {
            if (it.isNotBlank()) {
                candidates.add("$mainUrl/view/${it.trimStart('/')}/")
                candidates.add("$mainUrl/${it.trimStart('/')}/")
            }
        }

        var playlist: String? = null
        for (c in candidates) {
            try {
                val doc = app.get(c).document
                playlist = doc.selectFirst("video#bokep-player")?.attr("data-playlist")
                if (!playlist.isNullOrBlank()) break
            } catch (e: Exception) {
            }
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

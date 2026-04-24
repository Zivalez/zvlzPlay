package com.king

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.nicehttp.RequestBodyTypes
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.parseJson

class King : MainAPI() {
    override var mainUrl = "https://kingbokep.tv"
    override var name = "King"
    override var lang = "id"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.NSFW)

    override val mainPage = mainPageOf(
        "$mainUrl/page/%d/" to "Terbaru",
        "$mainUrl/category/indonesia/page/%d/" to "Indonesia",
        "$mainUrl/category/viral/page/%d/" to "Viral"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val home = mutableListOf<SearchResponse>()
        val rawData = request.data
        val requestUrl = if (rawData.contains("%d")) {
            // Some sites use '/' for the first page and '/page/2/' for others.
            val formatted = rawData.format(page)
            if (page == 1) formatted.replace("/page/1/", "/") else formatted
        } else {
            // If caller passed a base url (like / or /category/name/), append paging for page>1
            if (page == 1) rawData else rawData.trimEnd('/') + "/page/$page/"
        }

        val document = app.get(requestUrl).document

        var items = document.select("li.video-card")

        // Some pages (e.g. /page/1/) may return a 404 or different template. If we got no items for the first page,
        // try a fallback to the site root or the category base (without /page/1/).
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
                // ignore and continue with empty list
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

        // Use horizontal (landscape) layout for category lists to match extensions/Twitch pattern
        val isHorizontal = rawData.contains("/category/") || request.name.lowercase().contains("indonesia") || request.name.lowercase().contains("viral")
        return if (isHorizontal) {
            newHomePageResponse(listOf(HomePageList(request.name, home, isHorizontalImages = true)))
        } else {
            newHomePageResponse(request.name, home)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/search/?keyword=${query.replace(" ", "+") }"
        val doc = app.get(url).document
        val items = doc.select("li.video-card")
        return items.mapNotNull { el ->
            val a = el.selectFirst("a.group") ?: return@mapNotNull null
            val hrefRaw = a.attr("href").trim()
            val href = if (hrefRaw.startsWith("http")) hrefRaw else "$mainUrl/${hrefRaw.trimStart('/') }"
            val title = a.attr("title") ?: a.selectFirst("span.video-card-title")?.text() ?: ""
            val poster = a.selectFirst("img")?.attr("data-src") ?: a.selectFirst("img")?.attr("src")
            newMovieSearchResponse(title, href, TvType.Movie, false) { this.posterUrl = poster }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val slug = url.substringAfterLast("/")
        val doc = app.get("$mainUrl/$slug/").document

        val title = doc.selectFirst("h1")?.text()?.trim() ?: ""
        val poster = doc.selectFirst("video#bokep-player")?.attr("poster")
        val playlist = doc.selectFirst("video#bokep-player")?.attr("data-playlist")
        val durationStr = doc.selectFirst("[data-pagefind-meta=duration]")?.text()

        // Parse duration into seconds (MM:SS or HH:MM:SS)
        val duration = durationStr?.split(":")?.mapNotNull { it.toIntOrNull() }?.let { parts ->
            when (parts.size) {
                3 -> parts[0] * 3600 + parts[1] * 60 + parts[2]
                2 -> parts[0] * 60 + parts[1]
                1 -> parts[0]
                else -> 0
            }
        } ?: 0

        // Tags / categories
        val tags = doc.select("a[href*='/category/']").map { it.text().trim() }.filter { it.isNotEmpty() }.distinct()

        // Published year: try meta[property=article:published_time] -> yyyy-MM-ddT... ; fallback to data-tanggal (ms)
        var year: Int? = null
        val publishedMeta = doc.selectFirst("meta[property=article:published_time]")?.attr("content")
        if (publishedMeta != null) {
            year = publishedMeta.substringBefore("T").substringBefore("-")?.toIntOrNull()
        }
        if (year == null) {
            val dataTanggal = doc.selectFirst("[data-tanggal]")?.attr("data-tanggal")?.toLongOrNull()
            if (dataTanggal != null) {
                try {
                    year = java.time.Instant.ofEpochMilli(dataTanggal).atZone(java.time.ZoneId.systemDefault()).year
                } catch (e: Exception) {
                    // ignore
                }
            }
        }

        val tvType = TvType.Movie

        return newMovieLoadResponse(title, url, tvType, LoadData(slug).toJson()) {
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
        val slug = media.id ?: media.detailPath ?: media.title ?: ""
        val playlist = app.get("$mainUrl/$slug/").document.selectFirst("video#bokep-player")?.attr("data-playlist") ?: return false

        // Use M3u8Helper to generate extractor links for all variants/qualities
        M3u8Helper.generateM3u8(
            name,
            playlist,
            "$mainUrl/",
        ).forEach(callback)

        return true
    }

    data class LoadData(
        val id: String? = null,
        val detailPath: String? = null,
        val title: String? = null
    )
}

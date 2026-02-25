package com.winbu

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Winbu : MainAPI() {
    override var mainUrl = "https://winbu.net"
    override var name = "Winbu"
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA,
    )

    override val mainPage = mainPageOf(
        "anime-terbaru-animasu/page/%d/" to "Series Terbaru",
        "animedonghua/page/%d/"          to "Anime Donghua",
        "film/page/%d/"                  to "Film",
        "others/page/%d/"                to "Jepang Korea China Barat",
        "tvshow/page/%d/"                to "TV Show",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data.format(page)}").document
        val homeList = document.select("div.ml-item").mapNotNull { it.toSearchResult() }
        return newHomePageResponse(request.name, homeList)
    }

    private fun Element.toSearchResult(): AnimeSearchResponse? {
        val a = selectFirst("a.ml-mask") ?: return null
        val title = a.attr("title").trim().ifEmpty {
            selectFirst(".judul")?.text()?.trim()
        } ?: return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        val posterUrl = fixUrlNull(
            selectFirst("img")?.attr("data-original")
                ?: selectFirst("img")?.attr("src")
        )
        return newAnimeSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("div.ml-item").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document

        // Title is in the first .judul inside .mli-info on the detail card
        val title = document.selectFirst(".mli-info .judul")?.text()?.trim()
            ?: return null

        val poster = document.selectFirst("img.mli-thumb")?.attr("src")
        val tags = document.select(".mli-mvi a[href*='/genre/']").map { it.text() }
        val year = document.selectFirst(".mli-mvi a[href*='/season/']")?.text()?.let {
            Regex("(\\d{4})").find(it)?.groupValues?.getOrNull(1)?.toIntOrNull()
        }
        val description = document.selectFirst("div.mli-desc p")?.text()?.trim()

        // Trailer iframe src — only add if it has an actual video ID
        val trailerSrc = document.selectFirst("#pop-trailer iframe")?.attr("src")
            ?.takeIf { it.contains("youtube.com/embed/") && it.length > "https://www.youtube.com/embed/".length }

        val episodes = document.select("div.tvseason div.les-content a").mapNotNull { el ->
            val epTitle = el.text().trim()
            val epHref = fixUrlNull(el.attr("href")) ?: return@mapNotNull null
            val epNum = Regex("Episode\\s*(\\d+)", RegexOption.IGNORE_CASE)
                .find(epTitle)?.groupValues?.getOrNull(1)?.toIntOrNull()
            newEpisode(epHref) {
                name = epTitle
                episode = epNum
            }
        }.reversed()

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            engName = title
            posterUrl = poster
            this.year = year
            addEpisodes(DubStatus.Subbed, episodes)
            plot = description
            if (trailerSrc != null) addTrailer(trailerSrc)
            this.tags = tags
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document

        // Iterate each quality dropdown (360p, 480p, 720p, 1080p) and fetch each player via AJAX
        document.select(".dropdown").forEach { dropdown ->
            dropdown.select(".east_player_option").forEach { player ->
                val post = player.attr("data-post").takeIf { it.isNotEmpty() } ?: return@forEach
                val nume = player.attr("data-nume").takeIf { it.isNotEmpty() } ?: return@forEach
                val type = player.attr("data-type").ifEmpty { "schtml" }

                val response = app.post(
                    "$mainUrl/wp-admin/admin-ajax.php",
                    data = mapOf(
                        "action" to "player_ajax",
                        "post"   to post,
                        "nume"   to nume,
                        "type"   to type,
                    ),
                    referer = data,
                ).text

                val iframeSrc = Jsoup.parse(response).selectFirst("iframe")?.attr("src")
                    ?.takeIf { it.startsWith("http") } ?: return@forEach

                loadExtractor(iframeSrc, data, subtitleCallback, callback)
            }
        }

        // Also extract download links — CloudStream will handle supported hosts (e.g. Mp4Upload)
        document.select("#downloadb li").forEach { li ->
            li.select("a").forEach { a ->
                val href = fixUrlNull(a.attr("href")) ?: return@forEach
                loadExtractor(href, data, subtitleCallback, callback)
            }
        }

        return true
    }
}

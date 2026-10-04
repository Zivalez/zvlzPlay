package com.samehadaku

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addAniListId
import com.lagradost.cloudstream3.LoadResponse.Companion.addMalId
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.LoadResponse.Companion.addScore
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.amap
import kotlinx.coroutines.runBlocking
import org.jsoup.nodes.Element

class Samehadaku : MainAPI() {
    override var mainUrl = "https://v2.samehadaku.how"
    override var name = "Samehadaku"
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA
    )

    companion object {
        private const val UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/154.0.0.0 Safari/537.36"
        fun getType(t: String): TvType = when {
            t.contains("OVA", true) || t.contains("Special", true) -> TvType.OVA
            t.contains("Movie", true) -> TvType.AnimeMovie
            else -> TvType.Anime
        }
        fun getStatus(t: String): ShowStatus = when (t) {
            "Completed" -> ShowStatus.Completed
            "Ongoing" -> ShowStatus.Ongoing
            else -> ShowStatus.Completed
        }
    }

    private val headers = mapOf(
        "User-Agent" to UA,
        "Referer" to "$mainUrl/",
        "Accept" to "text/html,application/xhtml+xml"
    )

    override val mainPage = mainPageOf(
        "anime-terbaru/page/%d" to "Terbaru",
        "daftar-anime-2/page/%d/?order=update" to "Update",
        "daftar-anime-2/page/%d/?order=popular" to "Populer",
        "daftar-anime-2/page/%d/?type=Movie" to "Movie",
        "daftar-anime-2/page/%d/?status=Currently%20Airing" to "Ongoing",
        "daftar-anime-2/page/%d/?status=Finished%20Airing" to "Completed",
    )
    
    
    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        // Ganti %d manual: request.data berisi %20 (Currently%20Airing) yang
        // dibaca String.format sebagai format-specifier -> MissingFormatArgumentException.
        val url = "$mainUrl/${request.data.replace("%d", page.toString())}"
        val document = app.get(url, headers = headers).document
        // Site 2026: "Terbaru" = li[itemtype*=CreativeWork] (16 home / 14 archive).
        // Genre/Movie archive kini render widget home (Top10+Latest), tanpa article.
        // Fallback berlapis agar tidak kosong: animepost baru -> lama -> latest -> series.
        val items = if (request.name == "Terbaru") {
            document.select("li[itemtype*='CreativeWork']").ifEmpty {
                document.select("div.anime-grid article.animepost")
            }.ifEmpty { document.select("article.animepost") }
        } else {
            document.select("div.anime-grid article.animepost").ifEmpty {
                document.select("article.animepost")
            }.ifEmpty { document.select("div.animepost, article.animpost") }
                .ifEmpty { document.select("li[itemtype*='CreativeWork']") }
        }
        val homeList = items.mapNotNull {
            if (request.name == "Terbaru") it.toLatestAnimeResult() ?: it.toSearchResult()
            else it.toSearchResult() ?: it.toLatestAnimeResult()
        }.distinctBy { it.url }
        // Poster Terbaru landscape 300x169 -> horizontal (King.kt:65 pattern).
        // Sisanya poster portrait -> vertical default.
        return if (request.name == "Terbaru") {
            newHomePageResponse(listOf(HomePageList(request.name, homeList, isHorizontalImages = true)))
        } else {
            newHomePageResponse(request.name, homeList)
        }
    }

    private fun Element.toSearchResult(): AnimeSearchResponse? {
        // Struktur baru 2026: article.animepost > div.animposx > a[title] + div.content-thumb img.anmsa
        // Struktur lama: div.animepost a + div.title h2. Top10: a.series + span.judul.
        val a = this.selectFirst("div.animposx a")
            ?: this.selectFirst("a[title]")
            ?: this.selectFirst("a.series")
            ?: this.selectFirst("a[href*='/anime/'], a[href*='/batch/']")
            ?: if (this.tagName() == "a") this else return null
        val rawTitle = a.attr("title").ifBlank {
            a.selectFirst("div.title h2")?.text() ?: ""
        }.ifBlank { this.selectFirst("h2 a, h2")?.text()?.trim() ?: "" }
            .ifBlank { a.selectFirst("span.judul")?.text()?.trim() ?: "" }
            .ifBlank { a.text().trim() }.trim()
        val title = rawTitle.removeBloat().ifBlank { return null }
        val href = fixUrlNull(a.attr("href")) ?: fixUrlNull(this.selectFirst("a")?.attr("href")) ?: return null
        val posterUrl = fixUrlNull(
            this.selectFirst("div.content-thumb img")?.attr("src")?.takeIf { it.isNotBlank() }
                ?: this.selectFirst("img.anmsa")?.attr("src")
                ?: this.selectFirst("img")?.attr("src")
                ?: a.selectFirst("img")?.attr("src")
        )
        val statusText = a.selectFirst("div.data > div.type")?.text()?.trim()
            ?: this.selectFirst("div.data > div.type, span.rating, div.type")?.text()?.trim() ?: ""

        return newAnimeSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = posterUrl
            if (statusText.isNotBlank()) addDubStatus(statusText)
        }
    }

    private fun Element.toLatestAnimeResult(): AnimeSearchResponse? {
        val a = this.selectFirst("div.thumb a") ?: this.selectFirst("a[href]") ?: return null
        val title = this.selectFirst("h2.entry-title a")?.text()?.removeBloat()
            ?: a.attr("title")?.removeBloat()
            ?: this.selectFirst("h2.entry-title")?.text()?.removeBloat()
            ?: return null
        if (title.isBlank()) return null
        val href = fixUrlNull(a.attr("href")) ?: return null
        // img lazy: src / data-src / data-original
        val img = a.selectFirst("img") ?: this.selectFirst("img")
        val posterUrl = fixUrlNull(
            img?.attr("src")?.takeIf { it.isNotBlank() && !it.contains("favicon.ico") }
                ?: img?.attr("data-src")?.takeIf { it.isNotBlank() }
                ?: img?.attr("data-original")?.takeIf { it.isNotBlank() }
        )
        val epNum = this.selectFirst("div.dtla span:has(b)")?.ownText()?.trim()?.toIntOrNull()
            ?: this.selectFirst("div.dtla span")?.text()?.let { Regex("(\\d+)").find(it)?.groupValues?.getOrNull(1)?.toIntOrNull() }
            ?: this.selectFirst("div.dtla author")?.text()?.toIntOrNull()

        return newAnimeSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = posterUrl
            addSub(epNum)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query", headers = headers).document
        // 2026: article.animepost dalam div.anime-grid, main#content bukan main#main
        return document.select("article.animepost, div.animepost").mapNotNull { it.toSearchResult() }
            .distinctBy { it.url }
    }

    override suspend fun load(url: String): LoadResponse? {
        val fixUrl = if (url.contains("/anime/") || url.contains("/batch/")) url
        else app.get(url, headers = headers).document.selectFirst("div.nvs.nvsc a")?.attr("href")

        val document = app.get(fixUrl ?: return null, headers = headers).document

        // Judul baru 2026: main h1[itemprop=headline] "One Piece Sub Indo". Lama: h1.entry-title.
        val title = document.selectFirst("main h1[itemprop=headline]")?.text()?.removeBloat()
            ?: document.selectFirst("main h1")?.text()?.removeBloat()
            ?: document.selectFirst("h1.entry-title")?.text()?.removeBloat()
            ?: document.selectFirst("meta[property=og:title]")?.attr("content")?.removeBloat()
            ?: return null
        // Poster baru: main img.object-cover (uploads). Lama: div.thumb > img.
        val poster = document.select("main img").firstOrNull {
            val s = it.attr("src")
            s.contains("/wp-content/uploads/") && !s.contains("favicon.ico")
        }?.attr("src")
            ?: document.selectFirst("div.thumb > img")?.attr("src")
            ?: document.selectFirst("meta[property=og:image]")?.attr("content")
        // Genre baru: flex-wrap block di kartu detail. Lama: div.genre-info > a.
        // Scope ke div.flex agar tooltip rekomendasi (div.mta) tidak ikut.
        val tags = document.select("div.flex a[href*='/genre/']").map { it.text().trim() }.filter { it.isNotBlank() }.distinct()
            .ifEmpty { document.select("div.genre-info > a").map { it.text() } }
        // Detail grid baru: div.grid > div.flex > span[label] + span[value].
        val detailMap: Map<String, String> = try {
            document.select("div.grid div.flex").mapNotNull {
                val spans = it.select("span")
                if (spans.size >= 2) spans[0].text().trim() to spans[1].text().trim() else null
            }.toMap()
        } catch (_: Exception) { emptyMap() }
        fun detail(vararg keys: String): String? = keys.firstNotNullOfOrNull { detailMap[it] }
        val year = document.selectFirst("div.spe > span:contains(Rilis)")?.ownText()?.let {
            Regex("\\d,\\s(\\d*)").find(it)?.groupValues?.getOrNull(1)?.toIntOrNull()
        } ?: detail("Released")?.let { Regex("(19|20)\\d{2}").find(it)?.value?.toIntOrNull() }
            ?: detail("Season")?.let { Regex("(19|20)\\d{2}").find(it)?.value?.toIntOrNull() }
        val status = getStatus(detail("Status") ?: document.selectFirst("div.spe > span:contains(Status)")?.ownText() ?: "Completed")
        val type = getType(detail("Type") ?: document.selectFirst("div.spe > span:contains(Type)")?.ownText()?.trim()?.lowercase() ?: "tv")
        val rating = document.selectFirst("span.ratingValue")?.text()?.trim()?.toDoubleOrNull()
            ?: document.selectFirst("div.text-center span.text-base, span.font-extrabold")?.text()?.trim()?.toDoubleOrNull()
            ?: document.selectFirst("[itemprop=ratingValue], .rating")?.text()?.trim()?.let { Regex("[0-9]+(\\.[0-9]+)?").find(it)?.value?.toDoubleOrNull() }
        val description = document.select("main div.text-justify p").text().trim()
            .ifBlank { document.select("div.desc p").text().trim() }
            .ifBlank { document.selectFirst("meta[property=og:description]")?.attr("content")?.trim() ?: "" }
        val trailer = document.selectFirst("div.trailer-anime iframe")?.attr("src")
            ?: document.selectFirst("iframe[src*='youtube']")?.attr("src")

        val japName = detail("Japanese") ?: document.selectFirst("div.spe > span:contains(Japanese)")?.ownText()?.trim()
        val engTitle = detail("English") ?: document.selectFirst("div.spe > span:contains(English)")?.ownText()?.trim()
        val duration = (detail("Duration") ?: document.selectFirst("div.spe > span:contains(Duration)")?.ownText())
            ?.filter { it.isDigit() }?.toIntOrNull()
        val studio = detail("Studio") ?: document.selectFirst("div.spe > span:contains(Studio) a")?.text()?.trim()

        // Episode baru: scroll list max-h-[520px], tiap row 2 link sama href (angka + judul).
        // Lama: div.lstepsiode.listeps ul li > span.lchx > a.
        val episodeLinks = document.select("div.lstepsiode.listeps ul li, .lstepsiode li, .eplister li").mapNotNull {
            val header = it.selectFirst("span.lchx > a") ?: it.selectFirst("a[href*='episode'], a[href*='/anime/']") ?: return@mapNotNull null
            val episode = Regex("Episode\\s?(\\d+)").find(header.text())?.groupValues?.getOrNull(1)?.toIntOrNull()
            val link = fixUrl(header.attr("href"))
            Pair(link, episode)
        }.ifEmpty {
            document.select("a[href*='-episode-']").mapNotNull {
                val href = it.attr("href")
                if (href.isBlank()) return@mapNotNull null
                val link = fixUrl(href)
                val ep = Regex("Episode\\s?(\\d+)").find(it.text())?.groupValues?.getOrNull(1)?.toIntOrNull()
                    ?: Regex("-episode-(\\d+)").find(href)?.groupValues?.getOrNull(1)?.toIntOrNull()
                Pair(link, ep)
            }
        }.distinctBy { it.first }.reversed()

        val thumbMap: Map<String, String> = if (episodeLinks.isNotEmpty()) {
            try {
                app.get(episodeLinks.first().first, headers = headers).document
                    .select("div.lstepsiode.listeps ul li, .lstepsiode li")
                    .mapNotNull { li ->
                        val img = li.selectFirst("div.thumbnailrighteps img, img") ?: return@mapNotNull null
                        val href = li.selectFirst("span.lchx > a, a")?.attr("href")
                            ?.let { fixUrl(it) } ?: return@mapNotNull null
                        val src = img.attr("src").ifBlank { img.attr("data-src") }
                        if (src.isBlank() || src.contains("favicon.ico")) return@mapNotNull null
                        href to src
                    }.toMap()
            } catch (_: Exception) { emptyMap() }
        } else emptyMap()

        val episodes = episodeLinks.map { (link, epNum) ->
            newEpisode(link) {
                this.episode = epNum
                this.posterUrl = thumbMap[link]
            }
        }

        val recommendations = document.select("div.recom-section article.animepost").mapNotNull { it.toSearchResult() }
            .ifEmpty { document.select("aside#sidebar ul li").mapNotNull { it.toSearchResult() } }

        val tracker = APIHolder.getTracker(listOf(title), TrackerType.getTypes(type), year, true)

        return newAnimeLoadResponse(title, url, type) {
            this.japName = japName
            engName = engTitle ?: title
            posterUrl = tracker?.image ?: poster
            backgroundPosterUrl = tracker?.cover
            this.year = year
            this.duration = duration
            addEpisodes(DubStatus.Subbed, episodes)
            showStatus = status
            if (rating != null) addScore(rating.toString())
            plot = description
            addTrailer(trailer)
            this.tags = if (studio != null) tags + studio else tags
            this.recommendations = recommendations
            addMalId(tracker?.malId)
            addAniListId(tracker?.aniId?.toIntOrNull())
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data, headers = headers).document

        // 1. Direct player terbukti 2026-10-04: blogger.com/video.g?token=... (One Piece 1180)
        // Wajib duluan, ini stream langsung. Downloadb hanya fallback file-host.
        document.select("iframe[src]").mapNotNull {
            val src = it.attr("src").ifBlank { it.attr("data-src") }.trim()
            if (src.isBlank() || src.contains("youtube.com") || src.contains("google.com/recaptcha")) null
            else if (src.startsWith("//")) "https:$src" else src
        }.distinct().amap { href ->
            try {
                loadExtractor(href, "$mainUrl/", subtitleCallback, callback)
            } catch (_: Exception) { }
        }

        // 2. Fallback download: div#downloadb li > strong[quality] + a (acefile/dll).
        // Struktur valid 13 item: 360p/480p/720p/1080p/4K + x265.
        // SKIP filedon.co: tidak ada extractor Filedon di app upstream
        // (recloudstream/cloudstream/library/.../extractors/, 118 file, tanpa Filedon;
        // hanya Otakudesu yang register custom Filedon sendiri), dan TCP 443-nya
        // timeout di jaringan ID. Gofile tetap dipakai: extractor Gofile.kt ada
        // di upstream, jalan di jaringan yang tidak memblokirnya.
        document.select("div#downloadb li").flatMap { el ->
            val quality = el.select("strong").text()
            el.select("a").mapNotNull { a ->
                val u = fixUrl(a.attr("href"))
                if (u.contains("filedon.co")) null else Pair(u, quality)
            }
        }.amap { (url, quality) ->
            loadFixedExtractor(url, quality, "$mainUrl/", subtitleCallback, callback)
        }
        return true
    }

    private suspend fun loadFixedExtractor(
        url: String,
        name: String,
        referer: String? = null,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        loadExtractor(url, referer, subtitleCallback) { link ->
            runBlocking {
                callback.invoke(
                    newExtractorLink(link.name, link.name, link.url, link.type) {
                        this.referer = link.referer
                        this.quality = name.fixQuality()
                        this.headers = link.headers
                        this.extractorData = link.extractorData
                    }
                )
            }
        }
    }

    private fun String.fixQuality(): Int = when (this.uppercase()) {
        "4K" -> Qualities.P2160.value
        "FULLHD" -> Qualities.P1080.value
        "MP4HD" -> Qualities.P720.value
        else -> this.filter { it.isDigit() }.toIntOrNull() ?: Qualities.Unknown.value
    }

    private fun String.removeBloat(): String =
        this.replace(Regex("(Nonton)|(Anime)|(Subtitle\\sIndonesia)|(Sub\\sIndo)", RegexOption.IGNORE_CASE), "").trim()
}

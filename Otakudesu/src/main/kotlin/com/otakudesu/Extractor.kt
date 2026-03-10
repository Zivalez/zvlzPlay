package com.otakudesu

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.extractors.Filesim
import com.lagradost.cloudstream3.extractors.JWPlayer
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink

/**
 * Handles all desustream.info embed pages.
 * Two variants exist:
 *  1. Pages with a direct <video> tag (e.g. ondesuhd) — extract src, unescape HTML entities.
 *  2. Pages with a Blogger iframe — fetch Blogger page, call batchexecute API for MP4 URL.
 */
open class DesuStream : ExtractorApi() {
    override val name = "DesuStream"
    override val mainUrl = "https://desustream.info/"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink> {
        val sources = mutableListOf<ExtractorLink>()
        try {
            val doc = app.get(url, referer = referer).document

            // Case 1: direct <video> tag (e.g. ondesuhd pages)
            val videoSrc = doc.selectFirst("video source")?.attr("src")?.takeIf { it.isNotBlank() }
            if (videoSrc != null) {
                val itag = Regex("""itag=(\d+)""").find(videoSrc)?.groupValues?.get(1)
                sources.add(newExtractorLink(name, name, videoSrc) {
                    this.referer = url
                    this.quality = itagToQuality(itag)
                })
                return sources
            }

            // Case 2: Blogger iframe — needs batchexecute
            val bloggerUrl = doc.selectFirst("iframe#myIframe")?.attr("abs:src")
                ?: doc.selectFirst("iframe[src*='blogger.com/video.g']")?.attr("abs:src")
                ?: return sources

            val token = bloggerUrl.substringAfter("token=").substringBefore("&").takeIf { it.isNotBlank() }
                ?: return sources

            val bloggerHtml = app.get(bloggerUrl).text
            val fsid = Regex(""""FdrFJe":"([^"]+)"""").find(bloggerHtml)?.groupValues?.get(1) ?: return sources
            val bl = Regex(""""cfb2h":"([^"]+)"""").find(bloggerHtml)?.groupValues?.get(1) ?: return sources

            val batchUrl = "https://www.blogger.com/_/BloggerVideoPlayerUi/data/batchexecute" +
                    "?rpcids=WcwnYd&source-path=%2Fvideo.g&f.sid=$fsid&bl=$bl&hl=en-US&rt=c"
            val response = app.post(
                batchUrl,
                data = mapOf("f.req" to """[[["WcwnYd","[\"$token\",\"\",0]",null,"generic"]]]""")
            ).text

            // URL is doubly JSON-encoded: = → \u003d (inner) → \\u003d (outer)
            // regex allows backslash sequences: (?:[^"\\]|\\.)*
            val urlRegex = Regex("""https://(?:[^"\\]|\\.)*googlevideo\.com/videoplayback(?:[^"\\]|\\.)*""")
            urlRegex.findAll(response).forEach { match ->
                val videoUrl = match.value
                    .replace("\\\\u003d", "=")
                    .replace("\\\\u0026", "&")
                    .replace("\\u003d", "=")
                    .replace("\\u0026", "&")
                    .trimEnd('\\', '"')
                if (!videoUrl.contains("mime=video")) return@forEach
                val itag = Regex("""itag=(\d+)""").find(videoUrl)?.groupValues?.get(1)
                sources.add(newExtractorLink(name, name, videoUrl) {
                    this.referer = "https://www.blogger.com/"
                    this.quality = itagToQuality(itag)
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sources
    }

    private fun itagToQuality(itag: String?): Int = when (itag) {
        "18" -> Qualities.P360.value
        "22" -> Qualities.P720.value
        else -> Qualities.Unknown.value
    }
}

/**
 * Handles filedon.co embed pages (Inertia.js app).
 * The direct MP4 URL is embedded in the #app[data-page] JSON prop "url".
 */
class Filedon : ExtractorApi() {
    override val name = "Filedon"
    override val mainUrl = "https://filedon.co/embed/"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink> {
        val sources = mutableListOf<ExtractorLink>()
        try {
            val doc = app.get(url, referer = referer).document
            val dataPage = doc.selectFirst("#app")?.attr("data-page") ?: return sources

            // Extract the pre-signed MP4 URL from the Inertia props
            val videoUrl = Regex(""""url":"(https://[^"]+\.mp4[^"]*?)"""").find(dataPage)
                ?.groupValues?.get(1)
                ?.replace("\\/", "/")
                ?: return sources

            val fileName = Regex(""""name":"([^"]+)"""").find(dataPage)?.groupValues?.get(1) ?: ""
            val quality = when {
                fileName.contains("1080p", ignoreCase = true) -> Qualities.P1080.value
                fileName.contains("720p", ignoreCase = true)  -> Qualities.P720.value
                fileName.contains("480p", ignoreCase = true)  -> Qualities.P480.value
                fileName.contains("360p", ignoreCase = true)  -> Qualities.P360.value
                else -> Qualities.Unknown.value
            }
            sources.add(newExtractorLink(name, name, videoUrl) {
                this.referer = "https://filedon.co/"
                this.quality = quality
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sources
    }
}

class DesuBeta : JWPlayer() {
    override val name = "DesuBeta"
    override val mainUrl = "https://desustream.me/beta/"
}

class Desudesuhd : JWPlayer() {
    override val name = "Desudesuhd"
    override val mainUrl = "https://desustream.me/desudesuhd/"
}

class Odvidhide : Filesim() {
    override val name = "Odvidhide"
    override var mainUrl = "https://odvidhide.com"
}

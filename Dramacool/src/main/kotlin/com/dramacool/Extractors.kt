package com.dramacool

import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.extractors.MixDrop
import com.lagradost.cloudstream3.extractors.StreamTape
import com.lagradost.cloudstream3.extractors.StreamWishExtractor
import com.lagradost.cloudstream3.extractors.VidhideExtractor
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import java.util.concurrent.atomic.AtomicReference

class VidBasic : ExtractorApi() {
    override val name = "VidBasic"
    override val mainUrl = "https://vidbasic.top"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val direct = getDirectHls(url, referer) ?: return
        M3u8Helper.generateM3u8(name, direct, url).forEach(callback)
    }

    private suspend fun getDirectHls(url: String, referer: String?): String? {
        val document = app.get(url, referer = referer).document
        val iframe = document.selectFirst("iframe#embedvideo")?.attr("src")?.let {
            if (it.startsWith("//")) "https:$it" else if (it.startsWith("/")) "$mainUrl$it" else it
        }

        val html = iframe?.let { app.get(it, referer = url).text }
        Regex("""https?://[^"'<>\\\s]+\.m3u8[^"'<>\\\s]*""", RegexOption.IGNORE_CASE)
            .find(html.orEmpty())?.value?.let { return it }

        return getDirectHlsByWebView(url, referer)
    }

    private suspend fun getDirectHlsByWebView(url: String, referer: String?): String? {
        val direct = AtomicReference<String?>(null)
        val script = """
            (function() {
                function findM3u8(value) {
                    if (!value) return null;
                    var text = typeof value === 'string' ? value : JSON.stringify(value);
                    var match = text.match(/https?:[^\"'<>\\\s]+\.m3u8[^\"'<>\\\s]*/i);
                    return match ? match[0] : null;
                }
                try {
                    if (window.jwplayer) {
                        var current = findM3u8(window.jwplayer().getPlaylist());
                        if (current) return current;
                    }
                } catch(e) {}
                try {
                    for (var i = 0; i < window.frames.length; i++) {
                        var frame = window.frames[i];
                        if (frame.jwplayer) {
                            var fromFrame = findM3u8(frame.jwplayer().getPlaylist());
                            if (fromFrame) return fromFrame;
                        }
                    }
                } catch(e) {}
                try {
                    return findM3u8(document.documentElement.outerHTML);
                } catch(e) { return null; }
            })()
        """.trimIndent()

        val resolver = WebViewResolver(
            interceptUrl = Regex("""__DRAMACOOL_WV_NEVER_MATCH__"""),
            additionalUrls = listOf(Regex(""".*""")),
            useOkhttp = false,
            script = script,
            scriptCallback = { result ->
                val value = result?.trim()?.trim('"')?.replace("\\/", "/")
                if (!value.isNullOrBlank() && value.startsWith("http")) direct.compareAndSet(null, value)
            },
            timeout = 30_000L
        )

        runCatching {
            resolver.resolveUsingWebView(
                url = url,
                referer = referer ?: mainUrl,
                requestCallBack = { direct.get() != null }
            )
        }

        return direct.get()
    }
}

class Hanerix : StreamWishExtractor() {
    override var name = "Hanerix"
    override var mainUrl = "https://hanerix.com"
}

class Minochinos : VidhideExtractor() {
    override var name = "Minochinos"
    override var mainUrl = "https://minochinos.com"
}

class WatchAdsOnTape : StreamTape() {
    override var name = "WatchAdsOnTape"
    override var mainUrl = "https://watchadsontape.com"
}

class M1xDrop : MixDrop() {
    override var name = "M1xDrop"
    override var mainUrl = "https://m1xdrop.bz"
}

class UpnShare : ExtractorApi() {
    override val name = "UpnShare"
    override val mainUrl = "https://asianctv.upns.pro"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val direct = getDirectByWebView(url, referer) ?: return
        if (direct.contains(".m3u8", true)) {
            M3u8Helper.generateM3u8(name, direct, url).forEach(callback)
        } else {
            callback.invoke(
                newExtractorLink(name, name, direct, ExtractorLinkType.VIDEO) {
                    this.referer = url
                    this.quality = Qualities.Unknown.value
                }
            )
        }
    }

    private suspend fun getDirectByWebView(url: String, referer: String?): String? {
        val direct = AtomicReference<String?>(null)
        val script = """
            (function() {
                function findDirect(value) {
                    if (!value) return null;
                    var text = typeof value === 'string' ? value : JSON.stringify(value);
                    var match = text.match(/https?:[^\"'<>\\\s]+\.(?:m3u8|mp4)[^\"'<>\\\s]*/i);
                    return match ? match[0] : null;
                }
                try {
                    var videos = document.querySelectorAll('video, source');
                    for (var i = 0; i < videos.length; i++) {
                        var src = videos[i].currentSrc || videos[i].src || videos[i].getAttribute('src');
                        var found = findDirect(src);
                        if (found) return found;
                    }
                } catch(e) {}
                try { return findDirect(document.documentElement.outerHTML); } catch(e) { return null; }
            })()
        """.trimIndent()

        val resolver = WebViewResolver(
            interceptUrl = Regex("""__DRAMACOOL_UPNS_WV_NEVER_MATCH__"""),
            additionalUrls = listOf(Regex(""".*""")),
            useOkhttp = false,
            script = script,
            scriptCallback = { result ->
                val value = result?.trim()?.trim('"')?.replace("\\/", "/")
                if (!value.isNullOrBlank() && value.startsWith("http")) direct.compareAndSet(null, value)
            },
            timeout = 30_000L
        )

        runCatching {
            resolver.resolveUsingWebView(
                url = url,
                referer = referer ?: mainUrl,
                requestCallBack = { direct.get() != null }
            )
        }

        return direct.get()
    }
}

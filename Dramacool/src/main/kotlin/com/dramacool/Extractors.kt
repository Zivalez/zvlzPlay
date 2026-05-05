package com.dramacool

import android.util.Base64
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.extractors.MixDrop
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.getAndUnpack
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import java.util.concurrent.atomic.AtomicReference
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

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
        val document = app.get(url, referer = referer).document
        val nestedLinks = document.select("li.linkserver[data-video]").mapNotNull {
            it.attr("data-video").takeIf { link -> link.isNotBlank() }
        }.map { fixRelativeUrl(it, url) }.distinct()

        val iframe = document.selectFirst("iframe#embedvideo")?.attr("src")?.let { fixRelativeUrl(it, url) }
        getDirectHls(iframe ?: url, url, url)?.let { direct ->
            M3u8Helper.generateM3u8(
                name,
                direct,
                iframe ?: url,
                headers = mapOf("Origin" to mainUrl)
            ).forEach(callback)
        }

        nestedLinks.filterNot { it.contains(mainUrl, true) }.forEach { link ->
            loadExtractor(resolveExtractorUrl(link), url, subtitleCallback, callback)
        }
    }

    private suspend fun getDirectHls(url: String, referer: String, webViewUrl: String): String? {
        val response = app.get(url, referer = referer)
        Regex("""https?://[^"'<>\s]+\.m3u8[^"'<>\s]*""", RegexOption.IGNORE_CASE)
            .find(response.text)?.value?.replace("\\/", "/")?.let { return it }

        response.document.selectFirst("script[data-name=crypto][data-value]")
            ?.attr("data-value")
            ?.let { decryptVidBasicUrl(it) }
            ?.takeIf { it.contains(".m3u8", true) }
            ?.let { return it }

        return getDirectHlsByWebView(webViewUrl, referer)
    }

    private fun decryptVidBasicUrl(encrypted: String): String? {
        return runCatching {
            val key = "94588293375053432799222445521289".toByteArray(Charsets.UTF_8)
            val iv = "5259228356829423".toByteArray(Charsets.UTF_8)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
            String(cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT)), Charsets.UTF_8)
        }.getOrNull()
    }

    private suspend fun getDirectHlsByWebView(url: String, referer: String): String? {
        val direct = AtomicReference<String?>(null)
        val script = """
            (function() {
                function findM3u8(value) {
                    if (!value) return null;
                    var text = typeof value === 'string' ? value : JSON.stringify(value);
                    var match = text.match(/https?:[^\\"'<>\s]+\.m3u8[^\\"'<>\s]*/i);
                    return match ? match[0] : null;
                }
                function findFromWindow(win) {
                    try {
                        if (!win || !win.jwplayer) return null;
                        var player = win.jwplayer();
                        var item = player.getPlaylistItem && player.getPlaylistItem();
                        var current = findM3u8(item);
                        if (current) return current;
                        current = findM3u8(player.getPlaylist && player.getPlaylist());
                        if (current) return current;
                        current = findM3u8(player.getConfig && player.getConfig());
                        if (current) return current;
                    } catch(e) {}
                    return null;
                }
                try {
                    var current = findFromWindow(window);
                    if (current) return current;
                } catch(e) {}
                try {
                    for (var i = 0; i < window.frames.length; i++) {
                        var fromFrame = findFromWindow(window.frames[i]);
                        if (fromFrame) return fromFrame;
                    }
                } catch(e) {}
                try { return findM3u8(document.documentElement.outerHTML); } catch(e) { return null; }
            })()
        """.trimIndent()

        val resolver = WebViewResolver(
            interceptUrl = Regex("""__DRAMACOOL_VIDBASIC_WV_NEVER_MATCH__"""),
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
                referer = referer,
                requestCallBack = { direct.get() != null }
            )
        }

        return direct.get()
    }

    private fun fixRelativeUrl(url: String, baseUrl: String): String {
        return when {
            url.startsWith("http") -> url
            url.startsWith("//") -> "https:$url"
            url.startsWith("/") -> "$mainUrl$url"
            else -> "${baseUrl.substringBeforeLast("/")}/$url"
        }
    }

    private suspend fun resolveExtractorUrl(url: String): String {
        return runCatching {
            when {
                url.contains("hglink.to", true) -> url.replace("https://hglink.to/e/", "https://hanerix.com/e/")
                    .replace("http://hglink.to/e/", "https://hanerix.com/e/")
                else -> url
            }
        }.getOrDefault(url)
    }
}

open class XStreamHls : ExtractorApi() {
    override val name = "XStream"
    override val mainUrl = "https://example.com"
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
        val response = app.get(url, referer = referer ?: mainUrl)
        findHls(response.text, response.url)?.let { return it }
        response.document.select("script").forEach { script ->
            if (script.data().contains("eval(function(p,a,c,k,e,d)")) {
                findHls(getAndUnpack(script.data()), response.url)?.let { return it }
            }
        }
        return getDirectHlsByWebView(url, referer)
    }

    private fun findHls(text: String, baseUrl: String): String? {
        val direct = Regex("""https?://[^"'<>\s]+\.m3u8[^"'<>\s]*""", RegexOption.IGNORE_CASE)
            .find(text)?.value
        if (!direct.isNullOrBlank()) return direct.replace("\\/", "/")

        val relative = Regex("""["']([^"'<>\s]+\.m3u8[^"'<>\s]*)["']""", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.getOrNull(1)
            ?.replace("\\/", "/")
        return relative?.let { fixRelativeUrl(it, baseUrl) }
    }

    private suspend fun getDirectHlsByWebView(url: String, referer: String?): String? {
        val direct = AtomicReference<String?>(null)
        val script = """
            (function() {
                function absolute(value) {
                    if (!value) return null;
                    if (value.indexOf('http') === 0) return value;
                    if (value.indexOf('//') === 0) return location.protocol + value;
                    if (value.charAt(0) === '/') return location.origin + value;
                    return new URL(value, location.href).href;
                }
                function findM3u8(value) {
                    if (!value) return null;
                    var text = typeof value === 'string' ? value : JSON.stringify(value);
                    var match = text.match(/https?:[^\\"'<>\s]+\.m3u8[^\\"'<>\s]*/i);
                    if (match) return match[0];
                    var rel = text.match(/[^\\"'<>\s]+\.m3u8[^\\"'<>\s]*/i);
                    return rel ? absolute(rel[0]) : null;
                }
                try {
                    if (window.jwplayer) {
                        var current = findM3u8(window.jwplayer().getPlaylist());
                        if (current) return current;
                        var item = window.jwplayer().getPlaylistItem && window.jwplayer().getPlaylistItem();
                        current = findM3u8(item);
                        if (current) return current;
                        var config = window.jwplayer().getConfig && window.jwplayer().getConfig();
                        current = findM3u8(config);
                        if (current) return current;
                    }
                } catch(e) {}
                try { return findM3u8(document.documentElement.outerHTML); } catch(e) { return null; }
            })()
        """.trimIndent()

        val resolver = WebViewResolver(
            interceptUrl = Regex("""__DRAMACOOL_XSTREAM_WV_NEVER_MATCH__"""),
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

    private fun fixRelativeUrl(url: String, baseUrl: String): String {
        return when {
            url.startsWith("http") -> url
            url.startsWith("//") -> "https:$url"
            url.startsWith("/") -> Regex("""https?://[^/]+""").find(baseUrl)?.value + url
            else -> "${baseUrl.substringBeforeLast("/")}/$url"
        }
    }
}

class Hanerix : XStreamHls() {
    override val name = "Hanerix"
    override val mainUrl = "https://hanerix.com"
}

class Hglink : XStreamHls() {
    override val name = "Hglink"
    override val mainUrl = "https://hglink.to"
}

class Minochinos : XStreamHls() {
    override val name = "Minochinos"
    override val mainUrl = "https://minochinos.com"
}

class WatchAdsOnTape : ExtractorApi() {
    override val name = "WatchAdsOnTape"
    override val mainUrl = "https://watchadsontape.com"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val html = app.get(url, referer = referer).text

        val videoUrl = extractStreamTapeUrl(html)
            ?: extractStreamTapeUrlByWebView(url, referer)
            ?: return

        callback.invoke(
            newExtractorLink(name, name, videoUrl, ExtractorLinkType.VIDEO) {
                this.referer = url
                this.quality = Qualities.Unknown.value
            }
        )
    }

    private fun extractStreamTapeUrl(html: String): String? {
        val assignmentRegex = Regex(
            """getElementById\s*\(\s*['"](?:robotlink|ideoolink|botlink)['"]\s*\)\s*\.innerHTML\s*=\s*(.+?)(?:;|\n)""",
            RegexOption.IGNORE_CASE
        )

        val assignments = assignmentRegex.findAll(html).toList()
        if (assignments.isEmpty()) return null

        val target = assignments.lastOrNull { match ->
            val fullMatch = match.value
            fullMatch.contains("ideoolink") || fullMatch.contains("botlink")
        } ?: assignments.last()

        val expr = target.groupValues[1].trim()
        val resolved = evaluateConcat(expr) ?: return null

        val path = resolved.replace("\\/", "/").trim()
        if (!path.contains("get_video") && !path.contains("/e/")) return null

        val fullUrl = when {
            path.startsWith("http") -> path
            path.startsWith("//") -> "https:$path"
            path.startsWith("/") -> "$mainUrl$path"
            else -> return null
        }

        return if (fullUrl.contains("&stream=")) fullUrl
        else if (fullUrl.contains("?")) "$fullUrl&stream=1"
        else "$fullUrl?stream=1"
    }

    private fun evaluateConcat(expr: String): String? {
        val result = StringBuilder()
        val parts = expr.split("+")

        for (part in parts) {
            val trimmed = part.trim()
            if (trimmed.isEmpty()) continue

            val substringPattern = Regex("""\(\s*['"](.+?)['"]\s*\)((?:\s*\.substring\s*\(\s*\d+\s*\))+)""")
            val match = substringPattern.find(trimmed)

            if (match != null) {
                var value = match.groupValues[1]
                val substringCalls = Regex("""\.substring\s*\(\s*(\d+)\s*\)""").findAll(match.groupValues[2])
                for (sub in substringCalls) {
                    val idx = sub.groupValues[1].toIntOrNull() ?: continue
                    if (idx < value.length) value = value.substring(idx)
                }
                result.append(value)
            } else {
                val literal = trimmed
                    .removeSurrounding("'")
                    .removeSurrounding("\"")
                    .replace("\\\"", "\"")
                    .replace("\\'", "'")
                if (literal != trimmed || trimmed.startsWith("'") || trimmed.startsWith("\"")) {
                    result.append(literal)
                }
            }
        }

        return result.toString().takeIf { it.isNotBlank() }
    }

    private suspend fun extractStreamTapeUrlByWebView(url: String, referer: String?): String? {
        val direct = AtomicReference<String?>(null)
        val script = """
            (function() {
                try {
                    var bot = document.getElementById('botlink');
                    if (bot) {
                        var text = bot.textContent || bot.innerText || '';
                        if (text && text.indexOf('get_video') > -1) {
                            var src = text.trim();
                            if (src.indexOf('//') === 0) src = 'https:' + src;
                            if (src.indexOf('&stream=') === -1) src += '&stream=1';
                            return src;
                        }
                    }
                } catch(e) {}
                try {
                    var ideo = document.getElementById('ideoolink');
                    if (ideo) {
                        var text = ideo.textContent || ideo.innerText || '';
                        if (text && text.indexOf('get_video') > -1) {
                            var src = text.trim();
                            if (src.indexOf('//') === 0) src = 'https:' + src;
                            if (src.indexOf('&stream=') === -1) src += '&stream=1';
                            return src;
                        }
                    }
                } catch(e) {}
                try {
                    var video = document.querySelector('video');
                    if (video && video.src) return video.src;
                } catch(e) {}
                return null;
            })()
        """.trimIndent()

        val resolver = WebViewResolver(
            interceptUrl = Regex("""__DRAMACOOL_ST_WV_NEVER_MATCH__"""),
            additionalUrls = listOf(Regex(""".*""")),
            useOkhttp = false,
            script = script,
            scriptCallback = { result ->
                val value = result?.trim()?.trim('"')?.replace("\\/", "/")
                if (!value.isNullOrBlank() && value.startsWith("http")) direct.compareAndSet(null, value)
            },
            timeout = 20_000L
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

open class DramacoolMixDrop : MixDrop() {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer ?: mainUrl)
        val scriptText = response.document.select("script").joinToString("\n") { script ->
            val data = script.data()
            if (data.contains("eval(function(p,a,c,k,e,d)")) runCatching { getAndUnpack(data) }.getOrDefault(data) else data
        }

        val direct = extractMdCoreUrl(scriptText, "wurl")
            ?: extractMdCoreUrl(scriptText, "furl")
            ?: Regex("""https?://[^"'<>\s]+\.mp4[^"'<>\s]*""", RegexOption.IGNORE_CASE).find(scriptText)?.value
            ?: Regex("""["'](//[^"'<>\s]+\.mp4[^"'<>\s]*)["']""", RegexOption.IGNORE_CASE).find(scriptText)?.groupValues?.getOrNull(1)?.let { "https:$it" }
            ?: return

        callback.invoke(
            newExtractorLink(name, name, direct.replace("\\/", "/"), ExtractorLinkType.VIDEO) {
                this.referer = url
                this.quality = Qualities.Unknown.value
            }
        )
    }

    private fun extractMdCoreUrl(text: String, key: String): String? {
        val value = Regex("""MDCore\.$key\s*=\s*["']([^"']+)["']""", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.getOrNull(1)
            ?.replace("\\/", "/")
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: return null
        return when {
            value.startsWith("http") -> value
            value.startsWith("//") -> "https:$value"
            value.startsWith("/") -> "$mainUrl$value"
            else -> null
        }
    }
}

class M1xDrop : DramacoolMixDrop() {
    override var name = "M1xDrop"
    override var mainUrl = "https://m1xdrop.bz"
}

class MixDropPs : DramacoolMixDrop() {
    override var name = "MixDropPs"
    override var mainUrl = "https://mixdrop.ps"
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
                    var match = text.match(/https?:[^\\"'<>\\s]+\.(?:m3u8|mp4)[^\\"'<>\\s]*/i);
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

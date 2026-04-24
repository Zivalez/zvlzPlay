package com.kingv2

import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper

open class Extractor : ExtractorApi() {
    override val name = "Stream18"
    override val mainUrl = "https://stream18.net"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        try {
            val req = app.get(url, referer = referer ?: mainUrl)
            val html = req.text

            val m3u8Regex = Regex("https?://[^\"'\\s>]+\\.m3u8[^\"'\\s>]*", RegexOption.IGNORE_CASE)
            m3u8Regex.find(html)?.value?.let { playlist ->
                M3u8Helper.generateM3u8(name, playlist, url).forEach(callback)
                return
            }

            // direct hlsplaylist / hlsnew2 endpoints
            Regex("(https?://[^\"']*hls(?:playlist|new2)\\.php[^\"']*)", RegexOption.IGNORE_CASE).find(html)?.groupValues?.get(1)?.let { p ->
                M3u8Helper.generateM3u8(name, p, url).forEach(callback)
                return
            }

            // atob(...) simple base64 patterns
            Regex("atob\\(['\"]([^'\"]+)['\"]\\)", RegexOption.IGNORE_CASE).find(html)?.groupValues?.get(1)?.let { b64 ->
                val decoded = runCatching { com.lagradost.cloudstream3.base64Decode(b64) }.getOrNull()
                if (!decoded.isNullOrBlank()) {
                    m3u8Regex.find(decoded)?.value?.let { pl -> M3u8Helper.generateM3u8(name, pl, url).forEach(callback); return }
                    Regex("(https?://[^\"']*hls(?:playlist|new2)\\.php[^\"']*)", RegexOption.IGNORE_CASE).find(decoded)?.groupValues?.get(1)?.let { p2 ->
                        M3u8Helper.generateM3u8(name, p2, url).forEach(callback)
                        return
                    }
                }
            }

            // decode ?link= param if present
            Regex("[?&]link=([^&]+)").find(url)?.groupValues?.get(1)?.let { raw ->
                var dec = java.net.URLDecoder.decode(raw, "UTF-8")
                // try repeated base64 decodes
                for (i in 0..3) {
                    val attempt = runCatching { com.lagradost.cloudstream3.base64Decode(dec) }.getOrNull() ?: break
                    if (attempt.isNotBlank()) dec = attempt else break
                }
                m3u8Regex.find(dec)?.value?.let { pl -> M3u8Helper.generateM3u8(name, pl, url).forEach(callback); return }
                Regex("(https?://[^\"']*hls(?:playlist|new2)\\.php[^\"']*)", RegexOption.IGNORE_CASE).find(dec)?.groupValues?.get(1)?.let { p3 ->
                    M3u8Helper.generateM3u8(name, p3, url).forEach(callback)
                    return
                }
            }

            // try to find playerzyetsa-like arrays
            Regex("playerzyetsa[^=]*=\\s*(\\[[^\\]]+\\])", RegexOption.IGNORE_CASE).find(html)?.groupValues?.get(1)?.let { arr ->
                m3u8Regex.find(arr)?.value?.let { pl -> M3u8Helper.generateM3u8(name, pl, url).forEach(callback); return }
            }

        } catch (_: Exception) {}
    }
}

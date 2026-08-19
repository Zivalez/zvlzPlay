package com.nontonanimeid

import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.base64Decode
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink

/**
 * Extractor untuk kotakanimeid.link (s1/s2) — host utama Nontonanimeid.
 * Halaman embed meng-obfuscate setup jwplayer dengan XOR:
 *   var KEY=[int,...]; var DATA=atob("<base64>"); ... charCodeAt(i)^KEY[i%len]
 * Hasil decode berisi JSON jwplayer: sources:[{"file":"https://...video/mp4","label":"720p"},...]
 */
open class KotakAnimeid : ExtractorApi() {
    override var name = "KotakAnimeid"
    override var mainUrl = "https://s2.kotakanimeid.link"
    override val requiresReferer = true

    private fun xorDecode(base64: String, key: IntArray): String {
        val bytes = base64Decode(base64).toByteArray(Charsets.ISO_8859_1)
        val sb = StringBuilder(bytes.size)
        for (i in bytes.indices) {
            sb.append(((bytes[i].toInt() and 0xFF) xor key[i % key.size]).toChar())
        }
        return sb.toString()
    }

    private suspend fun extractVideoLinks(text: String, sourceLabel: String, referer: String, callback: (ExtractorLink) -> Unit) {
        // tangkap {"file":"...","label":"720p"} atau file:"..." dengan escaped slash
        val fileRegex = Regex(""""file"\s*:\s*"((?:[^"\\]|\\.)*)"(?:\s*,\s*"type"\s*:\s*"((?:[^"\\]|\\.)*)")?(?:\s*,\s*"label"\s*:\s*"((?:[^"\\]|\\.)*)")?""")
        fileRegex.findAll(text).forEach { m ->
            val rawUrl = m.groupValues[1].replace("\\/", "/")
            if (rawUrl.isBlank() || !rawUrl.startsWith("http")) return@forEach
            val label = m.groupValues[3].ifBlank { null }
            val quality = label?.replace(Regex("[^0-9]"), "")?.toIntOrNull() ?: Qualities.Unknown.value
            val isM3u8 = rawUrl.contains("m3u8") || m.groupValues[2].contains("mpegurl", true)
            callback(
                newExtractorLink(
                    this.name,
                    "$name [$sourceLabel${label?.let { " $it" } ?: ""}]",
                    rawUrl,
                    if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                ) {
                    this.referer = referer
                    this.quality = quality
                }
            )
        }
    }

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val ref = referer ?: mainUrl
        val body = app.get(url, referer = ref).text

        // 1) coba XOR-obfuscated blocks: var KEY=[..]; var DATA=atob("..")
        val blockRegex = Regex("""var\s+\w+\s*=\s*\[((?:\d+\s*,\s*)*\d+)\]\s*;\s*var\s+\w+\s*=\s*atob\("([^"]+)"\)""")
        var emitted = false
        blockRegex.findAll(body).forEach { m ->
            val key = m.groupValues[1].split(",").mapNotNull { it.trim().toIntOrNull() }.toIntArray()
            if (key.isEmpty()) return@forEach
            val decoded = runCatching { xorDecode(m.groupValues[2], key) }.getOrNull() ?: return@forEach
            if (decoded.contains("sources") || decoded.contains("\"file\"")) {
                extractVideoLinks(decoded, "Kotak", url, callback)
                emitted = true
            }
        }

        // 2) fallback: plaintext jwplayer / file di HTML
        if (!emitted) {
            extractVideoLinks(body, "Kotak", url, callback)
        }
    }
}

class KotakAnimeidS1 : KotakAnimeid() {
    override var mainUrl = "https://s1.kotakanimeid.link"
}

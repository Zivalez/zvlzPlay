package com.gomunime

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.amap
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.runBlocking
import org.jsoup.nodes.Document

suspend fun loadGomunimeLinks(
    data: String,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
): Boolean {
    val document = app.get(data).document
    val options = document.select("select.mirror option")
        .mapNotNull { option ->
            val name = option.text().trim()
            val value = option.attr("value").trim()
            if (name.isBlank() || value.isBlank()) return@mapNotNull null
            val iframeUrl = decodeIframeUrl(value) ?: return@mapNotNull null
            Gomunime.ServerOption(name = name, url = iframeUrl)
        }

    options.amap { server ->
        loadServerSource(server, data, subtitleCallback, callback)
    }
    return true
}

private suspend fun loadServerSource(
    server: Gomunime.ServerOption,
    referer: String,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
) {
    val direct = runCatching {
        app.get(server.url, referer = referer).document.findDirectVideoSource()
    }.getOrNull()

    if (!direct.isNullOrBlank()) {
        callback(
            newExtractorLink(server.name, server.name, direct) {
                this.referer = server.url
                this.quality = qualityFromUrl(direct)
            }
        )
        return
    }

    loadExtractor(server.url, referer, subtitleCallback) { link ->
        runBlocking {
            callback.invoke(
                newExtractorLink(
                    server.name,
                    link.name.ifBlank { server.name },
                    link.url,
                    link.type
                ) {
                    this.referer = link.referer
                    this.quality = link.quality
                    this.headers = link.headers
                    this.extractorData = link.extractorData
                }
            )
        }
    }
}

private fun decodeIframeUrl(encoded: String): String? {
    val html = runCatching { base64Decode(encoded) }.getOrNull()?.takeIf { it.isNotBlank() } ?: return null
    return Regex("""src="([^"]+)"""").find(html)?.groupValues?.getOrNull(1)?.takeIf { it.isNotBlank() }
}

private fun Document.findDirectVideoSource(): String? {
    val candidates = sequenceOf(
        selectFirst("video source[src]")?.attr("src"),
        selectFirst("source[src*='googlevideo']")?.attr("src"),
        selectFirst("source[src*='.mp4']")?.attr("src"),
        Regex("""https://[^"' ]+googlevideo\.com/videoplayback[^"' ]*""")
            .find(html())?.value,
    )

    return candidates.firstOrNull { !it.isNullOrBlank() }?.trim()
}

private fun qualityFromUrl(url: String): Int = when (Regex("""itag=(\d+)""").find(url)?.groupValues?.getOrNull(1)) {
    "37", "96", "137" -> Qualities.P1080.value
    "22", "59" -> Qualities.P720.value
    "18" -> Qualities.P360.value
    else -> Qualities.Unknown.value
}

package com.gomunime

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.amap
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
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

    val iframes = document.select("iframe").mapNotNull {
        val src = it.attr("src").ifBlank { it.attr("data-src") }.trim()
        if (src.isBlank()) null else fixUrl(src)
    }.distinct()

    val options = document.select("select.mirror option, div.server option, ul.servers li a")
        .mapNotNull { option ->
            val name = option.text().trim()
            val value = option.attr("value").ifBlank { option.attr("href") }.ifBlank { option.attr("data-video") }.trim()
            if (name.isBlank() || value.isBlank()) return@mapNotNull null
            val iframeUrl = decodeIframeUrl(value) ?: if (value.startsWith("http") || value.startsWith("//")) fixUrl(value) else null
            if (iframeUrl != null) Gomunime.ServerOption(name = name, url = iframeUrl) else null
        }

    val allUrls = (iframes.map { Gomunime.ServerOption("Gomunime", it) } + options).distinctBy { it.url }

    allUrls.amap { server ->
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
    val fixedUrl = if (server.url.startsWith("//")) "https:${server.url}" else server.url

    loadExtractor(fixedUrl, referer, subtitleCallback) { link ->
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
    if (encoded.startsWith("http") || encoded.startsWith("//")) return encoded
    val html = runCatching { base64Decode(encoded) }.getOrNull()?.takeIf { it.isNotBlank() } ?: return null
    return Regex("""src=["']([^"']+)["']""").find(html)?.groupValues?.getOrNull(1)?.takeIf { it.isNotBlank() }
}

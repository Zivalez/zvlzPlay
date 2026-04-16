package com.kuronime

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.extractors.helper.AesHelper
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.nicehttp.RequestBodyTypes
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URI

suspend fun loadKuronimeLinks(
    data: String,
    animekuUrl: String,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
): Boolean {
    val document = app.get(data).document
    val id = document.selectFirst("div#content script:containsData(is_singular)")?.data()
        ?.substringAfter("_0xa100d42aa = \"")?.substringBefore("\";")
        ?: throw ErrorLoadingException("No id found")

    val servers = app.post(
        "$animekuUrl/api/v9/sources",
        requestBody = """{"id":"$id"}""".toRequestBody(RequestBodyTypes.JSON.toMediaTypeOrNull()),
        referer = "$animekuUrl/"
    ).parsedSafe<Kuronime.Servers>()

    runAllAsync(
        {
            val decrypt = AesHelper.cryptoAESHandler(
                base64Decode(servers?.src ?: return@runAllAsync),
                Kuronime.KEY.toByteArray(),
                false,
                "AES/CBC/NoPadding"
            )
            val source = tryParseJson<Kuronime.Sources>(decrypt?.toJsonFormat())?.src?.replace("\\", "")
            val sourceName = resolveSourceName(source)
            M3u8Helper.generateM3u8(
                sourceName,
                source ?: return@runAllAsync,
                "$animekuUrl/",
                headers = mapOf("Origin" to animekuUrl)
            ).forEach(callback)
        },
        {
            val decrypt = AesHelper.cryptoAESHandler(
                base64Decode(servers?.mirror ?: return@runAllAsync),
                Kuronime.KEY.toByteArray(),
                false,
                "AES/CBC/NoPadding"
            )
            tryParseJson<Kuronime.Mirrors>(decrypt)?.embed?.forEach { (qualityLabel, sources) ->
                sources.forEach { (_, url) ->
                    loadFixedExtractor(
                        url = url,
                        quality = qualityLabel.removePrefix("v"),
                        sourceName = resolveSourceName(url, qualityLabel),
                        referer = "$animekuUrl/",
                        subtitleCallback = subtitleCallback,
                        callback = callback
                    )
                }
            }
        }
    )

    return true
}

private suspend fun loadFixedExtractor(
    url: String?,
    quality: String?,
    sourceName: String?,
    referer: String?,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
) {
    loadExtractor(url ?: return, referer, subtitleCallback) { link ->
        callback(
            newExtractorLink(
                sourceName ?: link.source,
                link.name.ifBlank { sourceName ?: link.source },
                link.url,
                link.type
            ) {
                this.referer = link.referer
                this.quality = quality?.fixQuality() ?: link.quality
                this.headers = link.headers
                this.extractorData = link.extractorData
            }
        )
    }
}

private fun resolveSourceName(url: String?, hint: String? = null): String {
    hint?.toSourceLabel()?.let { return it }

    val host = runCatching { url?.let { URI(it).host } }.getOrNull()
        ?.lowercase()
        ?.removePrefix("www.")
        ?.takeIf { it.isNotBlank() }

    host?.toSourceLabel()?.let { return it }

    return hint?.toSourceLabel() ?: "Kuronime"
}

private fun String.toSourceLabel(): String? {
    val cleaned = lowercase().replace(Regex("[^a-z0-9]+"), " ").trim()
    if (cleaned.isBlank() || cleaned.matches(Regex("v\\d+")) || cleaned.all { it.isDigit() }) {
        return null
    }

    return when {
        cleaned.contains("pixeldrain") -> "Pixeldrain"
        cleaned == "vip" || cleaned.contains(" vip ") || cleaned.startsWith("vip ") || cleaned.endsWith(" vip") -> "VIP"
        cleaned.contains("mega") -> "Mega"
        cleaned.contains("filemoon") -> "Filemoon"
        cleaned.contains("streamwish") -> "Streamwish"
        cleaned.contains("tune pk") || cleaned.contains("tunepk") -> "Tune.pk"
        cleaned.contains("dood") -> "DoodStream"
        else -> cleaned.split(Regex("\\s+")).joinToString(" ") {
            it.replaceFirstChar { ch -> ch.uppercase() }
        }
    }
}

private fun String.fixQuality(): Int = when (uppercase()) {
    "4K" -> Qualities.P2160.value
    "FULLHD" -> Qualities.P1080.value
    "MP4HD" -> Qualities.P720.value
    else -> filter { it.isDigit() }.toIntOrNull() ?: Qualities.Unknown.value
}

private fun String.toJsonFormat(): String {
    return if (this.startsWith("\"")) this.substringAfter("\"").substringBeforeLast("\"")
        .replace("\\\"", "\"") else this
}

package com.layarkaca

import android.util.Base64
import com.lagradost.api.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.extractors.Filesim
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.json.JSONObject
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random


class Co4nxtrl : Filesim() {
    override val mainUrl = "https://co4nxtrl.com"
    override val name = "Co4nxtrl"
    override val requiresReferer = true
}

open class Hownetwork : ExtractorApi() {
    override val name = "Hownetwork"
    override val mainUrl = "https://cloud.hownetwork.xyz"
    override val requiresReferer = true

    override suspend fun getUrl(
            url: String,
            referer: String?,
            subtitleCallback: (SubtitleFile) -> Unit,
            callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfter("id=", "").substringBefore("&").ifBlank {
            url.substringAfterLast("/", "")
        }
        if (id.isBlank()) return

        val postHeaders = mapOf(
            "X-Requested-With" to "XMLHttpRequest",
            "Referer" to (referer ?: url),
            "Origin" to mainUrl
        )

        val payloads = listOf(
            mapOf("r" to "https://playeriframe.sbs/", "d" to "cloud.hownetwork.xyz"),
            mapOf("r" to "", "d" to "stream.hownetwork.xyz")
        )

        val apiUrls = listOf(
            "$mainUrl/api2.php?id=$id",
            "$mainUrl/api.php?id=$id"
        )

        var hasEmitted = false

        apiUrls.forEach { api ->
            payloads.forEach { form ->
                val response = app.post(
                    api,
                    data = form,
                    headers = postHeaders,
                    referer = referer ?: url
                ).text

                val direct = runCatching {
                    JSONObject(response).optString("file")
                        .ifBlank { JSONObject(response).optString("link") }
                }.getOrDefault("")
                if (direct.isNotBlank()) {
                    hasEmitted = true
                    M3u8Helper.generateM3u8(this.name, direct, mainUrl).forEach(callback)
                }

                val parsed = tryParseJson<HownetworkSources>(response)
                parsed?.data?.forEach {
                    if (it.file.isNotBlank()) {
                        hasEmitted = true
                        callback.invoke(
                            newExtractorLink(this.name, this.name, it.file) {
                                this.referer = referer ?: url
                                this.quality = getQualityFromName(it.label)
                            }
                        )
                    }
                }
                if (hasEmitted) return
            }
        }
        Log.d("LayarKaca", "Hownetwork failed for: $url")
    }

    data class HownetworkSources(val data: ArrayList<Item>) {
        data class Item(
            val file: String,
            val label: String?,
        )
    }
}

class Furher : Filesim() {
    override val name = "Furher"
    override var mainUrl = "https://furher.in"
}

class Cloudhownetwork : Hownetwork() {
    override var mainUrl = "https://cloud.hownetwork.xyz"
}

class Playeriframe : ExtractorApi() {
    override val name = "Playeriframe"
    override val mainUrl = "https://playeriframe.sbs"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfterLast("/", "")
        if (id.isBlank()) return

        val api = "https://cloud.hownetwork.xyz"
        val response = app.post(
            "$api/api2.php?id=$id",
            data = mapOf(
                "r" to "https://playeriframe.sbs/",
                "d" to "cloud.hownetwork.xyz"
            ),
            headers = mapOf("X-Requested-With" to "XMLHttpRequest"),
            referer = referer ?: url
        ).text

        val direct = runCatching {
            JSONObject(response).optString("file")
                .ifBlank { JSONObject(response).optString("link") }
        }.getOrDefault("")

        if (direct.isNotBlank()) {
            M3u8Helper.generateM3u8(this.name, direct, api).forEach(callback)
        }
    }
}

class Furher2 : Filesim() {
    override val name = "Furher 2"
    override var mainUrl = "723qrh1p.fun"
}

class Turbovidhls : Filesim() {
    override val name = "Turbovidhls"
    override var mainUrl = "https://turbovidhls.com"
}

open class F16px : ExtractorApi() {
    override val name = "F16"
    override val mainUrl = "https://f16px.com"
    override val requiresReferer = false

    private data class PlaybackEnvelope(
        val playback: Playback?
    )

    private data class Playback(
        val iv: String?,
        val payload: String?,
        val key_parts: List<String>?
    )

    private data class SourceEnvelope(
        val sources: List<SourceItem>?
    )

    private data class SourceItem(
        val url: String?,
        val label: String?
    )

    private fun randomHex(length: Int): String {
        val chars = "0123456789abcdef"
        return (1..length).joinToString("") { chars[Random.nextInt(chars.length)].toString() }
    }

    private fun String.fixBase64(): String {
        var out = this
        while (out.length % 4 != 0) out += "="
        return out
    }

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfter("/e/", "").substringBefore("?")
        if (id.isBlank()) return

        val viewerId = randomHex(32)
        val deviceId = randomHex(32)
        val timestamp = System.currentTimeMillis() / 1000
        val jwtPayload =
            """{"viewer_id":"$viewerId","device_id":"$deviceId","confidence":0.91,"iat":$timestamp,"exp":${timestamp + 600}}"""
        val jwtPayloadEncoded = Base64.encodeToString(
            jwtPayload.toByteArray(),
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.$jwtPayloadEncoded.${randomHex(43)}"

        val responseText = app.post(
            "$mainUrl/api/videos/$id/embed/playback",
            headers = mapOf(
                "User-Agent" to "Mozilla/5.0",
                "Referer" to "$mainUrl/e/$id",
                "Origin" to mainUrl,
                "Content-Type" to "application/json",
                "x-embed-origin" to "playeriframe.sbs",
                "x-embed-parent" to "$mainUrl/e/$id",
                "x-embed-referer" to "https://playeriframe.sbs/"
            ),
            json = mapOf(
                "fingerprint" to mapOf(
                    "token" to token,
                    "viewer_id" to viewerId,
                    "device_id" to deviceId,
                    "confidence" to 0.91
                )
            )
        ).text

        val playback = tryParseJson<PlaybackEnvelope>(responseText)?.playback ?: return
        val payload = playback.payload ?: return
        val iv = playback.iv ?: return
        val keyParts = playback.key_parts ?: return
        if (keyParts.size < 2) return

        val part1 = Base64.decode(keyParts[0].fixBase64(), Base64.URL_SAFE)
        val part2 = Base64.decode(keyParts[1].fixBase64(), Base64.URL_SAFE)
        val decrypted = decryptAesGcm(payload, part1 + part2, iv) ?: return

        tryParseJson<SourceEnvelope>(decrypted)?.sources?.forEach { source ->
            val streamUrl = source.url ?: return@forEach
            callback.invoke(
                newExtractorLink("CAST", "CAST ${source.label ?: "Auto"}", streamUrl) {
                    this.referer = "$mainUrl/"
                    this.quality = getQualityFromName(source.label)
                }
            )
        }
    }

    private fun decryptAesGcm(payload: String, key: ByteArray, ivInput: String): String? {
        return try {
            val iv = Base64.decode(ivInput.fixBase64(), Base64.URL_SAFE)
            val encrypted = Base64.decode(payload.fixBase64(), Base64.URL_SAFE)
            val spec = GCMParameterSpec(128, iv)
            val keySpec = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec)
            String(cipher.doFinal(encrypted), Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e("LayarKaca", "F16 decrypt failed: ${e.message}")
            null
        }
    }
}

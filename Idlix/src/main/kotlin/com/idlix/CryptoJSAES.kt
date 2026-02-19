package com.idlix

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

data class EncryptedData(
        @JsonProperty("ct") val ct: String,
        @JsonProperty("iv") val iv: String,
        @JsonProperty("s") val s: String
)

object CryptoJsAes {
    private val objectMapper = ObjectMapper()

    fun encrypt(value: Any, passphrase: String): String {
        val salt = ByteArray(8)
        Random().nextBytes(salt)
        var salted = ByteArray(0)
        var dx = ByteArray(0)
        while (salted.size < 48) {
            dx = MessageDigest.getInstance("MD5").digest(dx + passphrase.toByteArray() + salt)
            salted += dx
        }
        val key = salted.copyOfRange(0, 32)
        val iv = salted.copyOfRange(32, 48)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
        val encryptedData = cipher.doFinal(objectMapper.writeValueAsBytes(value))

        val encryptedDataObj =
                EncryptedData(
                        ct = Base64.getEncoder().encodeToString(encryptedData),
                        iv = iv.toHex(),
                        s = salt.toHex()
                )

        return objectMapper.writeValueAsString(encryptedDataObj)
    }

    fun decrypt(jsonStr: String, passphrase: String): String? {
        val jsonData = objectMapper.readValue(jsonStr, EncryptedData::class.java)
        val salt = jsonData.s.hexToByteArray()
        val iv = jsonData.iv.hexToByteArray()
        val ct = Base64.getDecoder().decode(jsonData.ct)

        val concatedPassphrase = passphrase.toByteArray() + salt
        var result = MessageDigest.getInstance("MD5").digest(concatedPassphrase)
        repeat(2) { result += MessageDigest.getInstance("MD5").digest(result + concatedPassphrase) }
        val key = result.copyOfRange(0, 32)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
        val decryptedData = cipher.doFinal(ct)

        return try {
            String(decryptedData)
        } catch (e: Exception) {
            println("Error decoding JSON: ${e.message}")
            null
        }
    }

    private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
    private fun String.hexToByteArray(): ByteArray =
            chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}

fun addBase64Padding(b64String: String): String =
        b64String + "=".repeat((4 - b64String.length % 4) % 4)

fun dec(r: String, e: String): String {
    val rList = r.chunked(2).filterIndexed { index, _ -> index % 2 == 0 }
    val mPadded = addBase64Padding(e.reversed())
    val decodedM =
            try {
                String(Base64.getDecoder().decode(mPadded))
            } catch (e: IllegalArgumentException) {
                println("Base64 decoding error: ${e.message}")
                return ""
            }

    return decodedM.split("|")
            .filter { it.toIntOrNull() != null && it.toInt() < rList.size }
            .joinToString("") { "\\x${rList[it.toInt()]}" }
}

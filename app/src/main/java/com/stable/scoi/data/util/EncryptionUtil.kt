package com.stable.scoi.data.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {
    // 요구사항: AES 암호화, CBC 모드, PKCS#5 패딩
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val ALGORITHM = "AES"

    // 카톡으로 공유받은 16바이트(128비트) 키
    private const val KEY = "1ejvoqwxgoqkjgjc"

    // 키와 IV를 바이트 배열로 미리 준비 (UTF-8 명시)
    private val keyBytes = KEY.toByteArray(Charsets.UTF_8)
    private val ivSpec = IvParameterSpec(keyBytes) // IV도 동일한 16바이트 키 사용
    private val keySpec = SecretKeySpec(keyBytes, ALGORITHM)

    /**
     * 암호화: 평문 -> AES 암호화 -> Base64 인코딩
     */
    fun encrypt(plainText: String?): String {
        if (plainText.isNullOrEmpty()) return ""

        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            // Base64 인코딩 (URL 전송 및 JSON 포함 시 안전하게 NO_WRAP 사용)
            Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            android.util.Log.e("ENCRYPT_ERROR", "암호화 실패: ${e.message}")
            ""
        }
    }

    /**
     * 복호화: Base64 디코딩 -> AES 복호화 -> 평문
     */
    fun decrypt(encryptedText: String?): String {
        if (encryptedText.isNullOrEmpty()) return ""

        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

            // Base64 디코딩
            val decodedBytes = Base64.decode(encryptedText, Base64.NO_WRAP)
            val decryptedBytes = cipher.doFinal(decodedBytes)

            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            android.util.Log.e("DECRYPT_ERROR", "복호화 실패: ${e.message}")
            ""
        }
    }
}
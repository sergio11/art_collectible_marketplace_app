package com.dreamsoftware.artcollectibles.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.*
import android.util.Base64
import android.util.Log
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoUtils {

    private companion object {
        const val SECRET_KEY_ALIAS = "ART_COLLECTIBLE_USER_SECRET"
        const val transformation = "AES/CBC/PKCS7Padding"
    }

    private val charset by lazy {
        charset("UTF-8")
    }

    /**
     * Encrypt And Encode as Base64
     * @param key
     * @param data
     */
    fun encryptAndEncode(key: String, data: String): String =
        String(Base64.encode(encrypt(key, data.toByteArray()), Base64.DEFAULT), charset)

    /**
     * Decode and Decrypt
     * @param key
     * @param data
     */
    fun decodeAndDecrypt(key: String, data: String): String =
        String(decrypt(key, Base64.decode(data, Base64.DEFAULT)), charset)

    /**
     * Generate Secret Key
     */
    fun generateSecretKey(): String {
        val keyGenParameters = KeyGenParameterSpec.Builder(SECRET_KEY_ALIAS, PURPOSE_ENCRYPT or PURPOSE_DECRYPT).run {
            setBlockModes(BLOCK_MODE_CBC)
            setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
            setRandomizedEncryptionRequired(true)
            build()
        }
        val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM_AES).also {
            it.init(keyGenParameters)
        }

        val key = keyGenerator.generateKey()



        Log.d("ART_COLL", "Provider -> ${keyGenerator.provider.name}")

        Log.d("ART_COLL", "key.encoded -> ${key.encoded}")
        Log.d("ART_COLL", "key.format -> ${key.format}")
        Log.d("ART_COLL", "key.algorithm -> ${key.algorithm}")
        return String(Base64.encode(key.encoded, Base64.DEFAULT), charset)
    }
    /**
     * Private Methods
     */

    /**
     * Encrypt data
     * @param data
     */
    private fun encrypt(key: String, data: ByteArray): ByteArray {
        val cipher = getCipher(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher.doFinal(data)
        return encrypted + cipher.iv
    }

    /**
     * Decrypt
     * @param data
     */
    private fun decrypt(key: String, data: ByteArray): ByteArray {
        val ivSize = 16
        val encryptedData = data.sliceArray(0 until data.count()-ivSize)
        val iv = data.sliceArray((data.count()-ivSize) until data.count())
        val cipher = getCipher(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        return cipher.doFinal(encryptedData)
    }

    /**
     * Get Cipher
     * @param mode
     * @param params
     */
    private fun getCipher(mode: Int, key: String, params: AlgorithmParameterSpec? = null): Cipher=
        Cipher.getInstance(transformation).also {
            it.init(mode, SecretKeySpec(Base64.decode(key, Base64.DEFAULT), transformation), params)
        }
}
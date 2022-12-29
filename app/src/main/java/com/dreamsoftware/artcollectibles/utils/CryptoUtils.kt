package com.dreamsoftware.artcollectibles.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.*
import android.util.Base64
import java.security.KeyStore
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoUtils {

    private companion object {
        const val SECRET_KEY_ALIAS = "ART_COLLECTIBLE_SECRET_KEY_ALIAS"
        const val provider = "AndroidKeyStore"
        const val transformation = "AES/CBC/PKCS7Padding"
    }

    private val charset by lazy {
        charset("UTF-8")
    }
    private val keyStore by lazy {
        KeyStore.getInstance(provider).apply {
            load(null)
        }
    }

    /**
     * Encrypt And Encode as Base64
     * @param data
     */
    fun encryptAndEncode(data: String): String =
        String(Base64.encode(encrypt(data.toByteArray()), Base64.DEFAULT), charset)

    /**
     * Decode and Decrypt
     * @param data
     */
    fun decodeAndDecrypt(data: String): String =
        String(decrypt(Base64.decode(data, Base64.DEFAULT)), charset)

    /**
     * Private Methods
     */

    /**
     * Encrypt data
     * @param data
     */
    private fun encrypt(data: ByteArray): ByteArray {
        val cipher = getCipher(Cipher.ENCRYPT_MODE)
        val encrypted = cipher.doFinal(data)
        return encrypted + cipher.iv
    }

    /**
     * Decrypt
     * @param data
     */
    private fun decrypt(data: ByteArray): ByteArray {
        val ivSize = 16
        val encryptedData = data.sliceArray(0 until data.count()-ivSize)
        val iv = data.sliceArray((data.count()-ivSize) until data.count())
        val cipher = getCipher(Cipher.DECRYPT_MODE, IvParameterSpec(iv))
        return cipher.doFinal(encryptedData)
    }

    /**
     * Get Cipher
     * @param mode
     * @param params
     */
    private fun getCipher(mode: Int, params: AlgorithmParameterSpec? = null): Cipher=
        Cipher.getInstance(transformation).also {
            it.init(mode, getSecretKey() ?: generateSecretKey(), params)
        }

    private fun generateSecretKey(): SecretKey {
        val keyGenParameters = KeyGenParameterSpec.Builder(SECRET_KEY_ALIAS, PURPOSE_ENCRYPT or PURPOSE_DECRYPT).run {
            setBlockModes(BLOCK_MODE_CBC)
            setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
            setRandomizedEncryptionRequired(true)
            build()
        }
        val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM_AES, provider).also {
            it.init(keyGenParameters)
        }
        return keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey? {
        val secretKeyEntry = keyStore.getEntry(SECRET_KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return secretKeyEntry?.secretKey
    }
}
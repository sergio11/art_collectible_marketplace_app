package com.dreamsoftware.artcollectibles.utils

import android.util.Base64
import java.security.NoSuchAlgorithmException
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.InvalidKeySpecException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class CryptoUtils {

    private companion object {
        const val SECRET_KEY_ALIAS = "ART_COLLECTIBLE_USER_SECRET"
        const val SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256"
        const val TRANSFORMATION = "AES/CFB/PKCS5Padding"
        const val ALGORITHM = "AES"
        const val ITERATION_COUNT = 65536
        const val KEY_LENGTH = 256
        const val IV_SIZE = 16
    }

    private val charset by lazy {
        charset("UTF-8")
    }

    /**
     * Encrypt And Encode as Base64
     * @param password
     * @param salt
     * @param data
     */
    fun encryptAndEncode(password: String, salt: String, data: String): String =
        String(Base64.encode(encrypt(password, salt, data.toByteArray()), Base64.DEFAULT), charset)

    /**
     * Decode and Decrypt
     * @param password
     * @param salt
     * @param data
     */
    fun decodeAndDecrypt(password: String, salt: String, data: String): String =
        String(decrypt(password, salt, Base64.decode(data, Base64.DEFAULT)), charset)

    /**
     * Private Methods
     */

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getKeyFromPassword(password: String, salt: String): SecretKey {
        val factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM)
        val spec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), ITERATION_COUNT, KEY_LENGTH)
        return SecretKeySpec(factory.generateSecret(spec).encoded, ALGORITHM)
    }


    /**
     * Encrypt data
     * @param password
     * @param salt
     * @param data
     */
    private fun encrypt(password: String, salt: String, data: ByteArray): ByteArray {
        val cipher = getCipher(Cipher.ENCRYPT_MODE, password, salt)
        val encrypted = cipher.doFinal(data)
        return encrypted + cipher.iv
    }

    /**
     * Decrypt
     * @param password
     * @param salt
     * @param data
     */
    private fun decrypt(password: String, salt: String, data: ByteArray): ByteArray {
        val encryptedData = data.sliceArray(0 until data.count()-IV_SIZE)
        val iv = data.sliceArray((data.count()-IV_SIZE) until data.count())
        val cipher = getCipher(Cipher.DECRYPT_MODE, password, salt, IvParameterSpec(iv))
        return cipher.doFinal(encryptedData)
    }

    /**
     * Get Cipher
     * @param mode
     * @param password
     * @param salt
     * @param params
     */
    private fun getCipher(mode: Int, password: String, salt: String, params: AlgorithmParameterSpec? = null): Cipher=
        Cipher.getInstance(TRANSFORMATION).also {
            it.init(mode, getKeyFromPassword(password, salt), params)
        }
}
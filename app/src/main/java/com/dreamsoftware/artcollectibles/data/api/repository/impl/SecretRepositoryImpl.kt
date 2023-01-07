package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.SecretDataException
import com.dreamsoftware.artcollectibles.data.api.repository.ISecretRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ISecretDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO
import com.dreamsoftware.artcollectibles.utils.CryptoUtils

internal class SecretRepositoryImpl(
    private val secretDataSource: ISecretDataSource,
    private val cryptoUtils: CryptoUtils
) : ISecretRepository {

    @Throws(SecretDataException::class)
    override suspend fun generate(userUid: String): String = try {
        cryptoUtils.generateSecretKey().also {
            secretDataSource.save(SecretDTO(userUid, it))
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw SecretDataException("An error occurred when trying to save secret information", ex)
    }

    @Throws(SecretDataException::class)
    override suspend fun get(userUid: String): String = try {
        secretDataSource.getByUserUid(userUid).secret
    } catch (ex: Exception) {
        throw SecretDataException("An error occurred when trying to get secret information", ex)
    }
}
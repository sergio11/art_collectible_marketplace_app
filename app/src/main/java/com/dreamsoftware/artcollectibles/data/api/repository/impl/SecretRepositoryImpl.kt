package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.SecretDataException
import com.dreamsoftware.artcollectibles.data.api.mapper.PBEDataMapper
import com.dreamsoftware.artcollectibles.data.api.repository.ISecretRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ISecretDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO
import com.dreamsoftware.artcollectibles.domain.models.PBEData
import com.dreamsoftware.artcollectibles.utils.PasswordUtils

/**
 * Secret Repository Impl
 * @param secretDataSource
 * @param passwordUtils
 * @param PBEDataMapper
 */
internal class SecretRepositoryImpl(
    private val secretDataSource: ISecretDataSource,
    private val passwordUtils: PasswordUtils,
    private val PBEDataMapper: PBEDataMapper
) : ISecretRepository {

    private companion object {
        const val SECRET_LENGTH = 60
        const val SECRET_SALT_LENGTH = 20
    }

    @Throws(SecretDataException::class)
    override suspend fun generate(userUid: String): PBEData = try {
        val secret = passwordUtils.generatePassword(length = SECRET_LENGTH)
        val secretSalt = passwordUtils.generatePassword(length = SECRET_SALT_LENGTH)
        secretDataSource.save(SecretDTO(userUid, secret, secretSalt))
        PBEDataMapper.mapInToOut(secretDataSource.getByUserUid(userUid))
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw SecretDataException("An error occurred when trying to save secret information", ex)
    }

    @Throws(SecretDataException::class)
    override suspend fun get(userUid: String): PBEData = try {
        val secret = secretDataSource.getByUserUid(userUid)
        PBEDataMapper.mapInToOut(secret)
    } catch (ex: Exception) {
        throw SecretDataException("An error occurred when trying to get secret information", ex)
    }
}
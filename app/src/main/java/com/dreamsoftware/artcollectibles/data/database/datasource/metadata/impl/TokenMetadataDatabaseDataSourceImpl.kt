package com.dreamsoftware.artcollectibles.data.database.datasource.metadata.impl

import com.dreamsoftware.artcollectibles.data.database.datasource.metadata.ITokenMetadataDatabaseDataSource
import com.dreamsoftware.artcollectibles.data.database.exception.DBErrorException
import com.dreamsoftware.artcollectibles.data.database.exception.DBNoResultException
import com.dreamsoftware.artcollectibles.data.database.room.dao.metadata.ITokenMetadataDAO
import com.dreamsoftware.artcollectibles.data.database.room.entity.TokenMetadataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Token Metadata Database Data Source impl
 * @param tokenMetadataDAO
 */
internal class TokenMetadataDatabaseDataSourceImpl(
    private val tokenMetadataDAO: ITokenMetadataDAO
): ITokenMetadataDatabaseDataSource {

    @Throws(DBErrorException::class)
    override suspend fun save(tokenMetadataList: Iterable<TokenMetadataEntity>) {
        withContext(Dispatchers.IO) {
            try {
                tokenMetadataDAO.insert(tokenMetadataList.toList())
            } catch (ex: Exception) {
                throw DBErrorException("An error occurred when trying to save token metadata", ex)
            }
        }
    }

    @Throws(DBErrorException::class)
    override suspend fun save(tokenMetadata: TokenMetadataEntity) {
        withContext(Dispatchers.IO) {
            try {
                tokenMetadataDAO.insert(tokenMetadata)
            } catch (ex: Exception) {
                throw DBErrorException("An error occurred when trying to save token metadata", ex)
            }
        }
    }

    @Throws(DBErrorException::class)
    override suspend fun delete(cid: String) {
        withContext(Dispatchers.IO) {
            try {
                tokenMetadataDAO.deleteByCid(cid)
            } catch (ex: Exception){
                throw DBErrorException("An error occurred when trying to delete token metadata", ex)
            }
        }
    }

    @Throws(DBErrorException::class, DBNoResultException::class)
    override suspend fun findByAuthorAddress(address: String): Iterable<TokenMetadataEntity>  =
        withContext(Dispatchers.IO) {
            try {
                tokenMetadataDAO.findByAuthorAddress(address).also {
                    if(it.isEmpty()) {
                        throw DBNoResultException("No metadata found")
                    }
                }
            } catch (ex: Exception) {
                throw DBErrorException("An error occurred when trying to fetch token metadata", ex)
            }
        }

    @Throws(DBErrorException::class, DBNoResultException::class)
    override suspend fun findOneByCid(cid: String): TokenMetadataEntity =
        withContext(Dispatchers.IO) {
            try {
                tokenMetadataDAO.findOneByCid(cid) ?: throw DBNoResultException("Token metadata not found")
            } catch (ex: Exception) {
                throw DBErrorException("An error occurred when trying to fetch token metadata", ex)
            }
        }

    @Throws(DBErrorException::class, DBNoResultException::class)
    override suspend fun findByCidList(cidList: Iterable<String>): Iterable<TokenMetadataEntity> =
        withContext(Dispatchers.IO) {
            try {
                tokenMetadataDAO.findByCidList(cidList.toList()).also {
                    if(it.isEmpty()) {
                        throw DBNoResultException("No metadata found")
                    }
                }
            } catch (ex: Exception) {
                throw DBErrorException("An error occurred when trying to fetch token metadata", ex)
            }
        }

}
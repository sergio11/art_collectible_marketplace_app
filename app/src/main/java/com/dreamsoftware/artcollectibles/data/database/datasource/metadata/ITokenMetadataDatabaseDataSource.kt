package com.dreamsoftware.artcollectibles.data.database.datasource.metadata

import com.dreamsoftware.artcollectibles.data.database.exception.DBErrorException
import com.dreamsoftware.artcollectibles.data.database.exception.DBNoResultException
import com.dreamsoftware.artcollectibles.data.database.room.entity.TokenMetadataEntity

interface ITokenMetadataDatabaseDataSource  {

    @Throws(DBErrorException::class)
    suspend fun save(tokenMetadataList: Iterable<TokenMetadataEntity>)

    @Throws(DBErrorException::class)
    suspend fun save(tokenMetadata: TokenMetadataEntity)

    @Throws(DBErrorException::class)
    suspend fun delete(cid: String)

    @Throws(DBErrorException::class, DBNoResultException::class)
    suspend fun findByAuthorAddress(address: String): Iterable<TokenMetadataEntity>

    @Throws(DBErrorException::class, DBNoResultException::class)
    suspend fun findOneByCid(cid: String): TokenMetadataEntity

    @Throws(DBErrorException::class, DBNoResultException::class)
    suspend fun findByCidList(cidList: Iterable<String>): Iterable<TokenMetadataEntity>

}
package com.dreamsoftware.artcollectibles.data.database.datasource.metadata

import com.dreamsoftware.artcollectibles.data.database.exception.DBErrorException
import com.dreamsoftware.artcollectibles.data.database.room.entity.TokenMetadataEntity

interface ITokenMetadataDatabaseDataSource  {

    @Throws(DBErrorException::class)
    suspend fun save(tokenMetadataList: List<TokenMetadataEntity>)

}
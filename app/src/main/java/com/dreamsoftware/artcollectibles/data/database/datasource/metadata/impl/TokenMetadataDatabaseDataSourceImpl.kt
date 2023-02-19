package com.dreamsoftware.artcollectibles.data.database.datasource.metadata.impl

import com.dreamsoftware.artcollectibles.data.database.datasource.metadata.ITokenMetadataDatabaseDataSource
import com.dreamsoftware.artcollectibles.data.database.room.dao.metadata.ITokenMetadataDAO
import com.dreamsoftware.artcollectibles.data.database.room.entity.TokenMetadataEntity

internal class TokenMetadataDatabaseDataSourceImpl(
    private val tokenMetadataDAO: ITokenMetadataDAO
): ITokenMetadataDatabaseDataSource {

    override suspend fun save(tokenMetadataList: List<TokenMetadataEntity>) {
        TODO("Not yet implemented")
    }

}
package com.dreamsoftware.artcollectibles.data.database.room.dao.metadata

import androidx.room.Query
import com.dreamsoftware.artcollectibles.data.database.room.dao.core.ISupportDAO
import com.dreamsoftware.artcollectibles.data.database.room.entity.TokenMetadataEntity

interface ITokenMetadataDAO: ISupportDAO<TokenMetadataEntity> {

    @Query("SELECT * FROM token_metadata WHERE :address = author_address")
    fun findByAuthorAddress(address: String): List<TokenMetadataEntity>

    @Query("SELECT * FROM token_metadata WHERE :cid = cid")
    fun findOneByCid(cid: String): TokenMetadataEntity?

    @Query("SELECT * FROM token_metadata WHERE cid IN (:cidList)")
    fun findByCidList(cidList: List<String>): List<TokenMetadataEntity>

    @Query("DELETE FROM token_metadata WHERE :cid = cid")
    fun deleteByCid(cid: String)

}
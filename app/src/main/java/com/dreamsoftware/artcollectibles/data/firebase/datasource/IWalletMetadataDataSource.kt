package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.SaveWalletMetadataException
import com.dreamsoftware.artcollectibles.data.firebase.exception.WalletMetadataNotFoundException
import com.dreamsoftware.artcollectibles.data.firebase.model.WalletMetadataDTO

interface IWalletMetadataDataSource {

    /**
     * @param walletMetadata
     */
    @Throws(SaveWalletMetadataException::class)
    suspend fun save(walletMetadata: WalletMetadataDTO)

    /**
     * @param uid
     */
    @Throws(WalletMetadataNotFoundException::class)
    suspend fun getByUserUid(uid: String): WalletMetadataDTO
}
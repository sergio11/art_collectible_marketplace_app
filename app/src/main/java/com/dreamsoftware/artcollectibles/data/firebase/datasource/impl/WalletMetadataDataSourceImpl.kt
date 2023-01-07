package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IWalletMetadataDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.mapper.WalletMetadataMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.WalletMetadataDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Wallet Metadata Data Source
 * @param firebaseStore
 * @param walletMetadataMapper
 */
internal class WalletMetadataDataSourceImpl(
    private val firebaseStore: FirebaseFirestore,
    private val walletMetadataMapper: WalletMetadataMapper
): IWalletMetadataDataSource {

    private companion object {
        const val COLLECTION_NAME = "wallet_metadata"
    }

    @Throws(SaveWalletMetadataException::class)
    override suspend fun save(walletMetadata: WalletMetadataDTO): Unit = withContext(Dispatchers.IO)  {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(walletMetadata.userUid)
                .set(walletMetadataMapper.mapInToOut(walletMetadata))
                .await()
        } catch (ex: Exception) {
            throw SaveWalletMetadataException("An error occurred when trying to save wallet metadata information", ex)
        }
    }

    @Throws(WalletMetadataNotFoundException::class)
    override suspend fun getByUserUid(uid: String): WalletMetadataDTO = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(uid).get().await()?.data?.let {
                    walletMetadataMapper.mapOutToIn(it)
                } ?: throw WalletMetadataNotFoundException("Wallet Metadata not found")
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw FirebaseException("An error occurred when trying to get metadata information", ex)
        }
    }
}
package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IWalletSecretsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.mapper.WalletSecretMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.WalletSecretDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Secrets Data Source
 * @param firebaseStore
 * @param walletSecretMapper
 */
internal class WalletSecretsDataSourceImpl(
    private val firebaseStore: FirebaseFirestore,
    private val walletSecretMapper: WalletSecretMapper
): IWalletSecretsDataSource {

    private companion object {
        const val SECRETS_COLLECTION_NAME = "wallet_secrets"
    }

    @Throws(SaveSecretException::class)
    override suspend fun save(secret: WalletSecretDTO): Unit = withContext(Dispatchers.IO)  {
        try {
            firebaseStore.collection(SECRETS_COLLECTION_NAME)
                .document(secret.userUid)
                .set(walletSecretMapper.mapInToOut(secret))
                .await()
        } catch (ex: Exception) {
            throw SaveSecretException("An error occurred when trying to save secret information", ex)
        }
    }

    @Throws(SecretNotFoundException::class)
    override suspend fun getByUserUid(uid: String): WalletSecretDTO = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(SECRETS_COLLECTION_NAME)
                .document(uid).get().await()?.data?.let {
                    walletSecretMapper.mapOutToIn(it)
                } ?: throw UserNotFoundException("Secret not found")
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw UserErrorException("An error occurred when trying to get secret information", ex)
        }
    }
}
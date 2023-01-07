package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.ISecretDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.mapper.SecretMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Secret Data Source Impl
 * @param firebaseStore
 * @param secretMapper
 */
internal class SecretDataSourceImpl(
    private val firebaseStore: FirebaseFirestore,
    private val secretMapper: SecretMapper
): ISecretDataSource {

    private companion object {
        const val COLLECTION_NAME = "secrets"
    }

    @Throws(SaveSecretException::class)
    override suspend fun save(secret: SecretDTO): Unit = withContext(Dispatchers.IO)  {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(secret.userUid)
                .set(secretMapper.mapInToOut(secret))
                .await()
        } catch (ex: Exception) {
            throw SaveSecretException("An error occurred when trying to save secret information", ex)
        }
    }

    @Throws(SecretNotFoundException::class)
    override suspend fun getByUserUid(uid: String): SecretDTO = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(uid).get().await()?.data?.let {
                    secretMapper.mapOutToIn(it)
                } ?: throw SecretNotFoundException("Secret not found")
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw SecretNotFoundException("An error occurred when trying to get secret information", ex)
        }
    }
}
package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.ISecretsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.mapper.SecretMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Secrets Data Source
 * @param firebaseStore
 * @param secretMapper
 */
internal class SecretsDataSourceImpl(
    private val firebaseStore: FirebaseFirestore,
    private val secretMapper: SecretMapper
): ISecretsDataSource {

    private companion object {
        const val SECRETS_COLLECTION_NAME = "secrets"
    }

    @Throws(SaveSecretException::class)
    override suspend fun save(secret: SecretDTO): Unit = withContext(Dispatchers.IO)  {
        try {
            firebaseStore.collection(SECRETS_COLLECTION_NAME)
                .document(secret.uid)
                .set(secretMapper.mapInToOut(secret))
                .await()
        } catch (ex: Exception) {
            throw SaveSecretException("An error occurred when trying to save secret information", ex)
        }
    }

    @Throws(SecretNotFoundException::class)
    override suspend fun getByUserUid(uid: String): SecretDTO = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(SECRETS_COLLECTION_NAME)
                .document(uid).get().await()?.data?.let {
                    secretMapper.mapOutToIn(it)
                } ?: throw UserNotFoundException("Secret not found")
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw UserErrorException("An error occurred when trying to get secret information", ex)
        }
    }
}
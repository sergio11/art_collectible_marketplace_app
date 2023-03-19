package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IVisitorsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.internal.toLongOrDefault

/**
 * Visitors Data Source
 * @param firebaseStore
 */
internal class VisitorsDataSourceImpl(
    private val firebaseStore: FirebaseFirestore
): IVisitorsDataSource {

    private companion object {
        const val COLLECTION_NAME = "visitors"
        const val COUNT_FIELD_NAME = "count"
        const val IDS_FIELD_NAME = "ids"
        const val KEY_COUNT_SUFFIX = "_count"
    }

    @Throws(AddVisitorException::class)
    override suspend fun addVisitor(tokenId: String, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(tokenId).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayUnion(userAddress)), SetOptions.merge()).await()
                    document(tokenId + KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)), SetOptions.merge()).await()
                }
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw AddVisitorException("An error occurred when trying to add visitor", ex)
            }
        }
    }

    @Throws(GetVisitorException::class)
    override suspend fun count(tokenId: String) = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(tokenId + KEY_COUNT_SUFFIX)
                .get()
                .await()?.data?.get(COUNT_FIELD_NAME)
                .toString()
                .toLongOrDefault(0L)
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFavoritesException("An error occurred when trying to get visitors", ex)
        }
    }

    @Throws(GetVisitorsByTokenException::class)
    override suspend fun getByTokenId(tokenId: String): List<String> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(tokenId)
                .get()
                .await()?.data?.get(IDS_FIELD_NAME) as? List<String> ?: emptyList()
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetVisitorsByTokenException("An error occurred when trying to get visitors", ex)
        }
    }
}
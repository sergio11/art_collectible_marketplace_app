package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFavoritesDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.AddToFavoritesException
import com.dreamsoftware.artcollectibles.data.firebase.exception.FirebaseException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetFavoritesException
import com.dreamsoftware.artcollectibles.data.firebase.exception.RemoveFromFavoritesException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.internal.toLongOrDefault

internal class FavoritesDataSourceImpl(
    private val firebaseStore: FirebaseFirestore
): IFavoritesDataSource {

    private companion object {
        const val COLLECTION_NAME = "favorites"
        const val COUNT_FIELD_NAME = "count"
        const val IDS_FIELD_NAME = "ids"
        const val KEY_COUNT_SUFFIX = "_count"
    }

    @Throws(GetFavoritesException::class)
    override suspend fun hasAdded(tokenId: String, userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .whereArrayContains(tokenId, userId)
                .get()
                .await()
                .isEmpty
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFavoritesException("An error occurred when trying to get favorites", ex)
        }
    }

    @Throws(GetFavoritesException::class)
    override suspend fun getList(userId: String): List<String> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(userId)
                .get()
                .await()?.data?.get(IDS_FIELD_NAME) as? List<String> ?: emptyList()
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFavoritesException("An error occurred when trying to get favorites", ex)
        }
    }

    @Throws(GetFavoritesException::class)
    override suspend fun count(id: String): Long = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(id + KEY_COUNT_SUFFIX)
                .get()
                .await()?.data?.get(COUNT_FIELD_NAME)
                .toString()
                .toLongOrDefault(0L)
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFavoritesException("An error occurred when trying to get favorites", ex)
        }
    }

    @Throws(AddToFavoritesException::class)
    override suspend fun add(tokenId: String, userId: String) {
        try {
            firebaseStore.collection(COLLECTION_NAME).apply {
                document(tokenId).update(IDS_FIELD_NAME, FieldValue.arrayUnion(userId)).await()
                document(tokenId + KEY_COUNT_SUFFIX).update(COUNT_FIELD_NAME, FieldValue.increment(1)).await()
                document(userId).update(IDS_FIELD_NAME, FieldValue.arrayUnion(tokenId)).await()
                document(userId + KEY_COUNT_SUFFIX).update(COUNT_FIELD_NAME, FieldValue.increment(1)).await()
            }
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw AddToFavoritesException("An error occurred when trying to save favorite", ex)
        }
    }

    @Throws(RemoveFromFavoritesException::class)
    override suspend fun remove(tokenId: String, userId: String) {
        try {
            firebaseStore.collection(COLLECTION_NAME).apply {
                document(tokenId).update(IDS_FIELD_NAME, FieldValue.arrayRemove(userId)).await()
                document(tokenId + KEY_COUNT_SUFFIX).update(COUNT_FIELD_NAME, FieldValue.increment(-1)).await()
                document(userId).update(IDS_FIELD_NAME, FieldValue.arrayRemove(tokenId)).await()
                document(userId + KEY_COUNT_SUFFIX).update(COUNT_FIELD_NAME, FieldValue.increment(-1)).await()
            }
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw RemoveFromFavoritesException("An error occurred when trying to remove from favorites", ex)
        }
    }
}
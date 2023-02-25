package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFavoritesDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.AddToFavoritesException
import com.dreamsoftware.artcollectibles.data.firebase.exception.FirebaseException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetFavoritesException
import com.dreamsoftware.artcollectibles.data.firebase.exception.RemoveFromFavoritesException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
    override suspend fun hasAdded(tokenId: String, userAddress: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = firebaseStore.collection(COLLECTION_NAME)
                .whereArrayContains(IDS_FIELD_NAME, userAddress)
                .get()
                .await()
            !result.isEmpty
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFavoritesException("An error occurred when trying to get favorites", ex)
        }
    }

    @Throws(GetFavoritesException::class)
    override suspend fun getList(userAddress: String): List<String> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(userAddress)
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
    override suspend fun add(tokenId: String, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(tokenId).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayUnion(userAddress)), SetOptions.merge()).await()
                    document(tokenId + KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)), SetOptions.merge()).await()
                    document(userAddress).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayUnion(tokenId)), SetOptions.merge()).await()
                    document(userAddress + KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)), SetOptions.merge()).await()
                }
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw AddToFavoritesException("An error occurred when trying to save favorite", ex)
            }
        }
    }

    @Throws(RemoveFromFavoritesException::class)
    override suspend fun remove(tokenId: String, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(tokenId).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayRemove(userAddress)), SetOptions.merge()).await()
                    document(tokenId + KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(-1)), SetOptions.merge()).await()
                    document(userAddress).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayRemove(tokenId)), SetOptions.merge()).await()
                    document(userAddress + KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(-1)), SetOptions.merge()).await()
                }
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw RemoveFromFavoritesException("An error occurred when trying to remove from favorites", ex)
            }
        }
    }
}
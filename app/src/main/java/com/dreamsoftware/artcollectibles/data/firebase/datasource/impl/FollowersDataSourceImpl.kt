package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFollowersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.internal.toLongOrDefault

internal class FollowersDataSourceImpl(
    private val firebaseStore: FirebaseFirestore
): IFollowersDataSource {

    private companion object {
        const val COLLECTION_NAME = "followers"
        const val COUNT_FIELD_NAME = "count"
        const val FOLLOWING_IDS_FIELD_NAME = "following_ids"
        const val FOLLOWERS_IDS_FIELD_NAME = "following_ids"
        const val FOLLOWERS_COUNT_SUFFIX = "_followers_count"
        const val FOLLOWING_COUNT_SUFFIX = "_following_count"
        const val FOLLOWERS_SUFFIX = "_followers"
        const val FOLLOWING_SUFFIX = "_following"
    }

    @Throws(CheckFollowerException::class)
    override suspend fun isFollowedBy(from: String, to: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = firebaseStore.collection(COLLECTION_NAME)
                .whereArrayContains(FOLLOWERS_COUNT_SUFFIX, from)
                .get()
                .await()
            !result.isEmpty
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFavoritesException("An error occurred when trying to get favorites", ex)
        }
    }

    @Throws(GetFollowersException::class)
    override suspend fun getFollowers(userId: String): List<String> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(userId + FOLLOWERS_SUFFIX)
                .get()
                .await()?.data?.get(FOLLOWERS_IDS_FIELD_NAME) as? List<String> ?: emptyList()
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFollowersException("An error occurred when trying to get followers", ex)
        }
    }

    @Throws(GetFollowingException::class)
    override suspend fun getFollowing(userId: String): List<String> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(userId + FOLLOWING_SUFFIX)
                .get()
                .await()?.data?.get(FOLLOWING_IDS_FIELD_NAME) as? List<String> ?: emptyList()
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFollowingException("An error occurred when trying to get followers", ex)
        }
    }

    @Throws(CountFollowersException::class)
    override suspend fun countFollowers(userId: String): Long = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(userId + FOLLOWERS_COUNT_SUFFIX)
                .get()
                .await()?.data?.get(COUNT_FIELD_NAME)
                .toString()
                .toLongOrDefault(0L)
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw CountFollowingException("An error occurred when trying to get followers", ex)
        }
    }

    @Throws(CountFollowingException::class)
    override suspend fun countFollowing(userId: String): Long = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(userId + FOLLOWING_COUNT_SUFFIX)
                .get()
                .await()?.data?.get(COUNT_FIELD_NAME)
                .toString()
                .toLongOrDefault(0L)
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw CountFollowingException("An error occurred when trying to get followers", ex)
        }
    }

    @Throws(AddFollowerException::class)
    override suspend fun follow(from: String, to: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(from + FOLLOWING_SUFFIX).set(hashMapOf(FOLLOWING_IDS_FIELD_NAME to FieldValue.arrayUnion(to)), SetOptions.merge()).await()
                    document(from + FOLLOWING_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)), SetOptions.merge()).await()
                    document(to + FOLLOWERS_SUFFIX).set(hashMapOf(FOLLOWERS_IDS_FIELD_NAME to FieldValue.arrayUnion(from)), SetOptions.merge()).await()
                    document(to + FOLLOWERS_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)), SetOptions.merge()).await()
                }
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw AddFollowerException("An error occurred when trying to add follower")
            }
        }
    }

    @Throws(RemoveFollowerException::class)
    override suspend fun unfollow(from: String, to: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(from + FOLLOWING_SUFFIX).set(hashMapOf(FOLLOWING_IDS_FIELD_NAME to FieldValue.arrayRemove(to)), SetOptions.merge()).await()
                    document(from + FOLLOWING_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(-1)), SetOptions.merge()).await()
                    document(to + FOLLOWERS_SUFFIX).set(hashMapOf(FOLLOWERS_IDS_FIELD_NAME to FieldValue.arrayRemove(from)), SetOptions.merge()).await()
                    document(to + FOLLOWERS_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(-1)), SetOptions.merge()).await()
                }
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw RemoveFollowerException("An error occurred when trying to remove follower")
            }
        }
    }
}
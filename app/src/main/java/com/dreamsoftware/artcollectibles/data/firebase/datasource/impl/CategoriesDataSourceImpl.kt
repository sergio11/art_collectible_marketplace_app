package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICategoriesDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.mapper.CategoriesMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.CategoryDTO
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.internal.toLongOrDefault

internal class CategoriesDataSourceImpl(
    private val categoriesMapper: CategoriesMapper,
    private val firebaseStore: FirebaseFirestore
) : ICategoriesDataSource {

    private companion object {
        const val COLLECTION_NAME = "categories"
        const val CATEGORIES_KEY = "all"
        const val IDS_FIELD_NAME = "ids"
        const val KEY_COUNT_SUFFIX = "_count"
        const val COUNT_FIELD_NAME = "count"
    }

    @Throws(GetCategoriesException::class)
    override suspend fun getAll(): Iterable<CategoryDTO> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(CATEGORIES_KEY).get().await()?.data?.let {
                    categoriesMapper.mapInToOut(it)
                }?.sortedBy { it.name } ?: emptyList()
        } catch (ex: Exception) {
            throw GetCategoriesException("An error occurred when trying to get categories", ex)
        }
    }

    @Throws(GetCategoryException::class)
    override suspend fun getByUid(uid: String): CategoryDTO = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(CATEGORIES_KEY).get().await()?.data?.let {
                    categoriesMapper.mapInToOut(it)
                }?.find { it.uid == uid } ?: throw GetCategoriesException("no category found")
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetCategoryException("An error occurred when trying to get category", ex)
        }
    }

    @Throws(AddTokenToCategoryException::class)
    override suspend fun addToken(tokenId: String, categoryUid: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(categoryUid).set(
                        hashMapOf(
                            IDS_FIELD_NAME to FieldValue.arrayUnion(
                                tokenId
                            )
                        ), SetOptions.merge()
                    ).await()
                    document(categoryUid + KEY_COUNT_SUFFIX).set(
                        hashMapOf(
                            COUNT_FIELD_NAME to FieldValue.increment(1)
                        ), SetOptions.merge()
                    ).await()
                }
            } catch (ex: Exception) {
                throw AddTokenToCategoryException("An error occurred when trying to add token", ex)
            }
        }
    }

    @Throws(RemoveTokenFromCategoryException::class)
    override suspend fun removeToken(tokenId: String, categoryUid: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(categoryUid).set(
                        hashMapOf(
                            IDS_FIELD_NAME to FieldValue.arrayRemove(
                                tokenId
                            )
                        ), SetOptions.merge()
                    ).await()
                    val tokenCount = document(categoryUid + KEY_COUNT_SUFFIX)
                        .get().await()?.data?.get(COUNT_FIELD_NAME)
                        .toString().toLongOrDefault(0L)
                    if (tokenCount > 0) {
                        document(categoryUid + KEY_COUNT_SUFFIX).set(
                            hashMapOf(
                                COUNT_FIELD_NAME to FieldValue.increment(-1)
                            ), SetOptions.merge()
                        ).await()
                    }
                }
            }  catch (ex: Exception) {
                throw RemoveTokenFromCategoryException(
                    "An error occurred when trying to remove token",
                    ex
                )
            }
        }
    }

    @Throws(GetTokensByCategoryException::class)
    override suspend fun getTokensByUid(uid: String): Iterable<String> =
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME)
                    .document(uid)
                    .get()
                    .await()?.data?.get(IDS_FIELD_NAME) as? List<String> ?: emptyList()
            } catch (ex: Exception) {
                throw GetTokensByCategoryException(
                    "An error occurred when trying to get tokens",
                    ex
                )
            }
        }
}
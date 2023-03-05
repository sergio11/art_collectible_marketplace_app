package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICategoriesDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.FirebaseException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetCategoriesException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetCategoryException
import com.dreamsoftware.artcollectibles.data.firebase.mapper.CategoriesMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.CategoryDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

internal class CategoriesDataSourceImpl(
    private val categoriesMapper: CategoriesMapper,
    private val firebaseStore: FirebaseFirestore
): ICategoriesDataSource {

    private companion object {
        const val COLLECTION_NAME = "categories"
        const val CATEGORIES_KEY = "all"
    }

    @Throws(GetCategoriesException::class)
    override suspend fun getAll(): Iterable<CategoryDTO> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(CATEGORIES_KEY).get().await()?.data?.let {
                    categoriesMapper.mapInToOut(it)
            } ?: throw GetCategoriesException("no categories found")
        } catch (ex: FirebaseException) {
            throw ex
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
}
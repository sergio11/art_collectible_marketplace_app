package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.GetCategoriesDataException
import com.dreamsoftware.artcollectibles.data.api.exception.GetCategoryDataException
import com.dreamsoftware.artcollectibles.data.api.mapper.ArtCollectibleCategoryMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleCategoryRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICategoriesDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ArtCollectibleCategoryRepositoryImpl(
    private val categoriesDataSource: ICategoriesDataSource,
    private val artCollectibleCategoryMapper: ArtCollectibleCategoryMapper
): IArtCollectibleCategoryRepository {

    @Throws(GetCategoriesDataException::class)
    override suspend fun getAll(): Iterable<ArtCollectibleCategory> =
        withContext(Dispatchers.Default) {
            try {
                artCollectibleCategoryMapper.mapInListToOutList(categoriesDataSource.getAll())
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw GetCategoriesDataException("An error occurred when trying to get categories", ex)
            }
        }

    @Throws(GetCategoryDataException::class)
    override suspend fun getByUid(uid: String): ArtCollectibleCategory = withContext(Dispatchers.Default) {
        try {
            artCollectibleCategoryMapper.mapInToOut(categoriesDataSource.getByUid(uid))
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw GetCategoryDataException("An error occurred when trying to get a category", ex)
        }
    }
}
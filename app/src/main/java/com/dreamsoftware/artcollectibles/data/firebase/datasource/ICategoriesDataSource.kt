package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.model.CategoryDTO

interface ICategoriesDataSource {

    @Throws(GetCategoriesException::class)
    suspend fun getAll(): Iterable<CategoryDTO>

    @Throws(GetCategoryException::class)
    suspend fun getByUid(uid: String): CategoryDTO

    @Throws(AddTokenToCategoryException::class)
    suspend fun addToken(tokenId: String, categoryUid: String)

    @Throws(RemoveTokenFromCategoryException::class)
    suspend fun removeToken(tokenId: String, categoryUid: String)

    @Throws(GetTokensByCategoryException::class)
    suspend fun getTokensByUid(uid: String): Iterable<String>

}
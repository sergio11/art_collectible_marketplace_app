package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.GetCategoriesException
import com.dreamsoftware.artcollectibles.data.firebase.model.CategoryDTO

interface ICategoriesDataSource {

    @Throws(GetCategoriesException::class)
    suspend fun getAll(): Iterable<CategoryDTO>

}
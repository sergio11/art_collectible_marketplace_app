package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.AddToFavoritesDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RegisterVisitorDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IVisitorsRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IVisitorsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Visitors Repository Impl
 * @param visitorsDataSource
 */
internal class VisitorsRepositoryImpl(
    private val visitorsDataSource: IVisitorsDataSource
): IVisitorsRepository {

    @Throws(RegisterVisitorDataException::class)
    override suspend fun register(tokenId: String, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                visitorsDataSource.addVisitor(tokenId, userAddress)
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw RegisterVisitorDataException("An error occurred when trying to register visitor", ex)
            }
        }
    }
}
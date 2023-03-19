package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.GetVisitorsByTokenDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RegisterVisitorDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IVisitorsRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IVisitorsDataSource
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * Visitors Repository Impl
 * @param visitorsDataSource
 * @param userRepository
 */
internal class VisitorsRepositoryImpl(
    private val visitorsDataSource: IVisitorsDataSource,
    private val userRepository: IUserRepository
): IVisitorsRepository {

    @Throws(RegisterVisitorDataException::class)
    override suspend fun register(tokenId: String, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                visitorsDataSource.addVisitor(tokenId, userAddress)
            } catch (ex: Exception) {
                throw RegisterVisitorDataException("An error occurred when trying to register visitor", ex)
            }
        }
    }

    @Throws(GetVisitorsByTokenDataException::class)
    override suspend fun getByTokenId(tokenId: String): Iterable<UserInfo> =  withContext(Dispatchers.IO) {
        try {
            visitorsDataSource.getByTokenId(tokenId).asSequence().map { userAddress ->
                async { runCatching { userRepository.getByAddress(userAddress) }.getOrNull() }
            }.toList().awaitAll().filterNotNull()
        } catch (ex: Exception) {
            throw GetVisitorsByTokenDataException("An error occurred when trying to get visitors", ex)
        }
    }
}
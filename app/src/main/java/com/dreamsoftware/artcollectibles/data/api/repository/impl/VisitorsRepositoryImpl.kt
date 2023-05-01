package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.GetMostVisitedTokensDataException
import com.dreamsoftware.artcollectibles.data.api.exception.GetVisitorsByTokenDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RegisterVisitorDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IVisitorsRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IVisitorsDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.math.BigInteger

/**
 * Visitors Repository Impl
 * @param visitorsDataSource
 * @param userRepository
 * @param artCollectibleRepository
 */
internal class VisitorsRepositoryImpl(
    private val visitorsDataSource: IVisitorsDataSource,
    private val userRepository: IUserRepository,
    private val artCollectibleRepository: IArtCollectibleRepository
): IVisitorsRepository {

    @Throws(RegisterVisitorDataException::class)
    override suspend fun register(tokenId: BigInteger, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                visitorsDataSource.addVisitor(tokenId, userAddress)
            } catch (ex: Exception) {
                throw RegisterVisitorDataException("An error occurred when trying to register visitor", ex)
            }
        }
    }

    @Throws(GetVisitorsByTokenDataException::class)
    override suspend fun getByTokenId(tokenId: BigInteger): Iterable<UserInfo> =  withContext(Dispatchers.IO) {
        try {
            visitorsDataSource.getByTokenId(tokenId).asSequence().map { userAddress ->
                async { runCatching { userRepository.getByAddress(userAddress, true) }.getOrNull() }
            }.toList().awaitAll().filterNotNull()
        } catch (ex: Exception) {
            throw GetVisitorsByTokenDataException("An error occurred when trying to get visitors", ex)
        }
    }

    @Throws(GetMostVisitedTokensDataException::class)
    override suspend fun getMostVisitedTokens(limit: Int): Iterable<ArtCollectible> =  withContext(Dispatchers.IO) {
        try {
            visitorsDataSource.getMostVisitedTokens(limit)
                .mapNotNull {
                    runCatching { it.toBigInteger() }.getOrNull()
                }
                .let { artCollectibleRepository.getTokens(it) }
                .sortedByDescending { it.visitorsCount }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw GetMostVisitedTokensDataException("An error occurred when trying to get most visited tokens", ex)
        }
    }
}
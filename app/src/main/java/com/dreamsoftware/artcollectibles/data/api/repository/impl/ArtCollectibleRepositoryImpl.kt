package com.dreamsoftware.artcollectibles.data.api.repository.impl

import android.util.Log
import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.ITokenMetadataRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFavoritesDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IVisitorsDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.IArtCollectibleMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheException
import com.dreamsoftware.artcollectibles.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigInteger

internal class ArtCollectibleRepositoryImpl(
    private val artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
    private val userDataSource: IUsersDataSource,
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val walletRepository: IWalletRepository,
    private val userCredentialsMapper: UserCredentialsMapper,
    private val favoritesDataSource: IFavoritesDataSource,
    private val visitorsDataSource: IVisitorsDataSource,
    private val tokenMetadataRepository: ITokenMetadataRepository,
    private val artCollectibleMemoryCacheDataSource: IArtCollectibleMemoryCacheDataSource
) : IArtCollectibleRepository {

    private companion object {
        const val TOKENS_OWNED_KEY = "TOKENS_OWNED_KEY"
        const val TOKENS_CREATED_KEY = "TOKENS_CREATED_KEY"
    }

    @Throws(ObserveArtCollectibleMintedEventsException::class)
    override suspend fun observeArtCollectibleMintedEvents(): Flow<ArtCollectibleMintedEvent> =
        withContext(Dispatchers.Default) {
            try {
                val credentials =
                    userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
                artCollectibleDataSource.observeArtCollectibleMintedEvents(credentials)
                    .map { getTokenById(it.tokenId) }.map { ArtCollectibleMintedEvent(it) }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.d("ART_COLL", "observeArtCollectibleMintedEvents - ${ex.message} ERROR!")
                throw ObserveArtCollectibleMintedEventsException(
                    "An error occurred when trying to observe minted events", ex
                )
            }
        }

    @Throws(CreateArtCollectibleException::class)
    override suspend fun create(token: CreateArtCollectible): ArtCollectible =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                with(token) {
                    // Save token metadata
                    val tokenMetadata = tokenMetadataRepository.create(token.metadata)
                    // Mint new token
                    val tokenId =
                        artCollectibleDataSource.mintToken(
                            tokenMetadata.cid, royalty,
                            userCredentialsMapper.mapOutToIn(credentials)
                        )
                    // Get detail about the token already minted
                    val tokenMinted = artCollectibleDataSource.getTokenById(
                        tokenId,
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                    mapToArtCollectible(credentials, tokenMetadata, tokenMinted)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw CreateArtCollectibleException(
                    "An error occurred when trying to create a new token", ex
                )
            }
        }

    @Throws(DeleteArtCollectibleException::class)
    override suspend fun delete(tokenId: BigInteger) {
        try {
            val credentials = userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
            // Get detail about the token
            val token = artCollectibleDataSource.getTokenById(tokenId, credentials)
            // Delete token metadata
            tokenMetadataRepository.delete(token.metadataCID)
            // Delete token
            artCollectibleDataSource.burnToken(tokenId, credentials)
            // Delete memory cache entry
            artCollectibleMemoryCacheDataSource.delete(tokenId)
        } catch (ex: Exception) {
            throw DeleteArtCollectibleException(
                "An error occurred when trying to delete a new token", ex
            )
        }
    }

    @Throws(GetTokensOwnedException::class)
    override suspend fun getTokensOwned(): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                try {
                    artCollectibleMemoryCacheDataSource.findByKey(TOKENS_OWNED_KEY)
                } catch (ex: CacheException) {
                    val tokens = artCollectibleDataSource.getTokensOwned(
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                    tokenMetadataRepository.fetchByCid(tokens.map { it.metadataCID }).mapNotNull { tokenMetadata ->
                        tokens.find { it.metadataCID == tokenMetadata.cid }?.let { token ->
                            mapToArtCollectible(credentials, tokenMetadata, token)
                        }
                    }.also {
                        artCollectibleMemoryCacheDataSource.save(TOKENS_OWNED_KEY, it)
                    }
                }
            } catch (ex: Exception) {
                throw GetTokensOwnedException(
                    "An error occurred when trying to get tokens owned", ex
                )
            }
        }

    @Throws(GetTokensCreatedException::class)
    override suspend fun getTokensCreated(): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                try {
                    artCollectibleMemoryCacheDataSource.findByKey(TOKENS_CREATED_KEY)
                } catch (ex: CacheException) {
                    val fetchTokenFilesDeferred =
                        async { tokenMetadataRepository.fetchByAuthorAddress(credentials.address) }
                    val getTokensCreatedDeferred = async {
                        artCollectibleDataSource.getTokensCreated(
                            userCredentialsMapper.mapOutToIn(credentials)
                        )
                    }
                    val tokenFiles = fetchTokenFilesDeferred.await()
                    val tokens = getTokensCreatedDeferred.await()
                    tokenFiles.mapNotNull { tokenMetadata ->
                        tokens.find { it.metadataCID == tokenMetadata.cid }?.let { token ->
                            mapToArtCollectible(credentials, tokenMetadata, token)
                        }
                    }.also {
                        artCollectibleMemoryCacheDataSource.save(TOKENS_CREATED_KEY, it)
                    }
                }
            } catch (ex: Exception) {
                throw GetTokensCreatedException(
                    "An error occurred when trying to get tokens created", ex
                )
            }
        }

    @Throws(GetTokenByIdException::class)
    override suspend fun getTokenById(tokenId: BigInteger): ArtCollectible =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                try {
                    artCollectibleMemoryCacheDataSource.findByKey(tokenId).first()
                } catch (ex: CacheException) {
                    val token = artCollectibleDataSource.getTokenById(
                        tokenId, userCredentialsMapper.mapOutToIn(credentials)
                    )
                    val tokenMetadata = tokenMetadataRepository.fetchByCid(token.metadataCID)
                    mapToArtCollectible(credentials, tokenMetadata, token).also {
                        artCollectibleMemoryCacheDataSource.save(tokenId, listOf(it))
                    }
                }
            } catch (ex: Exception) {
                throw GetTokenByIdException("An error occurred when trying to get token by id", ex)
            }
        }

    @Throws(GetTokensException::class)
    override suspend fun getTokens(tokenList: Iterable<BigInteger>): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                artCollectibleDataSource.getTokens(
                    tokenList,
                    userCredentialsMapper.mapOutToIn(credentials)
                ).asSequence()
                    .map { token ->
                        async {
                            val tokenMetadata = tokenMetadataRepository.fetchByCid(token.metadataCID)
                            mapToArtCollectible(credentials, tokenMetadata, token)
                        }
                    }
                    .toList().awaitAll()
            } catch (ex: Exception) {
                throw GetTokensException("An error occurred when trying to get tokens", ex)
            }
        }

    private suspend fun mapToArtCollectible(
        credentials: UserWalletCredentials,
        tokenMetadata: ArtCollectibleMetadata,
        token: ArtCollectibleBlockchainDTO
    ): ArtCollectible =
        withContext(Dispatchers.IO) {
            val tokenId = token.tokenId.toString()
            val tokenAuthorDeferred = async { userDataSource.getByAddress(token.creator) }
            val tokenOwnerDeferred = async { userDataSource.getByAddress(token.creator) }
            val visitorsCountDeferred = async { visitorsDataSource.count(tokenId) }
            val favoritesCountDeferred = async { favoritesDataSource.count(tokenId) }
            val hasAddedToFavDeferred =
                async { favoritesDataSource.hasAdded(tokenId, credentials.address) }
            artCollectibleMapper.mapInToOut(
                ArtCollectibleMapper.InputData(
                    metadata = tokenMetadata,
                    blockchain = token,
                    author = tokenAuthorDeferred.await(),
                    owner = tokenOwnerDeferred.await(),
                    visitorsCount = visitorsCountDeferred.await(),
                    favoritesCount = favoritesCountDeferred.await(),
                    hasAddedToFav = hasAddedToFavDeferred.await()
                )
            )
        }
}
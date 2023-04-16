package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.ITokenMetadataRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICategoriesDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICommentsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFavoritesDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IVisitorsDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.IArtCollectibleMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheException
import com.dreamsoftware.artcollectibles.domain.models.*
import com.google.common.collect.Iterables
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigInteger

internal class ArtCollectibleRepositoryImpl(
    private val artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
    private val userRepository: IUserRepository,
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val walletRepository: IWalletRepository,
    private val userCredentialsMapper: UserCredentialsMapper,
    private val favoritesDataSource: IFavoritesDataSource,
    private val visitorsDataSource: IVisitorsDataSource,
    private val tokenMetadataRepository: ITokenMetadataRepository,
    private val artCollectibleMemoryCacheDataSource: IArtCollectibleMemoryCacheDataSource,
    private val commentsDataSource: ICommentsDataSource,
    private val categoriesDataSource: ICategoriesDataSource
) : IArtCollectibleRepository {

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
                    val tokenMetadata = tokenMetadataRepository.create(metadata)
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
                    mapToArtCollectible(credentials, tokenMetadata, tokenMinted, false)
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
                mapToArtCollectible(credentials, artCollectibleDataSource.getTokensOwned(
                    userCredentialsMapper.mapOutToIn(credentials)
                ))
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw GetTokensOwnedException(
                    "An error occurred when trying to get tokens owned", ex
                )
            }
        }

    @Throws(GetTokensOwnedException::class)
    override suspend fun getTokensOwnedBy(ownerAddress: String): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                mapToArtCollectible(
                    credentials, artCollectibleDataSource.getTokensOwnedBy(
                        userCredentialsMapper.mapOutToIn(credentials),
                        ownerAddress
                    )
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw GetTokensOwnedException(
                    "An error occurred when trying to get tokens owned", ex
                )
            }
        }

    @Throws(GetTokensOwnedException::class)
    override suspend fun getTokensOwnedBy(
        ownerAddress: String,
        limit: Long
    ): Iterable<ArtCollectible> = withContext(Dispatchers.Default) {
        try {
            val credentials = walletRepository.loadCredentials()
            mapToArtCollectible(
                credentials, artCollectibleDataSource.getTokensOwnedBy(
                    userCredentialsMapper.mapOutToIn(credentials),
                    ownerAddress,
                    limit
                )
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
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
                mapToArtCollectible(credentials, artCollectibleDataSource.getTokensCreated(
                    userCredentialsMapper.mapOutToIn(credentials)
                ))
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw GetTokensCreatedException(
                    "An error occurred when trying to get tokens created", ex
                )
            }
        }

    @Throws(GetTokensCreatedException::class)
    override suspend fun getTokensCreatedBy(creatorAddress: String): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                mapToArtCollectible(
                    credentials, artCollectibleDataSource.getTokensCreatedBy(
                        userCredentialsMapper.mapOutToIn(credentials),
                        creatorAddress
                    )
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw GetTokensOwnedException(
                    "An error occurred when trying to get tokens owned", ex
                )
            }
        }

    @Throws(GetTokensCreatedException::class)
    override suspend fun getTokensCreatedBy(
        creatorAddress: String,
        limit: Long
    ): Iterable<ArtCollectible> = withContext(Dispatchers.Default) {
        try {
            val credentials = walletRepository.loadCredentials()
            mapToArtCollectible(
                credentials, artCollectibleDataSource.getTokensCreatedBy(
                    userCredentialsMapper.mapOutToIn(credentials),
                    creatorAddress,
                    limit
                )
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw GetTokensOwnedException(
                "An error occurred when trying to get tokens owned", ex
            )
        }
    }

    @Throws(GetTokenByIdException::class)
    override suspend fun getTokenById(tokenId: BigInteger): ArtCollectible =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                with(artCollectibleMemoryCacheDataSource) {
                    try {
                        findByKey(tokenId)
                    } catch (ex: CacheException) {
                        val token = artCollectibleDataSource.getTokenById(
                            tokenId, userCredentialsMapper.mapOutToIn(credentials)
                        )
                        val tokenMetadata = tokenMetadataRepository.fetchByCid(token.metadataCID)
                        mapToArtCollectible(credentials, tokenMetadata, token, true).also {
                            save(tokenId, it)
                        }
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
                with(artCollectibleMemoryCacheDataSource) {
                    buildList {
                        findByKeys(
                            keys = tokenList, strictMode = false
                        ).let { tokensCached ->
                            if (Iterables.size(tokensCached) != Iterables.size(tokenList)) {
                                val tokenIdListCached = tokensCached.map { it.id }
                                artCollectibleDataSource.getTokens(
                                    tokenList.filterNot { tokenIdListCached.contains(it) },
                                    userCredentialsMapper.mapOutToIn(credentials)
                                ).let { tokensNotCached ->
                                    fetchTokenMetadata(credentials, tokensNotCached)
                                }.let { tokensNotCached ->
                                    tokensNotCached.forEach {
                                        save(it.id, it)
                                    }
                                    addAll(tokensCached + tokensNotCached)
                                }
                            } else {
                                addAll(tokensCached)
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                throw GetTokensException("An error occurred when trying to get tokens", ex)
            }
        }

    @Throws(GetTokensByCategoryException::class)
    override suspend fun getTokensByCategory(categoryUid: String): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                val tokenList = categoriesDataSource.getTokensByUid(categoryUid)
                mapToArtCollectible(
                    credentials, artCollectibleDataSource.getTokensByCID(
                        tokenList,
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                )
            } catch (ex: Exception) {
                throw GetTokensByCategoryException("An error occurred when trying to get tokens by category")
            }
        }

    /**
     * Get similar tokens by category
     * @param tokenCid
     * @param count
     */
    @Throws(GetTokensByCategoryException::class)
    override suspend fun getSimilarTokens(tokenCid: String, count: Int): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            try {
                val credentials = walletRepository.loadCredentials()
                val tokenMetadata = tokenMetadataRepository.fetchByCid(tokenCid)
                val tokenList = categoriesDataSource.getTokensByUid(tokenMetadata.category.uid)
                    .filter { it != tokenCid }.toMutableList().apply {
                        shuffle()
                        take(count)
                    }
                mapToArtCollectible(
                    credentials, artCollectibleDataSource.getTokensByCID(
                        tokenList,
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                )
            } catch (ex: Exception) {
                throw GetTokensByCategoryException("An error occurred when trying to get tokens by category")
            }
        }

    private suspend fun mapToArtCollectible(
        credentials: UserWalletCredentials,
        tokensList: Iterable<ArtCollectibleBlockchainDTO>
    ) = withContext(Dispatchers.Default) {
        with(artCollectibleMemoryCacheDataSource) {
            val tokensIdList = tokensList.map { it.tokenId }
            buildList {
                findByKeys(
                    keys = tokensIdList, strictMode = false
                ).let { tokensCached ->
                    if (Iterables.size(tokensCached) != Iterables.size(tokensIdList)) {
                        val tokenIdListCached = tokensCached.map { it.id }
                        val tokensNotCached = fetchTokenMetadata(credentials,
                            tokensList.filterNot { tokenIdListCached.contains(it.tokenId) })
                        tokensNotCached.forEach {
                            save(it.id, it)
                        }
                        addAll(tokensCached + tokensNotCached)
                    } else {
                        addAll(tokensCached)
                    }
                }
            }
        }
    }

    private suspend fun mapToArtCollectible(
        credentials: UserWalletCredentials,
        tokenMetadata: ArtCollectibleMetadata,
        token: ArtCollectibleBlockchainDTO,
        requireUserInfoFullDetail: Boolean
    ): ArtCollectible =
        withContext(Dispatchers.IO) {
            val tokenId = token.tokenId
            val tokenAuthorDeferred =
                async { userRepository.getByAddress(token.creator, requireUserInfoFullDetail) }
            val tokenOwnerDeferred = async {
                runCatching {
                    userRepository.getByAddress(
                        token.owner,
                        requireUserInfoFullDetail
                    )
                }.getOrElse {
                    userRepository.getByAddress(token.creator, requireUserInfoFullDetail)
                }
            }
            val visitorsCountDeferred = async { visitorsDataSource.count(tokenId) }
            val favoritesCountDeferred = async { favoritesDataSource.tokenCount(tokenId) }
            val commentsCountDeferred = async { commentsDataSource.count(tokenId) }
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
                    commentsCount = commentsCountDeferred.await(),
                    hasAddedToFav = hasAddedToFavDeferred.await()
                )
            )
        }

    private suspend fun fetchTokenMetadata(
        credentials: UserWalletCredentials,
        tokensList: Iterable<ArtCollectibleBlockchainDTO> ) = withContext(Dispatchers.IO) {
        tokenMetadataRepository.fetchByCid(tokensList.map { it.metadataCID }
        ).mapNotNull { tokenMetadata ->
            tokensList.find { it.metadataCID == tokenMetadata.cid }
                ?.let { token ->
                    mapToArtCollectible(
                        credentials,
                        tokenMetadata,
                        token,
                        false
                    )
                }
        }
    }
}
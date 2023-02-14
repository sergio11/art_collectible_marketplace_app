package com.dreamsoftware.artcollectibles.data.api.repository.impl

import android.util.Log
import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.api.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFavoritesDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.models.CreateTokenMetadataDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMintedEvent
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigInteger

internal class ArtCollectibleRepositoryImpl(
    private val artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
    private val ipfsDataSource: IpfsDataSource,
    private val userDataSource: IUsersDataSource,
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val walletRepository: IWalletRepository,
    private val userCredentialsMapper: UserCredentialsMapper,
    private val favoritesDataSource: IFavoritesDataSource
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
                val credentials =
                    userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
                with(token) {
                    // Save token metadata into IPFS
                    val tokenMetadata = ipfsDataSource.create(
                        CreateTokenMetadataDTO(
                            name = name,
                            description = description,
                            fileUri = fileUri,
                            mediaType = mediaType,
                            authorAddress = credentials.address
                        )
                    )
                    // Mint new token
                    val tokenId =
                        artCollectibleDataSource.mintToken(tokenMetadata.cid, royalty, credentials)
                    // Get detail about the token already minted
                    val tokenMinted = artCollectibleDataSource.getTokenById(tokenId, credentials)
                    // Get the detail about token author
                    val creatorInfo = userDataSource.getByAddress(credentials.address)
                    artCollectibleMapper.mapInToOut(
                        ArtCollectibleMapper.InputData(
                            metadata = tokenMetadata,
                            blockchain = tokenMinted,
                            userInfo = creatorInfo,
                            favoritesCount = 0L
                        )
                    )
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
            // Delete token metadata from IPFS
            ipfsDataSource.delete(token.metadataCID)
            // Delete token
            artCollectibleDataSource.burnToken(tokenId, credentials)
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
                val fetchTokenFilesDeferred = async { ipfsDataSource.fetchByOwnerAddress(credentials.address) }
                val getTokensOwnedDeferred = async {
                    artCollectibleDataSource.getTokensOwned(
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                }
                val tokenFiles = fetchTokenFilesDeferred.await()
                val tokens = getTokensOwnedDeferred.await()
                tokenFiles.mapNotNull { tokenMetadata ->
                    tokens.find { it.metadataCID == tokenMetadata.cid }?.let { token ->
                        val tokenOwner = userDataSource.getByAddress(tokenMetadata.ownerAddress)
                        val favoritesCount = favoritesDataSource.count(token.tokenId.toString())
                        artCollectibleMapper.mapInToOut(
                            ArtCollectibleMapper.InputData(
                                metadata = tokenMetadata,
                                blockchain = token,
                                userInfo = tokenOwner,
                                favoritesCount = favoritesCount
                            )
                        )
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
                val fetchTokenFilesDeferred = async { ipfsDataSource.fetchByCreatorAddress(credentials.address) }
                val getTokensCreatedDeferred = async { artCollectibleDataSource.getTokensCreated(
                    userCredentialsMapper.mapOutToIn(credentials)
                ) }
                val tokenFiles = fetchTokenFilesDeferred.await()
                val tokens = getTokensCreatedDeferred.await()
                tokenFiles.mapNotNull { tokenMetadata ->
                    tokens.find { it.metadataCID == tokenMetadata.cid }?.let { token ->
                        val tokenAuthor = userDataSource.getByAddress(token.creator)
                        val favoritesCount = favoritesDataSource.count(token.tokenId.toString())
                        artCollectibleMapper.mapInToOut(
                            ArtCollectibleMapper.InputData(
                                metadata = tokenMetadata,
                                blockchain = token,
                                userInfo = tokenAuthor,
                                favoritesCount = favoritesCount
                            )
                        )
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
                val token = artCollectibleDataSource.getTokenById(
                    tokenId, userCredentialsMapper.mapOutToIn(credentials)
                )
                val tokenMetadata = ipfsDataSource.fetchByCid(token.metadataCID)
                val tokenAuthor = userDataSource.getByAddress(tokenMetadata.ownerAddress)
                val favoritesCount = favoritesDataSource.count(token.tokenId.toString())
                artCollectibleMapper.mapInToOut(
                    ArtCollectibleMapper.InputData(
                        metadata = tokenMetadata,
                        blockchain = token,
                        userInfo = tokenAuthor,
                        favoritesCount = favoritesCount
                    )
                )
            } catch (ex: Exception) {
                throw GetTokenByIdException("An error occurred when trying to get token by id", ex)
            }
        }
}
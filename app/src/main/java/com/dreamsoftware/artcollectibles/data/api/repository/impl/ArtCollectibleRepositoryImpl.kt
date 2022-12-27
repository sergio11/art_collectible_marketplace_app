package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.ArtCollectibleDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.api.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.models.request.FileMetadataDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger

internal class ArtCollectibleRepositoryImpl(
    private val artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
    private val ipfsDataSource: IpfsDataSource,
    private val userDataSource: IUsersDataSource,
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val walletRepository: IWalletRepository,
    private val userCredentialsMapper: UserCredentialsMapper
) : IArtCollectibleRepository {

    @Throws(ArtCollectibleDataException::class)
    override suspend fun create(token: CreateArtCollectible): ArtCollectible = withContext(Dispatchers.Default) {
        try {
            val credentials = userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
            with(token) {
                // Save token metadata into IPFS
                val filePinned = ipfsDataSource.saveFile(file, mediaType, FileMetadataDTO(name).apply {
                    description = description
                    ownerAddress = credentials.address
                    authorAddress = credentials.address
                })
                // Mint new token
                val tokenId = artCollectibleDataSource.mintToken(filePinned.ipfsPinHash, royalty, credentials)
                // Get detail about the token already minted
                val tokenMinted = artCollectibleDataSource.getTokenById(tokenId, credentials)
                // Get the detail about token author
                val creatorInfo = userDataSource.getByAddress(credentials.address)
                artCollectibleMapper.mapInToOut(Triple(filePinned, tokenMinted, creatorInfo))
            }
        } catch (ex: Exception) {
            throw ArtCollectibleDataException("An error occurred when trying to create a new token", ex)
        }
    }

    @Throws(ArtCollectibleDataException::class)
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
            throw ArtCollectibleDataException("An error occurred when trying to delete a new token", ex)
        }
    }

    override suspend fun getTokensOwned(): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            val credentials = walletRepository.loadCredentials()
            val tokenFiles = ipfsDataSource.fetchByOwnerAddress(credentials.address)
            val tokens = artCollectibleDataSource.getTokensOwned(userCredentialsMapper.mapOutToIn(credentials))
            tokenFiles.mapNotNull { tokenMetadata ->
                tokens.find { it.metadataCID == tokenMetadata.ipfsPinHash }?.let { token ->
                    val tokenOwner = userDataSource.getByAddress(tokenMetadata.metadata.ownerAddress)
                    artCollectibleMapper.mapInToOut(Triple(tokenMetadata, token, tokenOwner))
                }
            }
        }

    override suspend fun getTokensCreated(): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            val credentials = walletRepository.loadCredentials()
            val tokenFiles = ipfsDataSource.fetchByCreatorAddress(credentials.address)
            val tokens = artCollectibleDataSource.getTokensCreated(userCredentialsMapper.mapOutToIn(credentials))
            tokenFiles.mapNotNull { tokenMetadata ->
                tokens.find { it.metadataCID == tokenMetadata.ipfsPinHash }?.let { token ->
                    val tokenAuthor = userDataSource.getByAddress(token.creator)
                    artCollectibleMapper.mapInToOut(Triple(tokenMetadata, token, tokenAuthor))
                }
            }
        }

    override suspend fun getTokenById(tokenId: BigInteger): ArtCollectible =
        withContext(Dispatchers.Default) {
            val credentials = walletRepository.loadCredentials()
            val token = artCollectibleDataSource.getTokenById(tokenId, userCredentialsMapper.mapOutToIn(credentials))
            val tokenMetadata = ipfsDataSource.fetchByCid(token.metadataCID)
            val tokenAuthor = userDataSource.getById(tokenMetadata.metadata.ownerAddress)
            artCollectibleMapper.mapInToOut(Triple(tokenMetadata, token, tokenAuthor))
        }
}
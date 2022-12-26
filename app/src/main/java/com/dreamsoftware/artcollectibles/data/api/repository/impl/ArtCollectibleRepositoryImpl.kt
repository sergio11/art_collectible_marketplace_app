package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.api.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
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

    override suspend fun getTokensOwned(): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            val credentials = walletRepository.loadCredentials()
            val tokenFiles = ipfsDataSource.fetchByOwnerAddress(credentials.address)
            val tokens = artCollectibleDataSource.getTokensOwned(userCredentialsMapper.mapOutToIn(credentials))
            tokenFiles.mapNotNull { tokenMetadata ->
                tokens.find { it.tokenId == tokenMetadata.metadata.tokenId }?.let { token ->
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
                tokens.find { it.tokenId == tokenMetadata.metadata.tokenId }?.let { token ->
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
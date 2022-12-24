package com.dreamsoftware.artcollectibles.data.api.impl

import com.dreamsoftware.artcollectibles.data.api.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.models.response.FilePinnedDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.AuthorInfo
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger

internal class ArtCollectibleRepositoryImpl(
    private val artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
    private val ipfsDataSource: IpfsDataSource,
    private val userCredentialsMapper: UserCredentialsMapper
) : IArtCollectibleRepository {

    override suspend fun getTokensOwnedBy(credentials: UserWalletCredentials): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            val tokenFiles = ipfsDataSource.fetchByOwnerAddress(credentials.address)
            val tokens = artCollectibleDataSource.getTokensOwned(userCredentialsMapper.mapOutToIn(credentials))
            tokenFiles.zip(tokens).map { mapToArtCollectible(it.first, it.second) }
        }

    override suspend fun getTokensCreatedBy(credentials: UserWalletCredentials): Iterable<ArtCollectible> =
        withContext(Dispatchers.Default) {
            val tokenFiles = ipfsDataSource.fetchByCreatorAddress(credentials.address)
            val tokens = artCollectibleDataSource.getTokensCreated(userCredentialsMapper.mapOutToIn(credentials))
            tokenFiles.zip(tokens).map { mapToArtCollectible(it.first, it.second) }
        }

    override suspend fun getTokenById(tokenId: BigInteger, credentials: UserWalletCredentials): ArtCollectible =
        withContext(Dispatchers.Default) {
            val token = artCollectibleDataSource.getTokenById(tokenId, userCredentialsMapper.mapOutToIn(credentials))
            val tokenMetadata = ipfsDataSource.fetchByCid(token.metadataCID)
            mapToArtCollectible(tokenMetadata, token)
        }

    private fun mapToArtCollectible(
        file: FilePinnedDTO,
        artCollectibleBlockchainEntity: ArtCollectibleBlockchainEntity
    ) = with(file) {
        with(file.metadata) {
            ArtCollectible(
                id = BigInteger.valueOf(1L),
                name = name,
                imageUrl = imageUrl,
                description = description,
                author = AuthorInfo(
                    fullName = authorName,
                    contact = authorContact,
                    royalty = artCollectibleBlockchainEntity.royalty,
                    address = authorAddress
                )
            )
        }
    }
}
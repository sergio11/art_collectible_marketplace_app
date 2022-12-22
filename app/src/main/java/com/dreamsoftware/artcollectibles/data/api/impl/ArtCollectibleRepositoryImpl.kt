package com.dreamsoftware.artcollectibles.data.api.impl

import com.dreamsoftware.artcollectibles.data.api.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import com.dreamsoftware.artcollectibles.data.ipfs.config.PinataConfig
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.models.response.FilePinnedDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.AuthorInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger

class ArtCollectibleRepositoryImpl(
    private val artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
    private val pinataConfig: PinataConfig,
    private val ipfsDataSource: IpfsDataSource,
    private val walletDataSource: IWalletDataSource
) : IArtCollectibleRepository {

    override suspend fun getTokensOwned(): Iterable<ArtCollectible> = withContext(Dispatchers.Default) {
        val credentials = walletDataSource.loadCredentials()
        val tokenFiles = ipfsDataSource.fetchByOwnerAddress(credentials.address)
        val tokens = artCollectibleDataSource.getTokensOwned()
        tokenFiles.zip(tokens).map {
            mapToArtCollectible(it.first, it.second)
        }
    }

    override suspend fun getTokensCreated(): Iterable<ArtCollectible> = withContext(Dispatchers.Default) {
        val credentials = walletDataSource.loadCredentials()
        val tokenFiles = ipfsDataSource.fetchByCreatorAddress(credentials.address)
        val tokens = artCollectibleDataSource.getTokensCreated()
        tokenFiles.zip(tokens).map {
            mapToArtCollectible(it.first, it.second)
        }
    }

    override suspend fun getTokenById(tokenId: BigInteger): ArtCollectible = withContext(Dispatchers.Default) {
        val token = artCollectibleDataSource.getTokenById(tokenId)
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
                imageUrl = pinataConfig.pinataGatewayBaseUrl.plus(ipfsPinHash),
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
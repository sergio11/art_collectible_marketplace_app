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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.zip
import java.math.BigInteger

class ArtCollectibleRepositoryImpl(
    private val artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
    private val pinataConfig: PinataConfig,
    private val ipfsDataSource: IpfsDataSource,
    private val walletDataSource: IWalletDataSource
) : IArtCollectibleRepository {

    @OptIn(FlowPreview::class)
    override suspend fun getTokensOwned(): Flow<ArtCollectible> =
        walletDataSource.loadCredentials().flatMapConcat {
            ipfsDataSource.fetchByOwnerAddress(it.address)
                .zip(artCollectibleDataSource.getTokensOwned(), ::mapToArtCollectible)
        }

    @OptIn(FlowPreview::class)
    override suspend fun getTokensCreated(): Flow<ArtCollectible> =
        walletDataSource.loadCredentials().flatMapConcat {
            ipfsDataSource.fetchByCreatorAddress(it.address)
                .zip(artCollectibleDataSource.getTokensOwned(), ::mapToArtCollectible)
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
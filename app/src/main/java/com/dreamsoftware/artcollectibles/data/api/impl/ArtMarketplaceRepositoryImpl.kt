package com.dreamsoftware.artcollectibles.data.api.impl

import com.dreamsoftware.artcollectibles.data.api.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource

class ArtMarketplaceRepositoryImpl(
    private val artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
    private val ipfsDataSource: IpfsDataSource
): IArtMarketplaceRepository {
}
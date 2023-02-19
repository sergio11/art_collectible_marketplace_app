package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.repository.ITokenMetadataRepository
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource

/**
 * Token Metadata Repository
 * @param ipfsDataSource
 */
internal class ITokenMetadataRepositoryImpl(
    private val ipfsDataSource: IpfsDataSource
): ITokenMetadataRepository {
}
package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.mapper.CreateArtCollectibleMetadataMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.TokenMetadataMapper
import com.dreamsoftware.artcollectibles.data.api.repository.ITokenMetadataRepository
import com.dreamsoftware.artcollectibles.data.database.datasource.metadata.ITokenMetadataDatabaseDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectibleMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Token Metadata Repository
 * @param ipfsDataSource
 * @param tokenMetadataDatabaseDataSource
 * @param createArtCollectibleMetadataMapper
 * @param tokenMetadataMapper
 */
internal class TokenMetadataRepositoryImpl(
    private val ipfsDataSource: IpfsDataSource,
    private val tokenMetadataDatabaseDataSource: ITokenMetadataDatabaseDataSource,
    private val createArtCollectibleMetadataMapper: CreateArtCollectibleMetadataMapper,
    private val tokenMetadataMapper: TokenMetadataMapper
) : ITokenMetadataRepository {

    override suspend fun create(tokenMetadata: CreateArtCollectibleMetadata): ArtCollectibleMetadata =
        withContext(Dispatchers.Default) {
            try {
                ipfsDataSource.create(createArtCollectibleMetadataMapper.mapInToOut(tokenMetadata))
                    .let {
                        tokenMetadataMapper.mapInToOut(it)
                    }
            } catch (ex: Exception) {
                throw ex
            }
        }

    override suspend fun delete(tokenCID: String) {
        withContext(Dispatchers.Default) {
            try {
                // Delete token metadata from IPFS
                ipfsDataSource.delete(tokenCID)
            } catch (ex: Exception) {
                throw ex
            }
        }
    }

    override suspend fun fetchByOwnerAddress(address: String): Iterable<ArtCollectibleMetadata> =
        withContext(Dispatchers.Default) {
            try {
                tokenMetadataMapper.mapInListToOutList(ipfsDataSource.fetchByOwnerAddress(address))
            } catch (ex: Exception) {
                throw ex
            }
        }

    override suspend fun fetchByCreatorAddress(address: String): Iterable<ArtCollectibleMetadata> =
        withContext(Dispatchers.Default) {
            try {
                tokenMetadataMapper.mapInListToOutList(ipfsDataSource.fetchByCreatorAddress(address))
            } catch (ex: Exception) {
                throw ex
            }
        }

    override suspend fun fetchByCid(cid: String): ArtCollectibleMetadata =
        withContext(Dispatchers.Default) {
            try {
                tokenMetadataMapper.mapInToOut(ipfsDataSource.fetchByCid(cid))
            } catch (ex: Exception) {
                throw ex
            }
        }
}
package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.mapper.CreateArtCollectibleMetadataMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.TokenMetadataToEntityMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.TokenMetadataEntityMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.TokenMetadataMapper
import com.dreamsoftware.artcollectibles.data.api.repository.ITokenMetadataRepository
import com.dreamsoftware.artcollectibles.data.database.datasource.metadata.ITokenMetadataDatabaseDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectibleMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * Token Metadata Repository
 * @param ipfsDataSource
 * @param tokenMetadataDatabaseDataSource
 * @param createArtCollectibleMetadataMapper
 * @param tokenMetadataMapper
 * @param tokenMetadataToEntityMapper
 * @param tokenMetadataEntityMapper
 */
internal class TokenMetadataRepositoryImpl(
    private val ipfsDataSource: IpfsDataSource,
    private val tokenMetadataDatabaseDataSource: ITokenMetadataDatabaseDataSource,
    private val createArtCollectibleMetadataMapper: CreateArtCollectibleMetadataMapper,
    private val tokenMetadataMapper: TokenMetadataMapper,
    private val tokenMetadataToEntityMapper: TokenMetadataToEntityMapper,
    private val tokenMetadataEntityMapper: TokenMetadataEntityMapper
) : ITokenMetadataRepository {

    override suspend fun create(tokenMetadata: CreateArtCollectibleMetadata): ArtCollectibleMetadata =
        withContext(Dispatchers.Default) {
            try {
                ipfsDataSource.create(createArtCollectibleMetadataMapper.mapInToOut(tokenMetadata))
                    .also {
                        tokenMetadataDatabaseDataSource.save(
                            tokenMetadataToEntityMapper.mapInToOut(
                                it
                            )
                        )
                    }.let {
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
                ipfsDataSource.delete(tokenCID).also {
                    tokenMetadataDatabaseDataSource.delete(tokenCID)
                }
            } catch (ex: Exception) {
                throw ex
            }
        }
    }

    override suspend fun fetchByAuthorAddress(address: String): Iterable<ArtCollectibleMetadata> =
        withContext(Dispatchers.Default) {
            try {
                tokenMetadataEntityMapper.mapInListToOutList(
                    tokenMetadataDatabaseDataSource.findByAuthorAddress(
                        address
                    )
                )
            } catch (ex: Exception) {
                ipfsDataSource.fetchByCreatorAddress(address).also {
                    tokenMetadataDatabaseDataSource.save(
                        tokenMetadataToEntityMapper.mapInListToOutList(
                            it
                        )
                    )
                }.let {
                    tokenMetadataMapper.mapInListToOutList(it)
                }
            }
        }

    override suspend fun fetchByCid(cid: String): ArtCollectibleMetadata =
        withContext(Dispatchers.Default) {
            try {
                tokenMetadataEntityMapper.mapInToOut(
                    tokenMetadataDatabaseDataSource.findOneByCid(cid)
                )
            } catch (ex: Exception) {
                ipfsDataSource.fetchByCid(cid).also {
                    tokenMetadataDatabaseDataSource.save(tokenMetadataToEntityMapper.mapInToOut(it))
                }.let {
                    tokenMetadataMapper.mapInToOut(it)
                }
            }
        }

    override suspend fun fetchByCid(cidList: Iterable<String>): Iterable<ArtCollectibleMetadata> =
        withContext(Dispatchers.Default) {
            try {
                tokenMetadataEntityMapper.mapInListToOutList(
                    tokenMetadataDatabaseDataSource.findByCidList(cidList)
                )
            } catch (ex: Exception) {
                cidList.map { async { ipfsDataSource.fetchByCid(it) } }
                    .awaitAll()
                    .also {
                        tokenMetadataDatabaseDataSource.save(tokenMetadataToEntityMapper.mapInListToOutList(it))
                    }
                    .let {
                        tokenMetadataMapper.mapInListToOutList(it)
                    }
            }
        }
}
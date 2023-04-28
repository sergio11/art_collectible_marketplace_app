package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.*
import com.dreamsoftware.artcollectibles.data.api.repository.ITokenMetadataRepository
import com.dreamsoftware.artcollectibles.data.database.datasource.metadata.ITokenMetadataDatabaseDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICategoriesDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.UpdateArtCollectibleMetadata
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
 * @param categoriesDataSource
 * @param updateArtCollectibleMetadataMapper
 */
internal class TokenMetadataRepositoryImpl(
    private val ipfsDataSource: IpfsDataSource,
    private val tokenMetadataDatabaseDataSource: ITokenMetadataDatabaseDataSource,
    private val createArtCollectibleMetadataMapper: CreateArtCollectibleMetadataMapper,
    private val tokenMetadataMapper: TokenMetadataMapper,
    private val tokenMetadataToEntityMapper: TokenMetadataToEntityMapper,
    private val tokenMetadataEntityMapper: TokenMetadataEntityMapper,
    private val categoriesDataSource: ICategoriesDataSource,
    private val updateArtCollectibleMetadataMapper: UpdateArtCollectibleMetadataMapper
) : ITokenMetadataRepository {

    @Throws(CreateTokenMetadataDataException::class)
    override suspend fun create(tokenMetadata: CreateArtCollectibleMetadata): ArtCollectibleMetadata =
        withContext(Dispatchers.Default) {
            try {
                ipfsDataSource.create(createArtCollectibleMetadataMapper.mapInToOut(tokenMetadata))
                    .also {
                        tokenMetadataDatabaseDataSource.save(tokenMetadataToEntityMapper.mapInToOut(it))
                        categoriesDataSource.addToken(it.cid, it.categoryUid)
                    }.let {
                        tokenMetadataMapper.mapInToOut(
                            TokenMetadataMapper.InputData(
                                tokenMetadata = it,
                                category = categoriesDataSource.getByUid(it.categoryUid)
                            )
                        )
                    }
            } catch (ex: Exception) {
                throw CreateTokenMetadataDataException(
                    "An error occurred when trying to save token metadata",
                    ex
                )
            }
        }

    @Throws(UpdateTokenMetadataDataException::class)
    override suspend fun update(tokenMetadata: UpdateArtCollectibleMetadata): ArtCollectibleMetadata =
        withContext(Dispatchers.Default) {
            try {
                ipfsDataSource.update(updateArtCollectibleMetadataMapper.mapInToOut(tokenMetadata)).also {
                    tokenMetadataDatabaseDataSource.save(tokenMetadataToEntityMapper.mapInToOut(it))
                }.let {
                    tokenMetadataMapper.mapInToOut(
                        TokenMetadataMapper.InputData(
                            tokenMetadata = it,
                            category = categoriesDataSource.getByUid(it.categoryUid)
                        )
                    )
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw UpdateTokenMetadataDataException(
                    "An error occurred when trying to update token metadata",
                    ex
                )
            }
        }

    @Throws(DeleteTokenMetadataDataException::class)
    override suspend fun delete(tokenCID: String) {
        withContext(Dispatchers.Default) {
            try {
                // Delete token metadata from IPFS
                ipfsDataSource.delete(tokenCID).also {
                    tokenMetadataDatabaseDataSource.findOneByCid(tokenCID).let {
                        categoriesDataSource.removeToken(it.cid, it.categoryUid)
                        tokenMetadataDatabaseDataSource.delete(tokenCID)
                    }
                }
            } catch (ex: Exception) {
                throw DeleteTokenMetadataDataException(
                    "An error occurred when deleting token metadata",
                    ex
                )
            }
        }
    }

    @Throws(FetchByAuthorAddressDataException::class)
    override suspend fun fetchByAuthorAddress(address: String): Iterable<ArtCollectibleMetadata> =
        withContext(Dispatchers.Default) {
            try {
                tokenMetadataEntityMapper.mapInListToOutList(
                    tokenMetadataDatabaseDataSource.findByAuthorAddress(
                        address
                    ).map {
                        TokenMetadataEntityMapper.InputData(
                            tokenMetadata = it,
                            category = categoriesDataSource.getByUid(it.categoryUid)
                        )
                    }
                )
            } catch (ex: Exception) {
                try {
                    ipfsDataSource.fetchByCreatorAddress(address).also {
                        tokenMetadataDatabaseDataSource.save(
                            tokenMetadataToEntityMapper.mapInListToOutList(it)
                        )
                    }.map {
                        TokenMetadataMapper.InputData(
                            tokenMetadata = it,
                            category = categoriesDataSource.getByUid(it.categoryUid)
                        )
                    }.let {
                        tokenMetadataMapper.mapInListToOutList(it)
                    }
                } catch (ex: Exception) {
                    throw FetchByAuthorAddressDataException(
                        "An error occurred when trying to fetch token metadata",
                        ex
                    )
                }
            }
        }

    @Throws(FetchByCidDataException::class)
    override suspend fun fetchByCid(cid: String): ArtCollectibleMetadata =
        withContext(Dispatchers.Default) {
            try {
                tokenMetadataEntityMapper.mapInToOut(
                    tokenMetadataDatabaseDataSource.findOneByCid(cid).let {
                        TokenMetadataEntityMapper.InputData(
                            tokenMetadata = it,
                            category = categoriesDataSource.getByUid(it.categoryUid)
                        )
                    }
                )
            } catch (ex: Exception) {
                try {
                    ipfsDataSource.fetchByCid(cid).also {
                        tokenMetadataDatabaseDataSource.save(
                            tokenMetadataToEntityMapper.mapInToOut(it)
                        )
                    }.let {
                        tokenMetadataMapper.mapInToOut(
                            TokenMetadataMapper.InputData(
                                tokenMetadata = it,
                                category = categoriesDataSource.getByUid(it.categoryUid)
                            )
                        )
                    }
                } catch (ex: Exception) {
                    throw FetchByCidDataException(
                        "An error occurred when trying to fetch token metadata",
                        ex
                    )
                }
            }
        }

    @Throws(FetchByCidDataException::class)
    override suspend fun fetchByCid(cidList: Iterable<String>): Iterable<ArtCollectibleMetadata> =
        withContext(Dispatchers.Default) {
            try {
                tokenMetadataEntityMapper.mapInListToOutList(
                    tokenMetadataDatabaseDataSource.findByCidList(cidList).map {
                        TokenMetadataEntityMapper.InputData(
                            tokenMetadata = it,
                            category = categoriesDataSource.getByUid(it.categoryUid)
                        )
                    }
                )
            } catch (ex: Exception) {
                try {
                    cidList.map { async { ipfsDataSource.fetchByCid(it) } }
                        .awaitAll()
                        .also {
                            tokenMetadataDatabaseDataSource.save(
                                tokenMetadataToEntityMapper.mapInListToOutList(
                                    it
                                )
                            )
                        }.map {
                            TokenMetadataMapper.InputData(
                                tokenMetadata = it,
                                category = categoriesDataSource.getByUid(it.categoryUid)
                            )
                        }
                        .let {
                            tokenMetadataMapper.mapInListToOutList(it)
                        }
                } catch (ex: Exception) {
                    throw FetchByCidDataException(
                        "An error occurred when trying to fetch token metadata",
                        ex
                    )
                }
            }
        }
}
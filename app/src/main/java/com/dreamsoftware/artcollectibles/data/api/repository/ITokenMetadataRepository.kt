package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.UpdateArtCollectibleMetadata

interface ITokenMetadataRepository {

    @Throws(CreateTokenMetadataDataException::class)
    suspend fun create(tokenMetadata: CreateArtCollectibleMetadata): ArtCollectibleMetadata

    @Throws(UpdateTokenMetadataDataException::class)
    suspend fun update(tokenMetadata: UpdateArtCollectibleMetadata): ArtCollectibleMetadata

    @Throws(DeleteTokenMetadataDataException::class)
    suspend fun delete(tokenCID: String)

    @Throws(FetchByAuthorAddressDataException::class)
    suspend fun fetchByAuthorAddress(address: String): Iterable<ArtCollectibleMetadata>

    @Throws(FetchByCidDataException::class)
    suspend fun fetchByCid(cid: String): ArtCollectibleMetadata

    @Throws(FetchByCidDataException::class)
    suspend fun fetchByCid(cidList: Iterable<String>): Iterable<ArtCollectibleMetadata>

}
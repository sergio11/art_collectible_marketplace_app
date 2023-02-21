package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.CreateTokenMetadataDataException
import com.dreamsoftware.artcollectibles.data.api.exception.DeleteTokenMetadataDataException
import com.dreamsoftware.artcollectibles.data.api.exception.FetchByAuthorAddressDataException
import com.dreamsoftware.artcollectibles.data.api.exception.FetchByCidDataException
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectibleMetadata

interface ITokenMetadataRepository {

    @Throws(CreateTokenMetadataDataException::class)
    suspend fun create(tokenMetadata: CreateArtCollectibleMetadata): ArtCollectibleMetadata

    @Throws(DeleteTokenMetadataDataException::class)
    suspend fun delete(tokenCID: String)

    @Throws(FetchByAuthorAddressDataException::class)
    suspend fun fetchByAuthorAddress(address: String): Iterable<ArtCollectibleMetadata>

    @Throws(FetchByCidDataException::class)
    suspend fun fetchByCid(cid: String): ArtCollectibleMetadata

    @Throws(FetchByCidDataException::class)
    suspend fun fetchByCid(cidList: Iterable<String>): Iterable<ArtCollectibleMetadata>

}
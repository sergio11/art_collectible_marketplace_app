package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectibleMetadata

interface ITokenMetadataRepository {

    suspend fun create(tokenMetadata: CreateArtCollectibleMetadata): ArtCollectibleMetadata

    suspend fun delete(tokenCID: String)

    suspend fun fetchByAuthorAddress(address: String): Iterable<ArtCollectibleMetadata>

    suspend fun fetchByCid(cid: String): ArtCollectibleMetadata

    suspend fun fetchByCid(cidList: Iterable<String>): Iterable<ArtCollectibleMetadata>

}
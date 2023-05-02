package com.dreamsoftware.artcollectibles.data.ipfs.pinata.service

import com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.response.PinnedFilesResponseDTO
import retrofit2.http.*

/**
 * Pinata Query Files Service
 * =======================
 * GET -> https://api.pinata.cloud/data/pinList?status=pinned&hashContains={cid}
 * GET -> https://api.pinata.cloud/data/pinList?status=pinned&metadata[author_address]=123456
 */
interface IPinataQueryFilesService {

    /**
     * Get Pinned File By CID
     * This endpoint returns data on what content the sender has pinned to IPFS through Pinata.
     * The purpose of this endpoint is to provide insight into what is being pinned, and how long it has been pinned.
     * @param cid
     */
    @GET("data/pinList?status=pinned&includeCount=false")
    suspend fun getPinnedFileByCid(
        // CID of file you are searching for
        @Query("hashContains") cid: String
    ): PinnedFilesResponseDTO

    /**
     * Get Pinned File By Creator Address
     * @param creatorAddress
     */
    @GET("data/pinList?status=pinned")
    suspend fun getPinnedFileByCreatorAddress(@Query("metadata[author_address]") creatorAddress: String): PinnedFilesResponseDTO
}
package com.dreamsoftware.artcollectibles.data.ipfs.pinata.service

import com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.response.PinnedFilesResponseDTO
import retrofit2.http.*

/**
 * Pinata Query Files Service
 * =======================
 * GET -> https://api.pinata.cloud/data/pinList?status=pinned&ipfs_pin_hash={cid}
 * GET -> https://api.pinata.cloud/data/pinList?status=pinned&keyvalues[creator]=123456
 */
interface IPinataQueryFilesService {

    /**
     * Get Pinned File By CID
     * @param cid
     */
    @GET("data/pinList?status=pinned")
    suspend fun getPinnedFileByCid(@Query("ipfs_pin_hash") cid: String): PinnedFilesResponseDTO

    /**
     * Get Pinned File By Creator Address
     * @param creatorAddress
     */
    @GET("data/pinList?status=pinned")
    suspend fun getPinnedFileByCreatorAddress(@Query("keyvalues[author_address]") creatorAddress: String): PinnedFilesResponseDTO

    /**
     * Get Pinned File By Owner Address
     * @param ownerAddress
     */
    @GET("data/pinList?status=pinned")
    suspend fun getPinnedFileByOwnerAddress(@Query("keyvalues[owner_address]") ownerAddress: String): PinnedFilesResponseDTO
}
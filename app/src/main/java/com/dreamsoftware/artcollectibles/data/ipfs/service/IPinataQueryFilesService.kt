package com.dreamsoftware.artcollectibles.data.ipfs.service

import com.dreamsoftware.artcollectibles.data.ipfs.models.response.PinnedFilesResponseDTO
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
    @GET("data/pinList?status=pinned&ipfs_pin_hash={cid}")
    suspend fun getPinnedFileByCid(@Path("cid") cid: String): PinnedFilesResponseDTO

    /**
     * Get Pinned File By Creator Address
     * @param creatorAddress
     */
    @GET("data/pinList?status=pinned&keyvalues[author_address]={creatorAddress}")
    suspend fun getPinnedFileByCreatorAddress(@Path("creatorAddress") creatorAddress: String): PinnedFilesResponseDTO

    /**
     * Get Pinned File By Owner Address
     * @param ownerAddress
     */
    @GET("data/pinList?status=pinned&keyvalues[owner_address]={ownerAddress}")
    suspend fun getPinnedFileByOwnerAddress(@Path("ownerAddress") ownerAddress: String): PinnedFilesResponseDTO
}
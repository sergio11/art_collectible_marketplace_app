package com.dreamsoftware.artcollectibles.data.ipfs.service

import com.dreamsoftware.artcollectibles.data.ipfs.models.response.PinnedFilesResponseDTO
import retrofit2.http.*

/**
 * Pinata Query Files Service
 * =======================
 * GET -> https://api.pinata.cloud/data/pinList?status=pinned&pfs_pin_hash={cid}
 */
interface IPinataQueryFilesService {

    /**
     * Get Pinned File By CID
     * @param cid
     */
    @GET("data/pinList?status=pinned&pfs_pin_hash={cid}")
    suspend fun getPinnedFileByCid(@Path("cid") cid: String): PinnedFilesResponseDTO

}
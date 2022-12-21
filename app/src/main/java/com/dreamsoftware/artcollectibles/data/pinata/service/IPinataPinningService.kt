package com.dreamsoftware.artcollectibles.data.pinata.service

import com.dreamsoftware.artcollectibles.data.pinata.models.request.SavePinataMetadataDTO
import com.dreamsoftware.artcollectibles.data.pinata.models.request.UpdateMetadataDTO
import com.dreamsoftware.artcollectibles.data.pinata.models.response.PinFileToIpfsResponseDTO
import com.dreamsoftware.artcollectibles.data.pinata.models.response.PinnedFilesResponseDTO
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Pinata Pinning Service
 * =======================
 * POST -> https://api.pinata.cloud/pinning/pinFileToIPFS
 * DELETE -> https://api.pinata.cloud/pinning/unpin/{cid}
 * PUT -> https://api.pinata.cloud/pinning/hashMetadata
 * GET -> https://api.pinata.cloud/data/pinList?status=pinned&pfs_pin_hash={cid}
 */
interface IPinataPinningService {

    /**
     * Pin file to IPFS
     * @param file
     * @param metadata
     */
    @Multipart
    @POST("pinning/pinFileToIPFS")
    suspend fun pinFileToIPFS(
        @Part file: MultipartBody.Part,
        @Part(value = "pinataMetadata") metadata: SavePinataMetadataDTO
    ): PinFileToIpfsResponseDTO

    /**
     * Unpin File
     * @param cid
     */
    @DELETE("pinning/unpin/{cid}")
    suspend fun unpinFile(@Path("cid") cid: String)

    /**
     * Update Metadata
     */
    @PUT("pinning/hashMetadata")
    suspend fun updateMetadata(@Body updateMetadataDTO: UpdateMetadataDTO)

    /**
     * Get Pinned File By CID
     * @param cid
     */
    @GET("https://api.pinata.cloud/data/pinList?status=pinned&pfs_pin_hash={cid}")
    suspend fun getPinnedFileByCid(@Path("cid") cid: String): PinnedFilesResponseDTO

}
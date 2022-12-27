package com.dreamsoftware.artcollectibles.data.ipfs.pinata.service

import com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.request.FileMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.request.UpdateFileMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.response.PinFileToIpfsResponseDTO
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Pinata Pinning Service
 * =======================
 * POST -> https://api.pinata.cloud/pinning/pinFileToIPFS
 * DELETE -> https://api.pinata.cloud/pinning/unpin/{cid}
 * PUT -> https://api.pinata.cloud/pinning/hashMetadata
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
        @Part(value = "pinataMetadata") metadata: FileMetadataDTO
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
    suspend fun updateMetadata(@Body updateFileMetadataDTO: UpdateFileMetadataDTO)

}
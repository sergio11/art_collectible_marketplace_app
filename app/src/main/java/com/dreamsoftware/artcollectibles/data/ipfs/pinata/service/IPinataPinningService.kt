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
     * This endpoint will accept a single file or a single directory.
     * The request must include a read stream for the payload in order for the API to accept it.
     * @param file
     * @param metadata
     */
    @Multipart
    @POST("pinning/pinFileToIPFS")
    suspend fun pinFileToIPFS(
        // Read stream representing the file
        @Part file: MultipartBody.Part,
        // Optional stringified object
        @Part(value = "pinataMetadata") metadata: FileMetadataDTO
    ): PinFileToIpfsResponseDTO

    /**
     * The process of removing files from IPFS is called unpinning. When you unpin something from an IPFS storage node, it is marked for garbage collection.
     * When garbage collection runs, the content is permanently deleted from the storage node.
     * @param cid
     */
    @DELETE("pinning/unpin/{cid}")
    suspend fun unpinFile(@Path("cid") cid: String)

    /**
     * This endpoint allows the sender to change the name and custom key-values associated with a piece of content stored on Pinata.
     * Changes made via this endpoint only affect the metadata for the hash passed in.
     */
    @PUT("pinning/hashMetadata")
    suspend fun updateMetadata(@Body updateFileMetadataDTO: UpdateFileMetadataDTO)

}
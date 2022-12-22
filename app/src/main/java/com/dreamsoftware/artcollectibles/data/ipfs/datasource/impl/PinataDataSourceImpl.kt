package com.dreamsoftware.artcollectibles.data.ipfs.datasource.impl

import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.core.SupportNetworkDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.models.request.FileMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.request.UpdateFileMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.response.FilePinnedDTO
import com.dreamsoftware.artcollectibles.data.ipfs.service.IPinataPinningService
import com.dreamsoftware.artcollectibles.data.ipfs.service.IPinataQueryFilesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

internal class PinataDataSourceImpl(
    private val pinataPinningService: IPinataPinningService,
    private val pinataQueryFilesService: IPinataQueryFilesService
): SupportNetworkDataSource(), IpfsDataSource {

    override suspend fun saveFile(
        file: File,
        mediaType: String,
        metadataDTO: FileMetadataDTO
    ): Flow<FilePinnedDTO> = safeNetworkCallAsFlow {
        val requestBody = file.asRequestBody(mediaType.toMediaType())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val response = pinataPinningService.pinFileToIPFS(filePart, metadataDTO)
        pinataQueryFilesService.getPinnedFileByCid(response.ipfsHash).rows.first()
    }

    override suspend fun fetchByCid(cid: String): Flow<FilePinnedDTO>  = safeNetworkCallAsFlow {
        pinataQueryFilesService.getPinnedFileByCid(cid).rows.first()
    }

    override suspend fun fetchByCreatorAddress(creatorAddress: String): Flow<FilePinnedDTO> = safeNetworkCall {
        pinataQueryFilesService.getPinnedFileByCreatorAddress(creatorAddress)
            .rows
            .asFlow()
    }

    override suspend fun updateMetadata(metadata: UpdateFileMetadataDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(cid: String) = safeNetworkCallAsFlow {
        pinataPinningService.unpinFile(cid)
    }

}
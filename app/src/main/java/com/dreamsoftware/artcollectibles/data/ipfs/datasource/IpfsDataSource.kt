package com.dreamsoftware.artcollectibles.data.ipfs.datasource

import com.dreamsoftware.artcollectibles.data.ipfs.models.request.FileMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.request.UpdateFileMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.response.FilePinnedDTO
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IpfsDataSource {

    /**
     * Save file into IPFS
     * @param file
     * @param mediaType
     * @param metadataDTO
     */
    suspend fun saveFile(file: File, mediaType: String, metadataDTO: FileMetadataDTO): Flow<FilePinnedDTO>

    /**
     * Fetch file by CID
     * @param cid
     */
    suspend fun fetchByCid(cid: String): Flow<FilePinnedDTO>

    /**
     * Update Metadata
     * @param metadata
     */
    suspend fun updateMetadata(metadata: UpdateFileMetadataDTO)

    /**
     * Delete File by CID
     * @param cid
     *
     */
    suspend fun delete(cid: String): Flow<Unit>
}
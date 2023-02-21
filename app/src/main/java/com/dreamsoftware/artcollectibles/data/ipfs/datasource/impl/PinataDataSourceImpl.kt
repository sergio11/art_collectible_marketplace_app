package com.dreamsoftware.artcollectibles.data.ipfs.datasource.impl

import android.net.Uri
import com.dreamsoftware.artcollectibles.data.core.network.SupportNetworkDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.mapper.CreateTokenMetadataMapper
import com.dreamsoftware.artcollectibles.data.ipfs.mapper.TokenMetadataMapper
import com.dreamsoftware.artcollectibles.data.ipfs.mapper.UpdateTokenMetadataMapper
import com.dreamsoftware.artcollectibles.data.ipfs.models.CreateTokenMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.TokenMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.UpdateTokenMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.service.IPinataPinningService
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.service.IPinataQueryFilesService
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

internal class PinataDataSourceImpl(
    private val applicationAware: IApplicationAware,
    private val pinataPinningService: IPinataPinningService,
    private val pinataQueryFilesService: IPinataQueryFilesService,
    private val createTokenMetadataMapper: CreateTokenMetadataMapper,
    private val updateTokenMetadataMapper: UpdateTokenMetadataMapper,
    private val tokenMetadataMapper: TokenMetadataMapper
) : SupportNetworkDataSource(), IpfsDataSource {

    override suspend fun create(tokenMetadata: CreateTokenMetadataDTO): TokenMetadataDTO =
        safeNetworkCall {
            with(tokenMetadata) {
                val fileContents: ByteArray = applicationAware.resolveContentAsByteArray(Uri.parse(fileUri)) ?:
                    throw IllegalStateException("Content Uri cannot be opened")
                val requestBody = fileContents.toRequestBody(mediaType.toMediaType())
                val filePart = MultipartBody.Part.createFormData("file", "file", requestBody)
                val fileMetadataDTO = createTokenMetadataMapper.mapInToOut(tokenMetadata)
                val response = pinataPinningService.pinFileToIPFS(filePart, fileMetadataDTO)
                tokenMetadataMapper.mapInToOut(
                    pinataQueryFilesService.getPinnedFileByCid(response.ipfsHash)
                        .rows.first()
                )
            }
        }

    override suspend fun update(metadata: UpdateTokenMetadataDTO) = safeNetworkCall {
        val tokenMetadataToUpdate = updateTokenMetadataMapper.mapInToOut(metadata)
        pinataPinningService.updateMetadata(tokenMetadataToUpdate)
    }

    override suspend fun delete(cid: String) = safeNetworkCall {
        pinataPinningService.unpinFile(cid)
    }

    override suspend fun fetchByCid(cid: String): TokenMetadataDTO = safeNetworkCall {
        tokenMetadataMapper.mapInToOut(pinataQueryFilesService.getPinnedFileByCid(cid).rows.first())
    }

    override suspend fun fetchByCreatorAddress(creatorAddress: String): Iterable<TokenMetadataDTO> =
        safeNetworkCall {
            tokenMetadataMapper.mapInListToOutList(
                pinataQueryFilesService.getPinnedFileByCreatorAddress(creatorAddress)
                    .rows
            )
        }
}
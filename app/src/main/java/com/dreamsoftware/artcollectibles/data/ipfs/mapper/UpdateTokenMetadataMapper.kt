package com.dreamsoftware.artcollectibles.data.ipfs.mapper

import com.dreamsoftware.artcollectibles.data.ipfs.models.UpdateTokenMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.request.UpdateFileMetadataDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class UpdateTokenMetadataMapper: IOneSideMapper<UpdateTokenMetadataDTO, UpdateFileMetadataDTO> {

    internal companion object {
        const val TOKEN_DESCRIPTION_KEY = "description"
    }

    override fun mapInToOut(input: UpdateTokenMetadataDTO): UpdateFileMetadataDTO = with(input) {
        UpdateFileMetadataDTO(
            cid = cid,
            name = name,
            keyValues = hashMapOf<String, String>().apply {
                description?.let {
                    put(TOKEN_DESCRIPTION_KEY, it)
                }
            }
        )
    }

    override fun mapInListToOutList(input: Iterable<UpdateTokenMetadataDTO>): Iterable<UpdateFileMetadataDTO> =
        input.map(::mapInToOut)

}
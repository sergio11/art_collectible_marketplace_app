package com.dreamsoftware.artcollectibles.data.ipfs.mapper

import com.dreamsoftware.artcollectibles.data.ipfs.models.CreateTokenMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.request.FileMetadataDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class CreateTokenMetadataMapper: IOneSideMapper<CreateTokenMetadataDTO, FileMetadataDTO> {

    internal companion object {
        const val TOKEN_DESCRIPTION_KEY = "description"
        const val TOKEN_TAGS_KEY = "token_tags"
        const val TOKEN_AUTHOR_KEY = "author_address"
    }

    override fun mapInToOut(input: CreateTokenMetadataDTO): FileMetadataDTO = with(input) {
        FileMetadataDTO(
            name = name,
            keyValues = hashMapOf(
                TOKEN_DESCRIPTION_KEY to description.orEmpty(),
                TOKEN_AUTHOR_KEY to authorAddress,
                TOKEN_TAGS_KEY to tags.joinToString(separator = ",")
            )
        )
    }

    override fun mapInListToOutList(input: Iterable<CreateTokenMetadataDTO>): Iterable<FileMetadataDTO> =
        input.map(::mapInToOut)
}
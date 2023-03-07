package com.dreamsoftware.artcollectibles.data.ipfs.mapper

import com.dreamsoftware.artcollectibles.data.ipfs.models.CreateTokenMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.request.FileMetadataDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class CreateTokenMetadataMapper: IOneSideMapper<CreateTokenMetadataDTO, FileMetadataDTO> {

    internal companion object {
        const val TOKEN_DESCRIPTION_KEY = "description"
        const val TOKEN_TAGS_KEY = "token_tags"
        const val TOKEN_AUTHOR_KEY = "author_address"
        const val TOKEN_CATEGORY_UID_KEY = "category_uid"
    }

    override fun mapInToOut(input: CreateTokenMetadataDTO): FileMetadataDTO = with(input) {
        FileMetadataDTO(
            name = name,
            keyValues = hashMapOf(
                TOKEN_DESCRIPTION_KEY to description.orEmpty(),
                TOKEN_AUTHOR_KEY to authorAddress,
                TOKEN_TAGS_KEY to tags.joinToString(separator = ","),
                TOKEN_CATEGORY_UID_KEY to categoryUid
            )
        )
    }

    override fun mapInListToOutList(input: Iterable<CreateTokenMetadataDTO>): Iterable<FileMetadataDTO> =
        input.map(::mapInToOut)
}
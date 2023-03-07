package com.dreamsoftware.artcollectibles.data.ipfs.mapper

import com.dreamsoftware.artcollectibles.data.ipfs.config.PinataConfig
import com.dreamsoftware.artcollectibles.data.ipfs.models.TokenMetadataDTO
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.response.FilePinnedDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class TokenMetadataMapper(
    private val pinataConfig: PinataConfig
): IOneSideMapper<FilePinnedDTO, TokenMetadataDTO> {

    private companion object {
        const val TOKEN_DESCRIPTION_KEY = "description"
        const val TOKEN_TAGS_KEY = "token_tags"
        const val TOKEN_AUTHOR_KEY = "author_address"
        const val TOKEN_CATEGORY_UID_KEY = "category_uid"
    }

    override fun mapInToOut(input: FilePinnedDTO): TokenMetadataDTO = with(input) {
        with(metadata) {
            TokenMetadataDTO(
                cid = ipfsPinHash,
                name = name,
                description = keyValues[TOKEN_DESCRIPTION_KEY].orEmpty(),
                createdAt = datePinned,
                imageUrl = pinataConfig.pinataGatewayBaseUrl.plus(ipfsPinHash),
                tags = keyValues[TOKEN_TAGS_KEY]?.split(",") ?: emptyList(),
                authorAddress = keyValues[TOKEN_AUTHOR_KEY].orEmpty(),
                categoryUid = keyValues[TOKEN_CATEGORY_UID_KEY].orEmpty()
            )
        }
    }

    override fun mapInListToOutList(input: Iterable<FilePinnedDTO>): Iterable<TokenMetadataDTO> =
        input.map(::mapInToOut)
}
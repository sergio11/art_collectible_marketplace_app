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
        const val TOKEN_TAGS = "token_tags"
    }

    override fun mapInToOut(input: FilePinnedDTO): TokenMetadataDTO = with(input) {
        TokenMetadataDTO(
            cid = ipfsPinHash,
            name = metadata.name,
            description = metadata.keyValues[TOKEN_DESCRIPTION_KEY].orEmpty(),
            createdAt = datePinned,
            imageUrl = pinataConfig.pinataGatewayBaseUrl.plus(ipfsPinHash),
            tags = metadata.keyValues[TOKEN_TAGS]?.split(",") ?: emptyList<String>()
        )
    }

    override fun mapInListToOutList(input: Iterable<FilePinnedDTO>): Iterable<TokenMetadataDTO> =
        input.map(::mapInToOut)
}
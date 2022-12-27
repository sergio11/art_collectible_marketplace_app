package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.TokenMetadataDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class ArtCollectibleMapper(
    private val userInfoMapper: UserInfoMapper
): IOneSideMapper<Triple<TokenMetadataDTO, ArtCollectibleBlockchainDTO, UserDTO>, ArtCollectible> {

    override fun mapInToOut(input: Triple<TokenMetadataDTO, ArtCollectibleBlockchainDTO, UserDTO>): ArtCollectible =
        with(input) {
            ArtCollectible(
                id = second.tokenId,
                name = first.name,
                imageUrl = first.imageUrl,
                description = first.description.orEmpty(),
                royalty = second.royalty,
                author = userInfoMapper.mapInToOut(third)
            )
        }

    override fun mapInListToOutList(input: Iterable<Triple<TokenMetadataDTO, ArtCollectibleBlockchainDTO, UserDTO>>): Iterable<ArtCollectible> =
        input.map(::mapInToOut)
}
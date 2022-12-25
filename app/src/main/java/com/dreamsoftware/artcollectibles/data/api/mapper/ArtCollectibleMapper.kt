package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.response.FilePinnedDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class ArtCollectibleMapper(
    private val userInfoMapper: UserInfoMapper
): IOneSideMapper<Triple<FilePinnedDTO, ArtCollectibleBlockchainEntity, UserDTO>, ArtCollectible> {

    override fun mapInToOut(input: Triple<FilePinnedDTO, ArtCollectibleBlockchainEntity, UserDTO>): ArtCollectible =
        with(input) {
            ArtCollectible(
                id = second.tokenId,
                name = first.metadata.name,
                imageUrl = first.imageUrl,
                description = first.metadata.description,
                royalty = second.royalty,
                author = userInfoMapper.mapInToOut(third)
            )
        }

    override fun mapInListToOutList(input: Iterable<Triple<FilePinnedDTO, ArtCollectibleBlockchainEntity, UserDTO>>): Iterable<ArtCollectible> =
        input.map(::mapInToOut)
}
package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.TokenMetadataDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class ArtCollectibleMapper(
    private val userInfoMapper: UserInfoMapper
): IOneSideMapper<ArtCollectibleMapper.InputData, ArtCollectible> {

    override fun mapInToOut(input: InputData): ArtCollectible = with(input) {
        ArtCollectible(
            id = blockchain.tokenId,
            name = metadata.name,
            imageUrl = metadata.imageUrl,
            description = metadata.description.orEmpty(),
            royalty = blockchain.royalty,
            author = userInfoMapper.mapInToOut(author),
            owner = userInfoMapper.mapInToOut(owner),
            favoritesCount = favoritesCount,
            hasAddedToFav = hasAddedToFav,
            tags = metadata.tags,
            createdAt = metadata.createdAt
        )
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<ArtCollectible> =
        input.map(::mapInToOut)

    data class InputData(
        val metadata: TokenMetadataDTO,
        val blockchain: ArtCollectibleBlockchainDTO,
        val author: UserDTO,
        val owner: UserDTO,
        val favoritesCount: Long = 0L,
        val hasAddedToFav: Boolean = false
    )
}
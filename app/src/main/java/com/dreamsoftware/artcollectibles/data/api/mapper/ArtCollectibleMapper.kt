package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class ArtCollectibleMapper: IOneSideMapper<ArtCollectibleMapper.InputData, ArtCollectible> {

    override fun mapInToOut(input: InputData): ArtCollectible = with(input) {
        ArtCollectible(
            id = blockchain.tokenId,
            royalty = blockchain.royalty,
            metadata = metadata,
            author = author,
            owner = owner,
            favoritesCount = favoritesCount,
            hasAddedToFav = hasAddedToFav,
            visitorsCount = visitorsCount,
            commentsCount = commentsCount
        )
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<ArtCollectible> =
        input.map(::mapInToOut)

    data class InputData(
        val metadata: ArtCollectibleMetadata,
        val blockchain: ArtCollectibleBlockchainDTO,
        val author: UserInfo,
        val owner: UserInfo,
        val visitorsCount: Long = 0L,
        val favoritesCount: Long = 0L,
        val commentsCount: Long = 0L,
        val hasAddedToFav: Boolean = false
    )
}
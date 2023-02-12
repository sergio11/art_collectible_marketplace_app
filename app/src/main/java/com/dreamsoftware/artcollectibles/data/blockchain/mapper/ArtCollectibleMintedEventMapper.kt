package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleMintedEventDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class ArtCollectibleMintedEventMapper : IOneSideMapper<ArtCollectibleContract.ArtCollectibleMintedEventResponse, ArtCollectibleMintedEventDTO> {

    override fun mapInToOut(input: ArtCollectibleContract.ArtCollectibleMintedEventResponse): ArtCollectibleMintedEventDTO = with(input) {
        ArtCollectibleMintedEventDTO(
            tokenId, creator, metadata, royalty
        )
    }

    override fun mapInListToOutList(input: Iterable<ArtCollectibleContract.ArtCollectibleMintedEventResponse>): Iterable<ArtCollectibleMintedEventDTO> =
        input.map(::mapInToOut)

}
package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class ArtCollectibleMapper : IOneSideMapper<ArtCollectible, ArtCollectibleBlockchainDTO> {

    override fun mapInToOut(input: ArtCollectible): ArtCollectibleBlockchainDTO =
        with(input) {
            ArtCollectibleBlockchainDTO(
                tokenId = tokenId,
                creator = creator,
                owner = owner,
                royalty = royalty,
                metadataCID = metadataCID,
                isExist = isExist
            )
        }

    override fun mapInListToOutList(input: Iterable<ArtCollectible>): Iterable<ArtCollectibleBlockchainDTO> =
        input.map(::mapInToOut)

}
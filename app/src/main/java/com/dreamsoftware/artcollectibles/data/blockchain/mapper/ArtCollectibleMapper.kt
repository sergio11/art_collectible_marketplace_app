package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.math.BigInteger

class ArtCollectibleMapper : IOneSideMapper<ArtCollectible, ArtCollectibleBlockchainDTO> {

    override fun mapInToOut(input: ArtCollectible): ArtCollectibleBlockchainDTO =
        ArtCollectibleBlockchainDTO(
            tokenId = BigInteger.valueOf(0L),
            creator = input.creator,
            royalty = input.royalty,
            metadataCID = "",
            isExist = input.isExist
        )

    override fun mapInListToOutList(input: Iterable<ArtCollectible>): Iterable<ArtCollectibleBlockchainDTO> =
        input.map(::mapInToOut)

}
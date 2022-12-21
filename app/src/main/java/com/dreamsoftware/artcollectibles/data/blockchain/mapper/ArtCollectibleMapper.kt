package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import com.dreamsoftware.artcollectibles.utils.IMapper

class ArtCollectibleMapper : IMapper<ArtCollectible, ArtCollectibleBlockchainEntity> {

    override fun mapInToOut(input: ArtCollectible): ArtCollectibleBlockchainEntity =
        ArtCollectibleBlockchainEntity(
            creator = input.creator, royalty = input.royalty, isExist = input.isExist
        )

    override fun mapInListToOutList(input: Iterable<ArtCollectible>): Iterable<ArtCollectibleBlockchainEntity> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: ArtCollectibleBlockchainEntity): ArtCollectible =
        ArtCollectible(
            input.creator, input.royalty, input.isExist
        )

    override fun mapOutListToInList(input: Iterable<ArtCollectibleBlockchainEntity>): Iterable<ArtCollectible> =
        input.map(::mapOutToIn)
}
package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity

class ArtMarketplaceMapper : IMapper<ArtCollectibleForSale, ArtCollectibleForSaleEntity> {

    override fun mapInToOut(input: ArtCollectibleForSale): ArtCollectibleForSaleEntity = with(input) {
        ArtCollectibleForSaleEntity(
            marketItemId = marketItemId,
            tokenId = tokenId,
            creator = creator,
            seller = seller,
            owner = owner,
            price = price,
            sold = sold,
            canceled = canceled
        )
    }

    override fun mapInListToOutList(input: Iterable<ArtCollectibleForSale>): Iterable<ArtCollectibleForSaleEntity> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: ArtCollectibleForSaleEntity): ArtCollectibleForSale = with(input) {
        ArtCollectibleForSale(
            marketItemId,
            tokenId,
            creator,
            seller,
            owner,
            price,
            sold,
            canceled
        )
    }

    override fun mapOutListToInList(input: Iterable<ArtCollectibleForSaleEntity>): Iterable<ArtCollectibleForSale> =
        input.map(::mapOutToIn)
}
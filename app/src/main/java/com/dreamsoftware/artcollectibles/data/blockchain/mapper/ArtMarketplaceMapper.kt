package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.utils.IMapper

class ArtMarketplaceMapper : IMapper<ArtCollectibleForSale, ArtCollectibleForSaleDTO> {

    override fun mapInToOut(input: ArtCollectibleForSale): ArtCollectibleForSaleDTO = with(input) {
        ArtCollectibleForSaleDTO(
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

    override fun mapInListToOutList(input: Iterable<ArtCollectibleForSale>): Iterable<ArtCollectibleForSaleDTO> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: ArtCollectibleForSaleDTO): ArtCollectibleForSale = with(input) {
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

    override fun mapOutListToInList(input: Iterable<ArtCollectibleForSaleDTO>): Iterable<ArtCollectibleForSale> =
        input.map(::mapOutToIn)
}
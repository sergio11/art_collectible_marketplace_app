package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.util.*

class ArtMarketplaceMapper(
    private val artCollectibleForSalePricesMapper: ArtCollectibleForSalePricesMapper
) : IOneSideMapper<ArtCollectibleForSale, ArtCollectibleForSaleDTO> {

    override fun mapInToOut(input: ArtCollectibleForSale): ArtCollectibleForSaleDTO = with(input) {
        ArtCollectibleForSaleDTO(
            marketItemId = marketItemId,
            metadataCID = metadataCID,
            tokenId = tokenId,
            creator = creator,
            seller = seller,
            owner = owner,
            prices = artCollectibleForSalePricesMapper.mapInToOut(price),
            sold = sold,
            canceled = canceled,
            putForSaleAt = Date(putForSaleAt.toLong() * 1000),
            soldAt = if(soldAt != null) {
                Date(soldAt.toLong() * 1000)
            } else {
                null
            },
            canceledAt = if(canceledAt != null) {
                Date(canceledAt.toLong() * 1000)
            } else {
                null
            }
        )
    }

    override fun mapInListToOutList(input: Iterable<ArtCollectibleForSale>): Iterable<ArtCollectibleForSaleDTO> =
        input.map(::mapInToOut)
}
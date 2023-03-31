package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import org.web3j.utils.Convert
import java.math.BigInteger
import java.util.*

class ArtMarketplaceMapper : IOneSideMapper<ArtCollectibleForSale, ArtCollectibleForSaleDTO> {

    override fun mapInToOut(input: ArtCollectibleForSale): ArtCollectibleForSaleDTO = with(input) {
        ArtCollectibleForSaleDTO(
            marketItemId = marketItemId,
            metadataCID = metadataCID,
            tokenId = tokenId,
            creator = creator,
            seller = seller,
            owner = owner,
            price = convertFromWeiToEth(price),
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

    private fun convertFromWeiToEth(priceInWei: BigInteger) =
        Convert.fromWei(priceInWei.toString(), Convert.Unit.WEI)
            .toBigInteger()
}
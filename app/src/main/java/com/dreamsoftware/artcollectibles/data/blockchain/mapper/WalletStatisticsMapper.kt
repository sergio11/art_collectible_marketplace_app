package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract
import com.dreamsoftware.artcollectibles.data.blockchain.model.WalletStatisticsDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class WalletStatisticsMapper : IOneSideMapper<ArtMarketplaceContract.WalletStatistics, WalletStatisticsDTO> {

    override fun mapInToOut(input: ArtMarketplaceContract.WalletStatistics): WalletStatisticsDTO =
        with(input) {
            WalletStatisticsDTO(
                countTokenSold, countTokenBought, countTokenWithdrawn
            )
        }

    override fun mapInListToOutList(input: Iterable<ArtMarketplaceContract.WalletStatistics>): Iterable<WalletStatisticsDTO> =
        input.map(::mapInToOut)

}
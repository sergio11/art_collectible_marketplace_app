package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
import com.dreamsoftware.artcollectibles.data.blockchain.model.TokenStatisticsDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class TokenStatisticsMapper : IOneSideMapper<ArtCollectibleContract.TokenStatistics, TokenStatisticsDTO> {

    override fun mapInToOut(input: ArtCollectibleContract.TokenStatistics): TokenStatisticsDTO =
        with(input) {
            TokenStatisticsDTO(
                countTokensCreator, countTokensOwned
            )
        }

    override fun mapInListToOutList(input: Iterable<ArtCollectibleContract.TokenStatistics>): Iterable<TokenStatisticsDTO> =
        input.map(::mapInToOut)

}
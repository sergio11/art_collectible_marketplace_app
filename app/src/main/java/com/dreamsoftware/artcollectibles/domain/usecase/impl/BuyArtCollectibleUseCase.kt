package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IStatisticsRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class BuyArtCollectibleUseCase(
    private val artCollectibleMarketplaceRepository: IArtMarketplaceRepository,
    private val statisticsRepository: IStatisticsRepository,
    private val preferenceRepository: IPreferenceRepository
): BaseUseCaseWithParams<BuyArtCollectibleUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        with(params) {
            artCollectibleMarketplaceRepository.buyItem(tokenId)
            with(statisticsRepository) {
                val authUserUid = preferenceRepository.getAuthUserUid()
                registerNewPurchase(authUserUid)
                registerNewSale(sellerUid)
            }
        }
    }

    data class Params(
        val sellerUid: String,
        val tokenId: BigInteger
    )
}
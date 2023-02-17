package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IFavoritesRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class AddTokenToFavoritesUseCase(
    private val favoritesRepository: IFavoritesRepository
): BaseUseCaseWithParams<AddTokenToFavoritesUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        with(params) {
            favoritesRepository.add(tokenId, userAddress)
        }
    }

    data class Params(
        val tokenId: String,
        val userAddress: String
    )
}
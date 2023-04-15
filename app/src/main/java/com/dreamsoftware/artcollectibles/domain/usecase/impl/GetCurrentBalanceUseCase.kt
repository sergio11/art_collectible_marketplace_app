package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetCurrentBalanceUseCase(
    private val walletRepository: IWalletRepository
): BaseUseCaseWithParams<GetCurrentBalanceUseCase.Params, AccountBalance>() {

    override suspend fun onExecuted(params: Params): AccountBalance = with(params) {
        with(walletRepository) {
            targetAddress?.let {
                getBalanceOf(it)
            } ?: getCurrentBalance()
        }
    }

    data class Params(
        val targetAddress: String? = null
    )
}
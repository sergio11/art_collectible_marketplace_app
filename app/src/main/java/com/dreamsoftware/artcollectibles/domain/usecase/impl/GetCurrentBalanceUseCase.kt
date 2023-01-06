package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetCurrentBalanceUseCase(
    private val walletRepository: IWalletRepository
): BaseUseCase<AccountBalance>() {
    override suspend fun onExecuted(): AccountBalance =
        walletRepository.getCurrentBalance()
}
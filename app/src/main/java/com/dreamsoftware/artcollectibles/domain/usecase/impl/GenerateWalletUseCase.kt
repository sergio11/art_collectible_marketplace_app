package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GenerateWalletUseCase(
    private val walletRepository: IWalletRepository
): BaseUseCaseWithParams<GenerateWalletUseCase.Params, UserWalletCredentials>() {

    override suspend fun onExecuted(params: Params): UserWalletCredentials {
        TODO()
    }

    data class Params(
        val email: String,
        val password: String
    )
}
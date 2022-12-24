package com.dreamsoftware.artcollectibles.data.api

import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials

interface IWalletRepository {

    suspend fun loadCredentials(): UserWalletCredentials

    suspend fun generate(password: String): UserWalletCredentials
}
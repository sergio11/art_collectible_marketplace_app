package com.dreamsoftware.artcollectibles.data.api.impl

import com.dreamsoftware.artcollectibles.data.api.IWalletRepository
import com.dreamsoftware.artcollectibles.data.api.exception.DataRepositoryException
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.exception.BlockchainDataSourceException
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials

internal class WalletRepositoryImpl(
    private val userCredentialsMapper: UserCredentialsMapper,
    private val walletDataSource: IWalletDataSource
): IWalletRepository {

    override suspend fun loadCredentials(): UserWalletCredentials {
        TODO("Not yet implemented")
    }

    override suspend fun generate(password: String): UserWalletCredentials = try {
        val credentials = walletDataSource.generate(password)
        userCredentialsMapper.mapInToOut(credentials)
    } catch (ex: BlockchainDataSourceException) {
        throw DataRepositoryException("An error occurred when generating a new wallet", ex)
    }
}
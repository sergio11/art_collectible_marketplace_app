package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.model.AccountBalanceDTO
import org.web3j.crypto.Credentials


interface IAccountBlockchainDataSource {

    suspend fun getCurrentBalance(credentials: Credentials): AccountBalanceDTO

    suspend fun getBalanceOf(targetAddress: String): AccountBalanceDTO

}
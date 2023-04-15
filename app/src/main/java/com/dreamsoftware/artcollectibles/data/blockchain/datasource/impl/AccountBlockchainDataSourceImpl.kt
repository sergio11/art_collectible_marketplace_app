package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.alchemy.models.request.AlchemyRequestDTO
import com.dreamsoftware.artcollectibles.data.blockchain.alchemy.service.IAccountInformationService
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IAccountBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.AccountBalanceDTO
import com.dreamsoftware.artcollectibles.data.core.network.SupportNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert
import java.math.BigInteger

/**
 * Account Blockchain Data Source Impl
 * @param accountInformationService
 * @param web3j
 */
internal class AccountBlockchainDataSourceImpl(
    private val accountInformationService: IAccountInformationService,
    private val web3j: Web3j
): SupportNetworkDataSource(),  IAccountBlockchainDataSource {

    override suspend fun getCurrentBalance(credentials: Credentials): AccountBalanceDTO =
        withContext(Dispatchers.IO) {
            val ethGetBalance = web3j
                .ethGetBalance(credentials.address, DefaultBlockParameterName.LATEST)
                .send()
            AccountBalanceDTO(
                balanceInWei = ethGetBalance.balance,
                balanceInEth = Convert.fromWei(ethGetBalance.balance.toString(), Convert.Unit.ETHER)
            )
        }

    override suspend fun getBalanceOf(targetAddress: String): AccountBalanceDTO = safeNetworkCall {
        val response = accountInformationService.getNativeBalance(AlchemyRequestDTO(
            id = 1,
            jsonRpc = "2.0",
            params = listOf(targetAddress, "latest"),
            method = "eth_getBalance"
        ))
        val balanceInWei = BigInteger(response.result.removePrefix("0x"), 16)
        AccountBalanceDTO(
            balanceInWei = balanceInWei,
            balanceInEth = Convert.fromWei(balanceInWei.toString(), Convert.Unit.ETHER)
        )
    }
}
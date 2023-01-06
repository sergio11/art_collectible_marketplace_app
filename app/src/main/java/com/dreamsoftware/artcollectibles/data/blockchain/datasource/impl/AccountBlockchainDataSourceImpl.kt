package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.alchemy.models.request.AlchemyRequestDTO
import com.dreamsoftware.artcollectibles.data.blockchain.alchemy.service.IAccountInformationService
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IAccountBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.AccountBalanceDTO
import com.dreamsoftware.artcollectibles.data.core.network.SupportNetworkDataSource
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
        safeNetworkCall {
            val response = accountInformationService.getNativeBalance(AlchemyRequestDTO(
                id = 1,
                jsonRpc = "2.0",
                params = listOf(credentials.address, "latest"),
                method = "eth_getBalance"
            ))
            val ethGetBalance = web3j
                .ethGetBalance(credentials.address, DefaultBlockParameterName.LATEST)
                .send()
            AccountBalanceDTO(
                erc20 = BigInteger(response.result.removePrefix("0x"), 16),
                balanceInWei = ethGetBalance.balance,
                balanceInEth = Convert.fromWei(ethGetBalance.balance.toString(), Convert.Unit.ETHER)
            )
        }
}
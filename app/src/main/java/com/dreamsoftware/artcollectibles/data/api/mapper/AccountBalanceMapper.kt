package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.model.AccountBalanceDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.MarketPricesDTO
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class AccountBalanceMapper: IOneSideMapper<AccountBalanceMapper.InputData, AccountBalance> {

    override fun mapInToOut(input: InputData): AccountBalance = with(input) {
        AccountBalance(
            balanceInWei = accountBalanceDTO.balanceInWei,
            balanceInEth = accountBalanceDTO.balanceInEth,
            balanceInEUR = marketPricesDTO.priceInEUR,
            balanceInUSD = marketPricesDTO.priceInUSD
        )
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<AccountBalance> =
        input.map(::mapInToOut)

    data class InputData(
        val accountBalanceDTO: AccountBalanceDTO,
        val marketPricesDTO: MarketPricesDTO
    )
}
package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.model.AccountBalanceDTO
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class AccountBalanceMapper: IOneSideMapper<AccountBalanceDTO, AccountBalance> {

    override fun mapInToOut(input: AccountBalanceDTO): AccountBalance = with(input) {
        AccountBalance(
            erc20, balanceInWei, balanceInEth
        )
    }

    override fun mapInListToOutList(input: Iterable<AccountBalanceDTO>): Iterable<AccountBalance> =
        input.map(::mapInToOut)
}
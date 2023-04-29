package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.MarketStatisticDTO
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class UserMarketStatisticMapper: IOneSideMapper<UserMarketStatisticMapper.InputData, UserMarketStatistic> {

    override fun mapInToOut(input: InputData): UserMarketStatistic = with(input) {
        UserMarketStatistic(
            key = marketStatisticDTO.key,
            value = marketStatisticDTO.value,
            userInfo = userInfo
        )
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<UserMarketStatistic> =
        input.map(::mapInToOut)

    data class InputData(
        val userInfo: UserInfo,
        val marketStatisticDTO: MarketStatisticDTO
    )
}
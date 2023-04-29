package com.dreamsoftware.artcollectibles.domain.models

data class UserMarketStatistic(
    val key: String,
    val value: Long,
    val userInfo: UserInfo
)

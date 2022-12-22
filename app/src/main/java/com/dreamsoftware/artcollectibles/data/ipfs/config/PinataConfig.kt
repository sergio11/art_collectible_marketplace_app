package com.dreamsoftware.artcollectibles.data.ipfs.config

import com.dreamsoftware.artcollectibles.BuildConfig

class PinataConfig {

    val pinataBaseUrl: String
        get() = BuildConfig.PINATA_BASE_URL

    val pinataGatewayBaseUrl: String
        get() = BuildConfig.PINATA_GATEWAY_BASE_URL
}

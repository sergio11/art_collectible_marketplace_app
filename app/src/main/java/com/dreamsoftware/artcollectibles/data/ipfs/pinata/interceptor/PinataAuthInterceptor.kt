package com.dreamsoftware.artcollectibles.data.ipfs.pinata.interceptor

import com.dreamsoftware.artcollectibles.data.ipfs.config.PinataConfig
import okhttp3.Interceptor
import okhttp3.Response

class PinataAuthInterceptor(
    private val pinataConfig: PinataConfig
): Interceptor {

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        proceed(request()
            .newBuilder()
            .addHeader(AUTHORIZATION_HEADER, "Bearer ${pinataConfig.pinataApiKey}")
            .build())
    }
}
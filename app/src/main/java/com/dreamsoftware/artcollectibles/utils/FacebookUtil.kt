package com.dreamsoftware.artcollectibles.utils

import com.facebook.CallbackManager

object FacebookUtil {
    val callbackManager by lazy {
        CallbackManager.Factory.create()
    }
}
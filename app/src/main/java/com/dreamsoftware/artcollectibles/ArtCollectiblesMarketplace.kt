/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dreamsoftware.artcollectibles

import android.app.Application
import com.dreamsoftware.artcollectibles.domain.models.Secret
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArtCollectiblesMarketplace : Application(), IApplicationAware {

    private var sessionUserSecret: Secret? = null

    override fun getApplicationId(): String = BuildConfig.APPLICATION_ID

    override fun getFileProviderAuthority(): String = BuildConfig.APPLICATION_ID + ".provider"

    override fun setUserSecret(secret: Secret?) {
        sessionUserSecret = secret
    }

    override fun getUserSecret(): Secret = sessionUserSecret ?: throw IllegalStateException("User secret has not been configured")
}

package com.dreamsoftware.artcollectibles.utils

import android.net.Uri
import com.dreamsoftware.artcollectibles.domain.models.PBEData
import java.io.InputStream

interface IApplicationAware {

    /**
     * Get Application Id
     */
    fun getApplicationId(): String

    /**
     * Get File Provider Authority
     */
    fun getFileProviderAuthority(): String

    fun getMasterSecret(): PBEData

    fun setUserSecret(PBEData: PBEData?)

    fun getUserSecret(): PBEData

    fun resolveContentAsByteArray(contentUri: Uri): ByteArray?

    fun getDeviceName(): String
}
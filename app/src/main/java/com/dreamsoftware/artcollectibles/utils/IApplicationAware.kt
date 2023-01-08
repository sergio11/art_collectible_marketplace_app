package com.dreamsoftware.artcollectibles.utils

import com.dreamsoftware.artcollectibles.domain.models.PBEData

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
}
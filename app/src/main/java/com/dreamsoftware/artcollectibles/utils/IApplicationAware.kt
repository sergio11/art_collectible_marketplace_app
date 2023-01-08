package com.dreamsoftware.artcollectibles.utils

import com.dreamsoftware.artcollectibles.domain.models.Secret

interface IApplicationAware {

    /**
     * Get Application Id
     */
    fun getApplicationId(): String

    /**
     * Get File Provider Authority
     */
    fun getFileProviderAuthority(): String

    fun setUserSecret(secret: Secret?)

    fun getUserSecret(): Secret
}
package com.dreamsoftware.artcollectibles.utils

interface IApplicationAware {

    /**
     * Get Application Id
     */
    fun getApplicationId(): String

    /**
     * Get File Provider Authority
     */
    fun getFileProviderAuthority(): String

    fun setUserSecretKey(key: String?)

    fun getUserSecretKey(): String
}
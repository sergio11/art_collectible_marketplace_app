package com.dreamsoftware.artcollectibles.data.preferences.datasource


interface IPreferencesDataSource {

    suspend fun saveWalletPassword(password: String)

    suspend fun getWalletPassword(): String

    suspend fun clearData()
}
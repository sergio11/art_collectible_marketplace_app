package com.dreamsoftware.artcollectibles.data.preferences.datasource

import kotlinx.coroutines.flow.Flow

interface IPreferencesDataSource {

    suspend fun saveWalletPassword(password: String)

    suspend fun getWalletPassword(): Flow<String>

    suspend fun clearData()
}
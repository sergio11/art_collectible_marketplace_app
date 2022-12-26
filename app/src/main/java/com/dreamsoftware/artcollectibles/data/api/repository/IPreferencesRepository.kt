package com.dreamsoftware.artcollectibles.data.api.repository

interface IPreferencesRepository {

    suspend fun saveAuthUserUid(uid: String)

    suspend fun getAuthUserUid(): String

    suspend fun clearData()
}
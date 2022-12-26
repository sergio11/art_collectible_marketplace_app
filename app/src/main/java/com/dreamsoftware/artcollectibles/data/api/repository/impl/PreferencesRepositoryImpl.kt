package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IPreferencesRepository
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource

internal class PreferencesRepositoryImpl(
    private val preferencesDataSource: IPreferencesDataSource
): IPreferencesRepository {

    override suspend fun saveAuthUserUid(uid: String) {
        preferencesDataSource.saveAuthUserUid(uid)
    }

    override suspend fun getAuthUserUid(): String =
        preferencesDataSource.getAuthUserUid()

    override suspend fun clearData() {
        preferencesDataSource.clearData()
    }
}
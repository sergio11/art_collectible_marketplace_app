package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.PreferenceDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource

/**
 * Preference Repository
 * @param preferenceDataSource
 */
internal class PreferenceRepositoryImpl(
    private val preferenceDataSource: IPreferencesDataSource
): IPreferenceRepository {

    @Throws(PreferenceDataException::class)
    override suspend fun saveAuthUserUid(uid: String) = try {
        preferenceDataSource.saveAuthUserUid(uid)
    } catch (ex: Exception) {
        throw PreferenceDataException("An error occurred when trying to save user uid", ex)
    }

    @Throws(PreferenceDataException::class)
    override suspend fun getAuthUserUid(): String = try {
        preferenceDataSource.getAuthUserUid()
    } catch (ex: Exception) {
        throw PreferenceDataException("An error occurred when trying to get user uid", ex)
    }

    @Throws(PreferenceDataException::class)
    override suspend fun clearData() = try {
        preferenceDataSource.clearData()
    } catch (ex: Exception) {
        throw PreferenceDataException("An error occurred when trying to clear data", ex)
    }
}
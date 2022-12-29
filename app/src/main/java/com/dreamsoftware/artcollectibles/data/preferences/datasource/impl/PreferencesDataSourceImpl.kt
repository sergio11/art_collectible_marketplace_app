package com.dreamsoftware.artcollectibles.data.preferences.datasource.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Preferences Data Source Impl
 * @param cryptoUtils
 * @param dataStore
 */
internal class PreferencesDataSourceImpl(
    private val cryptoUtils: CryptoUtils,
    private val dataStore: DataStore<Preferences>
) : IPreferencesDataSource {

    private companion object {
        @JvmStatic
        val AUTH_USER_UID_KEY = stringPreferencesKey("auth_user_uid_key")
    }

    override suspend fun saveAuthUserUid(uid: String) {
        dataStore.edit {
            it[AUTH_USER_UID_KEY] = cryptoUtils.encryptAndEncode(uid)
        }
    }

    override suspend fun getAuthUserUid(): String = dataStore.data.map {
        cryptoUtils.decodeAndDecrypt(it[AUTH_USER_UID_KEY].orEmpty())
    }.first()

    override suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }

}
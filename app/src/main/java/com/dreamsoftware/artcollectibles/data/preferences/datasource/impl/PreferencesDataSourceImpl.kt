package com.dreamsoftware.artcollectibles.data.preferences.datasource.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Preferences Data Source Impl
 * @param applicationAware
 * @param cryptoUtils
 * @param dataStore
 */
internal class PreferencesDataSourceImpl(
    private val applicationAware: IApplicationAware,
    private val cryptoUtils: CryptoUtils,
    private val dataStore: DataStore<Preferences>
) : IPreferencesDataSource {

    private companion object {
        @JvmStatic
        val AUTH_USER_UID_KEY = stringPreferencesKey("auth_user_uid_key")
    }

    override suspend fun saveAuthUserUid(uid: String) {
        with(applicationAware.getUserSecret()) {
            dataStore.edit {
                it[AUTH_USER_UID_KEY] = cryptoUtils.encryptAndEncode(secret, salt, uid)
            }
        }
    }

    override suspend fun getAuthUserUid(): String = dataStore.data.map {
        with(applicationAware.getUserSecret()) {
            cryptoUtils.decodeAndDecrypt(secret, salt, it[AUTH_USER_UID_KEY].orEmpty())
        }
    }.first()

    override suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }
}
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
        val SECURITY_KEY_ALIAS = "preferences"

        @JvmStatic
        val BYTES_SEPARATOR = "|"

        @JvmStatic
        val AUTH_USER_UID_KEY = stringPreferencesKey("auth_user_uid_key")

        @JvmStatic
        val WALLET_PASSWORD_KEY = stringPreferencesKey("wallet_password_key")
    }

    override suspend fun saveAuthUserUid(uid: String) {
        dataStore.edit {
            val encryptedValue = cryptoUtils.encryptData(SECURITY_KEY_ALIAS, uid)
            it[AUTH_USER_UID_KEY] = encryptedValue.joinToString(BYTES_SEPARATOR)
        }
    }

    override suspend fun getAuthUserUid(): String = dataStore.data.map {
        val authUserUidEncrypted = it[AUTH_USER_UID_KEY].orEmpty()
        cryptoUtils.decryptData(SECURITY_KEY_ALIAS, authUserUidEncrypted.toByteArray())
    }.first()

    /*override suspend fun saveWalletPassword(password: String) {
        dataStore.edit {
            val encryptedValue = cryptoUtils.encryptData(SECURITY_KEY_ALIAS, password)
            it[WALLET_PASSWORD_KEY] = encryptedValue.joinToString(BYTES_SEPARATOR)
        }
    }

    override suspend fun getWalletPassword(): String =
        dataStore.data.map {
            val walletPasswordEncrypted = it[WALLET_PASSWORD_KEY].orEmpty()
            cryptoUtils.decryptData(SECURITY_KEY_ALIAS, walletPasswordEncrypted.toByteArray())
        }.first()*/

    override suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }

}
package com.athallah.ecommerce.data.datasource.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.athallah.ecommerce.data.datasource.api.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStore(private val dataStore: DataStore<Preferences>) {

    private val language = stringPreferencesKey("language")
    private val refToken = stringPreferencesKey("ref_token")
    private val accToken = stringPreferencesKey("acc_token")
    private val isOnboard = booleanPreferencesKey("is_onboard")
    private val light = booleanPreferencesKey("light_theme")
    private val username = stringPreferencesKey("username")
    private val isLogin = booleanPreferencesKey("is_login")
    private val userImage = stringPreferencesKey("user_image")
    private val userExpires = intPreferencesKey("user_expires")





    fun getLanguage(): Flow<String> {
        return dataStore.data.map {
            it[language] ?: "en"
        }
    }

    suspend fun setLanguage(value: String) {
        dataStore.edit {
            it[language] = value
        }
    }

    fun getRefToken(): Flow<String> {
        return dataStore.data.map {
            it[refToken] ?: ""
        }
    }

    suspend fun setRefToken(value: String) {
        dataStore.edit {
            it[refToken] = value
        }
    }

    fun getAccToken(): Flow<String> {
        return dataStore.data.map {
            it[accToken] ?: ""
        }
    }

    suspend fun setAccToken(value: String) {
        dataStore.edit {
            it[accToken] = value
        }
    }

    fun getIsOnboard(): Flow<Boolean> {
        return dataStore.data.map {
            it[isOnboard] ?: false
        }
    }

    suspend fun setIsOnBoard(value: Boolean) {
        dataStore.edit {
            it[isOnboard] = value
        }
    }

    fun getLight(): Flow<Boolean> {
        return dataStore.data.map {
            it[light] ?: true
        }
    }

    suspend fun setLight(value: Boolean) {
        dataStore.edit {
            it[light] = value
        }
    }

    fun getUsername(): Flow<String> {
        return dataStore.data.map {
            it[username] ?: ""
        }
    }

    suspend fun setUsername(value: String){
        dataStore.edit {
            it[username] = value
        }
    }

    fun getIsLogin(): Flow<Boolean> {
        return dataStore.data.map {
            it[isLogin] ?: false
        }
    }

    suspend fun setIsLogin(value: Boolean) {
        dataStore.edit {
            it[isLogin] = value
        }
    }

    suspend fun setUserDataSession(user: User) {
        dataStore.edit { preferences ->
            user.userName?.let { preferences[username] = it }
            user.accessToken?.let { preferences[accToken] = it }
            user.refreshToken?.let { preferences[refToken] = it }
            user.userImage?.let { preferences[userImage] = it }
            user.expiresAt?.let { preferences[userExpires] = it.toInt() }
        }
    }

    suspend fun clearAllDataSession() {
        dataStore.edit { preferences ->
            preferences.remove(username)
            preferences.remove(accToken)
            preferences.remove(refToken)
            preferences.remove(userImage)
            preferences.remove(userExpires)
        }
    }



}
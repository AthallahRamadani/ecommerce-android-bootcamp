package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import kotlinx.coroutines.flow.Flow



interface AppRepository {
    //pref
    fun getLanguage(): Flow<String>
    suspend fun setLanguage(value: String)
    fun getRefToken(): Flow<String>
    suspend fun setRefToken(value: String)
    fun getAccToken(): Flow<String>
    suspend fun setAccToken(value: String)

    fun getIsOnboard(): Flow<Boolean>
    suspend fun setIsOnBoard(value: Boolean)

    fun getLight(): Flow<Boolean>
    suspend fun setLight(value: Boolean)

    fun getUsername(): Flow<String>
    suspend fun setUsername(value: String)

    fun getIsLogin(): Flow<Boolean>
    suspend fun setIsLogin(value: Boolean)
}

class AppRepositoryImpl(
    private val sharedPref: UserDataStore
): AppRepository {
    //pref
    override fun getLanguage(): Flow<String> = sharedPref.getLanguage()
    override suspend fun setLanguage(value: String) {
        sharedPref.setLanguage(value)
    }
    override fun getRefToken(): Flow<String> = sharedPref.getRefToken()
    override suspend fun setRefToken(value: String) {
        sharedPref.setRefToken(value)
    }
    override fun getAccToken(): Flow<String> = sharedPref.getAccToken()
    override suspend fun setAccToken(value: String) {
        sharedPref.setAccToken(value)
    }
    override fun getIsOnboard(): Flow<Boolean> = sharedPref.getIsOnboard()
    override suspend fun setIsOnBoard(value: Boolean) {
        sharedPref.setIsOnBoard(value)
    }
    override fun getLight(): Flow<Boolean> = sharedPref.getLight()
    override suspend fun setLight(value: Boolean) {
        sharedPref.setLight(value)
    }
    override fun getUsername(): Flow<String> = sharedPref.getUsername()
    override suspend fun setUsername(value: String) {
        sharedPref.setUsername(value)
    }
    override fun getIsLogin(): Flow<Boolean> = sharedPref.getIsLogin()
    override suspend fun setIsLogin(value: Boolean) {
        sharedPref.setIsLogin(value)
    }
}
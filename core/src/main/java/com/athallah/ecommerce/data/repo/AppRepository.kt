package com.athallah.ecommerce.data.repo

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    // pref
    fun getLanguage(): Flow<String>
    suspend fun setLanguage(value: String?)
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
    suspend fun logout()

    fun checkUserAuthorization(): Flow<Boolean>
    suspend fun setUserAuthorization(value: Boolean)
}

package com.athallah.ecommerce.data

import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.data.model.User
import com.athallah.ecommerce.data.source.local.LocalDataSource
import com.athallah.ecommerce.data.source.local.preference.SharedPref
import com.athallah.ecommerce.data.source.remote.RemoteDataSource
import com.athallah.ecommerce.data.source.remote.request.AuthRequest
import com.athallah.ecommerce.data.source.remote.response.AuthResponse
import com.athallah.ecommerce.data.source.remote.response.ErrorResponse
import com.athallah.ecommerce.data.source.remote.service.ApiService
import com.athallah.ecommerce.utils.extension.toUser
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AppRepository(
    private val sharedPref: UserDataStore
):ImpAppRepository {
//     fun setIsFirstLaunchToFalse() {
//        sharedPref.setIsFirstLaunchToFalse()
//    }
//
//    fun isFirstLaunch() : Boolean {
//        return sharedPref.isFirstLaunch()
//    }


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

    //Api












}
package com.athallah.ecommerce.data.repo

import android.util.Log
import com.athallah.ecommerce.data.datasource.firebase.FirebaseSubscribe
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.data.datasource.room.dao.CartDao
import com.athallah.ecommerce.data.datasource.room.dao.NotificationDao
import com.athallah.ecommerce.data.datasource.room.dao.WishlistDao
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppRepositoryImpl(
    private val sharedPref: UserDataStore,
    private val wishlistDao: WishlistDao,
    private val notificationDao: NotificationDao,
    private val cartDao: CartDao,
    private val firebaseSubscribe: FirebaseSubscribe
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

    override suspend fun logout() {
        withContext(Dispatchers.IO){
            wishlistDao.clearTable()
            cartDao.clearTable()
            notificationDao.clearTable()
            sharedPref.clearAllDataSession()
            firebaseSubscribe.unsubscribe()
        }
    }

    override fun checkUserAuthorization(): Flow<Boolean> = sharedPref.checkUserAuthorization()

    override suspend fun setUserAuthorization(value: Boolean) {
        sharedPref.setUserAuthorization(value)
    }

}
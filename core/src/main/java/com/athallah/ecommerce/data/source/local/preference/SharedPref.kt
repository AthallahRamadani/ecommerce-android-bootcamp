package com.athallah.ecommerce.data.source.local.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.athallah.ecommerce.data.model.User
import kotlinx.coroutines.flow.Flow


class SharedPref {
    fun setIsFirstLaunchToFalse() {
        editor!!.putBoolean(IS_FIRST_LAUNCH, false)
        editor!!.commit()
    }

    fun isFirstLaunch() : Boolean {
        return sharedPreferences!!.getBoolean(IS_FIRST_LAUNCH, true)
    }

    fun setUserSession(user: User) {
        editor!!.putString(USER_NAME_KEY, user.userName)
        editor!!.putString(USER_IMAGE_KEY, user.userImage)
        editor!!.putString(USER_ACCESS_TOKEN_KEY, user.accessToken)
        editor!!.putString(USER_REFRESH_TOKEN_KEY, user.refreshToken)
        user.expiresAt?.let { editor!!.putInt(USER_EXPIRES_KEY, it) }
        editor!!.commit()
    }

//    fun getUserSession(): Flow<User> = dataStore.data.map { preferences ->
//        User(
//            preferences[USER_NAME_KEY],
//            preferences[USER_IMAGE_KEY],
//            preferences[USER_ACCESS_TOKEN_KEY],
//            preferences[USER_REFRESH_TOKEN_KEY],
//            preferences[USER_EXPIRES_KEY]
//        )
//    }

    fun getUserSession(): User {
        val userName = sharedPreferences?.getString(USER_NAME_KEY, null)
        val userImage = sharedPreferences?.getString(USER_IMAGE_KEY, null)
        val accessToken = sharedPreferences?.getString(USER_ACCESS_TOKEN_KEY, null)
        val refreshToken = sharedPreferences?.getString(USER_REFRESH_TOKEN_KEY, null)
        val expiresAt = sharedPreferences?.getInt(USER_EXPIRES_KEY, 0)

        return User(userName, userImage, accessToken, refreshToken, expiresAt)
    }




    companion object{
        private val sharedPref = SharedPref()
        private var sharedPreferences : SharedPreferences?= null
        private var editor: SharedPreferences.Editor? = null
        private val IS_FIRST_LAUNCH = "is_first_launch"

        private val USER_NAME_KEY = "user_name"
        private val USER_IMAGE_KEY = "user_image"
        private val USER_ACCESS_TOKEN_KEY = "user_access_token"
        private val USER_REFRESH_TOKEN_KEY = "user_refresh_token"
        private val USER_EXPIRES_KEY = "user_expires"


        @Synchronized
        fun getInstance(context: Context): SharedPref {

            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                editor = sharedPreferences!!.edit()
            }
            return sharedPref
        }
    }
}
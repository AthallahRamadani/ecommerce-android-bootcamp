package com.athallah.ecommerce.data.source.local

import com.athallah.ecommerce.data.model.User
import com.athallah.ecommerce.data.source.local.preference.SharedPref
import kotlinx.coroutines.flow.Flow

class ILocalDataSource (
    private val appPreferences: SharedPref
) : LocalDataSource {
    override suspend fun setUserSession(user: User) {
        appPreferences.setUserSession(user)
    }

    override fun getUserSession(): User = appPreferences.getUserSession()

}


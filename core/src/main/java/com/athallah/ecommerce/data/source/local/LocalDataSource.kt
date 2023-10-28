package com.athallah.ecommerce.data.source.local

import com.athallah.ecommerce.data.model.User
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun setUserSession(user: User)
    fun getUserSession(): User
}

package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.model.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(email: String, password: String): User
    fun uploadProfile(userName: String, userImage: File?): Flow<ResultState<Boolean>>


}

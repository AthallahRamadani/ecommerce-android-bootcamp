package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.User
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

interface UserRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(email: String, password: String): User
    fun uploadProfile(
        userName: RequestBody,
        userImage: MultipartBody.Part?
    ): Flow<ResultState<Boolean>>

}

package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.ResultState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {
    suspend fun login(email: String, password: String): Flow<ResultState<Boolean>>
    suspend fun register(email: String, password: String): Flow<ResultState<Boolean>>
    fun uploadProfile(
        userName: RequestBody,
        userImage: MultipartBody.Part?
    ): Flow<ResultState<Boolean>>
}

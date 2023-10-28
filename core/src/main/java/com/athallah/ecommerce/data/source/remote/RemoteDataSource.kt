package com.athallah.ecommerce.data.source.remote

import com.athallah.ecommerce.data.source.remote.request.AuthRequest
import com.athallah.ecommerce.data.source.remote.response.AuthResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RemoteDataSource {
    suspend fun register(authRequest: AuthRequest): AuthResponse
    suspend fun login(authRequest: AuthRequest): AuthResponse
    suspend fun profile(
        token: String,
        userName: RequestBody,
        userImage: MultipartBody.Part
    ): AuthResponse
}
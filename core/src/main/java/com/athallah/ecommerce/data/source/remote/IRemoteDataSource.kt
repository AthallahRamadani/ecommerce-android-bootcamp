package com.athallah.ecommerce.data.source.remote

import com.athallah.ecommerce.data.source.remote.request.AuthRequest
import com.athallah.ecommerce.data.source.remote.response.AuthResponse
import com.athallah.ecommerce.data.source.remote.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class IRemoteDataSource(
    private val apiService: ApiService
) : RemoteDataSource {
    override suspend fun register(authRequest: AuthRequest): AuthResponse =
        apiService.register(authRequest)

    override suspend fun login(authRequest: AuthRequest): AuthResponse =
        apiService.login(authRequest)

    override suspend fun profile(
        token: String,
        userName: RequestBody,
        userImage: MultipartBody.Part
    ): AuthResponse = apiService.profile(token, userName, userImage)

}




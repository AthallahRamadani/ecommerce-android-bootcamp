package com.athallah.ecommerce.data.datasource.api.service

import com.athallah.ecommerce.data.datasource.api.request.AuthRequest
import com.athallah.ecommerce.data.datasource.api.response.ApiResponse
import com.athallah.ecommerce.data.datasource.api.response.AuthDataResponse
import com.athallah.ecommerce.utils.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body authRequest: AuthRequest,
        @Header("API_KEY") apiKey: String = Constant.API_KEY
    ): ApiResponse<AuthDataResponse>

    @POST("login")
    suspend fun login(
        @Body authRequest: AuthRequest,
        @Header("API_KEY") apiKey: String = Constant.API_KEY
    ): ApiResponse<AuthDataResponse>

    @Multipart
    @POST("profile")
    suspend fun profile(
        @Header("Authorization") token: String,
        @Part("userName") userName: RequestBody,
        @Part userImage: MultipartBody.Part
    ): ApiResponse<Any>
}

package com.athallah.ecommerce.data.datasource.api.service

import com.athallah.ecommerce.data.datasource.api.request.AuthRequest
import com.athallah.ecommerce.data.datasource.api.request.RefreshRequest
import com.athallah.ecommerce.data.datasource.api.response.ApiResponse
import com.athallah.ecommerce.data.datasource.api.response.AuthDataResponse
import com.athallah.ecommerce.data.datasource.api.response.LoginDataResponse
import com.athallah.ecommerce.data.datasource.api.response.LoginResponse
import com.athallah.ecommerce.data.datasource.api.response.ProductsResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileResponse
import com.athallah.ecommerce.data.datasource.api.response.RefreshResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterDataResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterResponse
import com.athallah.ecommerce.utils.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body authRequest: AuthRequest,
        @Header("API_KEY") apiKey: String = Constant.API_KEY
    ): RegisterResponse<RegisterDataResponse>

    @POST("login")
    suspend fun login(
        @Body authRequest: AuthRequest,
        @Header("API_KEY") apiKey: String = Constant.API_KEY
    ): LoginResponse<LoginDataResponse>

    @Multipart
    @POST("profile")
    suspend fun profile(
        @Part userName: MultipartBody.Part,
        @Part userImage: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): ProfileResponse<ProfileDataResponse>

    @POST("products")
    suspend fun products(
        @QueryMap query: Map<String, String>,
        @Header("Authorization") token: String
    ): ProductsResponse

    @POST("refresh")
    suspend fun refresh(
        @Body refreshRequest: RefreshRequest
    ): RefreshResponse


}

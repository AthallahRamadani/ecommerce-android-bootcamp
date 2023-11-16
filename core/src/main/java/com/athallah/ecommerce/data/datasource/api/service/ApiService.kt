package com.athallah.ecommerce.data.datasource.api.service

import com.athallah.ecommerce.data.datasource.api.request.AuthRequest
import com.athallah.ecommerce.data.datasource.api.request.FulfillmentRequest
import com.athallah.ecommerce.data.datasource.api.request.RatingRequest
import com.athallah.ecommerce.data.datasource.api.request.RefreshRequest
import com.athallah.ecommerce.data.datasource.api.response.FulfillmentResponse
import com.athallah.ecommerce.data.datasource.api.response.LoginDataResponse
import com.athallah.ecommerce.data.datasource.api.response.LoginResponse
import com.athallah.ecommerce.data.datasource.api.response.PaymentResponse
import com.athallah.ecommerce.data.datasource.api.response.ProductsDetailResponse
import com.athallah.ecommerce.data.datasource.api.response.ProductsResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileDataResponse
import com.athallah.ecommerce.data.datasource.api.response.ProfileResponse
import com.athallah.ecommerce.data.datasource.api.response.RatingResponse
import com.athallah.ecommerce.data.datasource.api.response.RefreshResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterDataResponse
import com.athallah.ecommerce.data.datasource.api.response.RegisterResponse
import com.athallah.ecommerce.data.datasource.api.response.ReviewResponse
import com.athallah.ecommerce.data.datasource.api.response.SearchResponse
import com.athallah.ecommerce.data.datasource.api.response.TransactionResponse
import com.athallah.ecommerce.utils.Constant
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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

    @POST("search")
    suspend fun search(
        @Query("query") query: String
    ): SearchResponse

    @GET("products/{id}")
    suspend fun detailProducts(
        @Path("id") id: String
    ) : ProductsDetailResponse

    @GET("review/{id}")
    suspend fun reviewProducts(
        @Path("id") id: String
    ) : ReviewResponse

    @GET("payment")
    suspend fun payment() : PaymentResponse

    @POST("fulfillment")
    suspend fun fulfillment(
        @Body fulfillmentRequest: FulfillmentRequest
    ): FulfillmentResponse

    @POST("rating")
    suspend fun rating(
        @Body ratingRequest: RatingRequest
    ): RatingResponse

    @GET("transaction")
    suspend fun transaction(): TransactionResponse
}

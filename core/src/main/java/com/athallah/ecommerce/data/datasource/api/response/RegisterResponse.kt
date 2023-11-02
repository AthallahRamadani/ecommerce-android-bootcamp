package com.athallah.ecommerce.data.datasource.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse<T>(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: T? = null

)

data class RegisterDataResponse(

    @field:SerializedName("accessToken")
    val accessToken: String? = null,

    @field:SerializedName("expiresAt")
    val expiresAt: Long? = null,

    @field:SerializedName("refreshToken")
    val refreshToken: String? = null
)



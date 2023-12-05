package com.athallah.ecommerce.data.datasource.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse<T>(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: T? = null

)

data class LoginDataResponse(

    @field:SerializedName("userImage")
    val userImage: String? = null,

    @field:SerializedName("userName")
    val userName: String? = null,

    @field:SerializedName("accessToken")
    val accessToken: String? = null,

    @field:SerializedName("expiresAt")
    val expiresAt: Long? = null,

    @field:SerializedName("refreshToken")
    val refreshToken: String? = null
)

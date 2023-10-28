package com.athallah.ecommerce.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: AuthDataResponse? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class AuthDataResponse(

    @field:SerializedName("userImage")
    val userImage: String? = null,

    @field:SerializedName("userName")
    val userName: String? = null,

    @field:SerializedName("accessToken")
    val accessToken: String? = null,

    @field:SerializedName("expiresAt")
    val expiresAt: Int? = null,

    @field:SerializedName("refreshToken")
    val refreshToken: String? = null
)

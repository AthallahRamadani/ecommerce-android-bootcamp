package com.athallah.ecommerce.data.datasource.api.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse<T>(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: T? = null
)

data class ProfileDataResponse(

    @field:SerializedName("userImage")
    val userImage: String? = null,

    @field:SerializedName("userName")
    val userName: String? = null,
)

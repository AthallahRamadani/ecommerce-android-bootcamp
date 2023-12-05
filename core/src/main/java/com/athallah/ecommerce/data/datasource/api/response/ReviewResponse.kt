package com.athallah.ecommerce.data.datasource.api.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: List<ReviewResponseItem>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ReviewResponseItem(

    @field:SerializedName("userImage")
    val userImage: String? = null,

    @field:SerializedName("userName")
    val userName: String? = null,

    @field:SerializedName("userReview")
    val userReview: String? = null,

    @field:SerializedName("userRating")
    val userRating: Int? = null
)

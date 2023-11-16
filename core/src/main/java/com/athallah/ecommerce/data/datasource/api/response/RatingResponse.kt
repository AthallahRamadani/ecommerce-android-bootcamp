package com.athallah.ecommerce.data.datasource.api.response

import com.google.gson.annotations.SerializedName

data class RatingResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

package com.athallah.ecommerce.data.datasource.api.response

import com.google.gson.annotations.SerializedName

data class RefreshResponse(

	@field:SerializedName("token")
	val token: String? = null
)

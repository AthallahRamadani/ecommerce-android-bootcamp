package com.athallah.ecommerce.data.datasource.api.response

import com.google.gson.annotations.SerializedName

data class PaymentResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<PaymentResponseData>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PaymentResponseDataItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class PaymentResponseData(

	@field:SerializedName("item")
	val item: List<PaymentResponseDataItem>? = null,

	@field:SerializedName("title")
	val title: String? = null
)

package com.athallah.ecommerce.data.datasource.api.response

import com.google.gson.annotations.SerializedName

data class FulfillmentResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: FulfillmentResponseData? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class FulfillmentResponseData(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("invoiceId")
    val invoiceId: String? = null,

    @field:SerializedName("payment")
    val payment: String? = null,

    @field:SerializedName("time")
    val time: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

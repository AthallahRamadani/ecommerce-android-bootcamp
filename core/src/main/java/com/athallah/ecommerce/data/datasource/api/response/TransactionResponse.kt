package com.athallah.ecommerce.data.datasource.api.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: List<TransactionResponseData>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class TransactionResponseItem(

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("productId")
    val productId: String? = null,

    @field:SerializedName("variantName")
    val variantName: String? = null
)

data class TransactionResponseData(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("review")
    val review: String? = null,

    @field:SerializedName("rating")
    val rating: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("invoiceId")
    val invoiceId: String? = null,

    @field:SerializedName("payment")
    val payment: String? = null,

    @field:SerializedName("time")
    val time: String? = null,

    @field:SerializedName("items")
    val items: List<TransactionResponseItem>? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

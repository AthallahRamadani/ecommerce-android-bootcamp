package com.athallah.ecommerce.data.datasource.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Payment(
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("item")
    val item: List<PaymentItem>
) {
    @Parcelize
    data class PaymentItem(
        @field:SerializedName("image")
        val image: String,
        @field:SerializedName("label")
        val label: String,
        @field:SerializedName("status")
        val status: Boolean
    ) : Parcelable
}

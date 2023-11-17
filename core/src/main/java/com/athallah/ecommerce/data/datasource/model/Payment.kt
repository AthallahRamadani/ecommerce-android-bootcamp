package com.athallah.ecommerce.data.datasource.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Payment(
    val title: String,
    val item: List<PaymentItem>
) {
    @Parcelize
    data class PaymentItem(
        val image: String,
        val label: String,
        val status: Boolean
    ) : Parcelable
}
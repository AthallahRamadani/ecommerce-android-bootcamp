package com.athallah.ecommerce.data.datasource.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fulfillment(
    val date: String,
    val total: Int,
    val invoiceId: String,
    val payment: String,
    val time: String,
    val status: Boolean
) : Parcelable

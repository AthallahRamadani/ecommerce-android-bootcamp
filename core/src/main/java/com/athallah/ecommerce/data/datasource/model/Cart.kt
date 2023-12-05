package com.athallah.ecommerce.data.datasource.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val productId: String,
    val productName: String,
    val productPrice: Int,
    val image: String,
    val stock: Int,
    val store: String,
    val sale: Int,
    val productRating: Float,
    val description: String,
    val totalRating: Int,
    val totalSatisfaction: Int,
    val brand: String,
    val totalReview: Int,
    val variantName: String,
    val variantPrice: Int,
    val quantity: Int? = null,
    val isChecked: Boolean = false,
) : Parcelable

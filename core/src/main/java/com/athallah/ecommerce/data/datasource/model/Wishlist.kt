package com.athallah.ecommerce.data.datasource.model

data class Wishlist(
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
    val variantPrice: Int
)

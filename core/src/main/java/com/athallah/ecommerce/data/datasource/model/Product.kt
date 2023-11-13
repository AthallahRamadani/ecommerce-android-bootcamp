package com.athallah.ecommerce.data.datasource.model

data class Product(
    val productId: String,
    val productName: String,
    val productPrice: Int,
    val image: String,
    val brand: String,
    val store: String,
    val sale: Int,
    val productRating: Float
)

package com.athallah.ecommerce.data.datasource.api.model

data class Wishlist(
    val productId: String,
    val productName: String,
    val productPrice: Int,
    val image: String,
    val store: String,
    val sale: Int,
    val stock: Int,
    val productRating: Float,
    val productVariant: String
)

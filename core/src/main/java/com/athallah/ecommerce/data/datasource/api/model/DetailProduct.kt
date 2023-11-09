package com.athallah.ecommerce.data.datasource.api.model

data class DetailProduct(
    val image: List<String>,
    val productId: String,
    val description: String,
    val totalRating: Int,
    val store: String,
    val productName: String,
    val totalSatisfaction: Int,
    val sale: Int,
    val productVariant: List<ProductVariant>,
    val stock: Int,
    val productRating: Float,
    val brand: String,
    val productPrice: Int,
    val totalReview: Int
) {
    data class ProductVariant (
        val variantPrice: Int,
        val variantName: String
    )
}

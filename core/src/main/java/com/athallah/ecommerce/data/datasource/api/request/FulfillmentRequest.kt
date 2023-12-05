package com.athallah.ecommerce.data.datasource.api.request

data class FulfillmentRequest(
    val payment: String,
    val items: List<Item>,
) {
    data class Item(
        val productId: String,
        val variantName: String,
        val quantity: Int
    )
}

package com.athallah.ecommerce.data.datasource.api.request

data class RatingRequest(
    val invoiceId: String,
    val rating: Int?,
    val review: String?
)

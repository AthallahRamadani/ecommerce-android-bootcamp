package com.athallah.ecommerce.data.datasource.model

data class Transaction(
    val date: String,
    val image: String,
    val total: Int,
    val review: String,
    val rating: Int,
    val name: String,
    val invoiceId: String,
    val payment: String,
    val time: String,
    val items: List<TransactionItem>,
    val status: Boolean
) {
    data class TransactionItem(
        val quantity: Int,
        val productId: String,
        val variantName: String
    )
}

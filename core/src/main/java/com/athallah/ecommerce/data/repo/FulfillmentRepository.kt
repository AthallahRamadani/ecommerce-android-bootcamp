package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.datasource.model.Fulfillment
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.data.datasource.model.Transaction
import kotlinx.coroutines.flow.Flow

interface FulfillmentRepository {

//    fun getPaymentMethod(): Flow<ResultState<List<Payment>>>
    fun fulfillment(payment: String, listItem: List<Cart>): Flow<ResultState<Fulfillment>>
    fun rating(
        invoiceId: String,
        rating: Int? = null,
        review: String? = null
    ): Flow<ResultState<Boolean>>
    fun getTransaction(): Flow<ResultState<List<Transaction>>>
}
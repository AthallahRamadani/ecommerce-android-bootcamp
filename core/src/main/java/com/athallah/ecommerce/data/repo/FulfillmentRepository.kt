package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Payment
import kotlinx.coroutines.flow.Flow

interface FulfillmentRepository {

    fun getPaymentMethod(): Flow<ResultState<List<Payment>>>

}
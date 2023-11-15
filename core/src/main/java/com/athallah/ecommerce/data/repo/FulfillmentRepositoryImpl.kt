package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.utils.extension.toPayment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FulfillmentRepositoryImpl(
    private val apiService: ApiService
) : FulfillmentRepository {
    override fun getPaymentMethod(): Flow<ResultState<List<Payment>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = apiService.payment()
            val data = response.data?.map { it.toPayment() }
            if (data != null) {
                emit(ResultState.Success(data))
            } else {
                throw Exception("Data is empty")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }
}
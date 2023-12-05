package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.request.FulfillmentRequest
import com.athallah.ecommerce.data.datasource.api.request.RatingRequest
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.datasource.model.Fulfillment
import com.athallah.ecommerce.data.datasource.model.Transaction
import com.athallah.ecommerce.utils.extension.toFulfillment
import com.athallah.ecommerce.utils.extension.toFulfillmentRequestItem
import com.athallah.ecommerce.utils.extension.toTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FulfillmentRepositoryImpl(
    private val apiService: ApiService
) : FulfillmentRepository {
//    override fun getPaymentMethod(): Flow<ResultState<List<Payment>>> = flow {
//        emit(ResultState.Loading)
//        try {
//            val response = apiService.payment()
//            val data = response.data?.map { it.toPayment() }
//            if (data != null) {
//                emit(ResultState.Success(data))
//            } else {
//                throw Exception("Data is empty")
//            }
//        } catch (e: Exception) {
//            ResultState.Error(e)
//        }
//    }

    override fun fulfillment(
        payment: String,
        listItem: List<Cart>
    ): Flow<ResultState<Fulfillment>> = flow {
        emit(ResultState.Loading)
        try {
            val listItemRequest = listItem.map { it.toFulfillmentRequestItem() }
            val request = FulfillmentRequest(payment, listItemRequest)

            val response = apiService.fulfillment(request)

            val data = response.data?.toFulfillment()
            if (data != null) {
                emit(ResultState.Success(data))
            } else {
                throw Exception("No data")
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    override fun rating(
        invoiceId: String,
        rating: Int?,
        review: String?
    ): Flow<ResultState<Boolean>> = flow {
        emit(ResultState.Loading)
        try {
            val response = apiService.rating(RatingRequest(invoiceId, rating, review))
            if (response.code == 200) {
                emit(ResultState.Success(true))
            } else {
                throw Exception("Failed")
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    override fun getTransaction(): Flow<ResultState<List<Transaction>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = apiService.transaction()
            val listData = response.data?.map { it.toTransaction() }
            if (listData != null) {
                emit(ResultState.Success(listData))
            } else {
                throw Exception("Failed")
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }
}

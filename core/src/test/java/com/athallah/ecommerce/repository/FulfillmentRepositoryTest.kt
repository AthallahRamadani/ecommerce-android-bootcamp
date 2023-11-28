package com.athallah.ecommerce.repository

import app.cash.turbine.test
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.request.FulfillmentRequest
import com.athallah.ecommerce.data.datasource.api.request.RatingRequest
import com.athallah.ecommerce.data.datasource.api.response.FulfillmentResponse
import com.athallah.ecommerce.data.datasource.api.response.FulfillmentResponseData
import com.athallah.ecommerce.data.datasource.api.response.PaymentResponse
import com.athallah.ecommerce.data.datasource.api.response.PaymentResponseData
import com.athallah.ecommerce.data.datasource.api.response.PaymentResponseDataItem
import com.athallah.ecommerce.data.datasource.api.response.RatingResponse
import com.athallah.ecommerce.data.datasource.api.response.TransactionResponse
import com.athallah.ecommerce.data.datasource.api.response.TransactionResponseData
import com.athallah.ecommerce.data.datasource.api.response.TransactionResponseItem
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.datasource.model.Fulfillment
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.data.datasource.model.Transaction
import com.athallah.ecommerce.data.datasource.room.dao.CartDao
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.FulfillmentRepository
import com.athallah.ecommerce.data.repo.FulfillmentRepositoryImpl
import com.athallah.ecommerce.utils.extension.toFulfillment
import com.athallah.ecommerce.utils.extension.toPayment
import com.athallah.ecommerce.utils.extension.toTransaction
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class FulfillmentRepositoryTest {
    private lateinit var fulfillmentRepository: FulfillmentRepository
    private val apiService: ApiService = mock()

    @Before
    fun setUp() {
        fulfillmentRepository = FulfillmentRepositoryImpl(apiService)
    }

    //    @Test
//    fun getPaymentMethod() = runTest {
//        val paymentResponse = PaymentResponse(
//            code = 200,
//            data = listOf(PaymentResponseData(
//                item = listOf(PaymentResponseDataItem(
//                    image = "null",
//                    label = "null",
//                    status = false
//                )),
//                title = "null"
//            )),
//            message = "null"
//        )
//        whenever(apiService.payment()).thenReturn(paymentResponse)
//        fulfillmentRepository.getPaymentMethod().test {
//            assertEquals(ResultState.Loading, awaitItem())
//            assertEquals(ResultState.Success(paymentResponse.data?.map { it.toPayment() }), awaitItem())
//        }
//    }
    @Test
    fun fulfillment() = runTest {
        val fulfillmentRequest = FulfillmentRequest(
            payment = "",
            items = listOf(
                FulfillmentRequest.Item(
                    productId = "",
                    variantName = "",
                    quantity = 2
                )
            )
        )

        val fulfillmentResponse = FulfillmentResponse(
            code = 200,
            data = FulfillmentResponseData(
                date = "",
                total = 2,
                invoiceId = "",
                payment = "",
                time = "null",
                status = true
            ),
            message = "null"
        )

        val cart = Cart(
            "",
            "",
            2,
            "",
            1000000000,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2,
            2,
            true
        )
        whenever(apiService.fulfillment(fulfillmentRequest)).thenReturn(fulfillmentResponse)
        fulfillmentRepository.fulfillment(payment = "", listOf(cart)).test {
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(
                ResultState.Success(fulfillmentResponse.data!!.toFulfillment()),
                awaitItem()
            )
            awaitComplete()
        }
    }

    @Test
    fun rating() = runTest {
        val ratingRequest = RatingRequest(
            invoiceId = "",
            rating = 4,
            review = "null"
        )
        val ratingResponse = RatingResponse(code = 200, message = "null")
        whenever(apiService.rating(ratingRequest)).thenReturn(ratingResponse)
        fulfillmentRepository.rating(invoiceId = "", rating = 4, review = "null").test {
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(ResultState.Success(true), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun getTransaction() = runTest {
        val transactionResponse = TransactionResponse(
            code = 200,
            message = "null",
            data = listOf(
                TransactionResponseData(
                    date = "",
                    image = "null",
                    total = 1,
                    review = "null",
                    rating = 1,
                    name = "null",
                    invoiceId = "null",
                    payment = "null",
                    time = "null",
                    status = true,
                    items = listOf(
                        TransactionResponseItem(
                            quantity = 1,
                            productId = "null",
                            variantName = "null"
                        )
                    )
                )
            )
        )
        whenever(apiService.transaction()).thenReturn(transactionResponse)
        fulfillmentRepository.getTransaction().test {
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(
                ResultState.Success(transactionResponse.data!!.map { it.toTransaction() }),
                awaitItem()
            )
            awaitComplete()
        }
    }

}
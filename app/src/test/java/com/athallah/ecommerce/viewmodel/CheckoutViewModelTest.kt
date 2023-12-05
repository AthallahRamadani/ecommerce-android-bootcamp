package com.athallah.ecommerce.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.response.FulfillmentResponse
import com.athallah.ecommerce.data.datasource.api.response.FulfillmentResponseData
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.data.datasource.room.entity.CartEntity
import com.athallah.ecommerce.data.repo.FulfillmentRepository
import com.athallah.ecommerce.fragment.checkout.CheckoutFragment
import com.athallah.ecommerce.fragment.checkout.CheckoutViewModel
import com.athallah.ecommerce.utils.extension.toCart
import com.athallah.ecommerce.utils.extension.toFulfillment
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class CheckoutViewModelTest {
    private val dummyFulfillmentResponse = FulfillmentResponse(
        code = 200,
        message = "OK",
        data = FulfillmentResponseData(
            invoiceId = "ba47402c-d263-49d3-a1f8-759ae59fa4a1",
            status = true,
            date = "09 Jun 2023",
            time = "08:53",
            payment = "Bank BCA",
            total = 48998000
        )
    )
    private val dummyCartList = ArrayList<CartEntity>().apply {
        repeat(6) {
            add(
                CartEntity(
                    productId = it.toString(),
                    productName = "Asus ROG",
                    productPrice = 1000000,
                    image = "image.png",
                    stock = 10,
                    sale = 100,
                    store = "Toko Komputer",
                    productRating = 5F,
                    description = "Lorem ipsum",
                    totalRating = 20,
                    totalSatisfaction = 100,
                    brand = "Asus",
                    totalReview = 12,
                    variantName = "RAM 16GB",
                    variantPrice = 0,
                    quantity = 2,
                    isChecked = false
                )
            )
        }
    }
    private lateinit var checkoutViewModel: CheckoutViewModel
    private val fulfillmentRepository: FulfillmentRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val savedStateHandle: SavedStateHandle =
        SavedStateHandle(mapOf(CheckoutFragment.ARG_DATA to dummyCartList.map { it.toCart() }))

    @Before
    fun setUp() {
        checkoutViewModel = CheckoutViewModel(fulfillmentRepository, savedStateHandle)
    }

    @Test
    fun updateDataQuantityInsert() = runTest {
        var listCart = dummyCartList.map { it.toCart() }
        val cart = dummyCartList[0].toCart()

        checkoutViewModel.updateDataQuantity(cart, true)
        listCart = listCart.map {
            if (it == cart) {
                it.copy(quantity = it.quantity!!.plus(1))
            } else {
                it
            }
        }

        assertEquals(listCart, checkoutViewModel.listData.value)
    }

    @Test
    fun updateDataQuantityRemove() = runTest {
        var listCart = dummyCartList.map { it.toCart() }
        val cart = dummyCartList[0].toCart()

        checkoutViewModel.updateDataQuantity(cart, false)
        listCart = listCart.map {
            if (it == cart) {
                it.copy(quantity = it.quantity!!.minus(1))
            } else {
                it
            }
        }

        assertEquals(listCart, checkoutViewModel.listData.value)
    }

    @Test
    fun makePaymentSuccess() = runTest {
        val paymentItem = Payment.PaymentItem(
            image = "",
            label = "",
            status = true
        )

        val data = checkoutViewModel.listData.first()
        checkoutViewModel.paymentItem = paymentItem
        whenever(fulfillmentRepository.fulfillment(paymentItem.label, data)).thenReturn(
            flowOf(
                ResultState.Success(dummyFulfillmentResponse.data!!.toFulfillment())
            )
        )
        checkoutViewModel.makePayment()

        assertEquals(
            ResultState.Success(dummyFulfillmentResponse.data!!.toFulfillment()),
            checkoutViewModel.paymentState.value
        )
    }
}

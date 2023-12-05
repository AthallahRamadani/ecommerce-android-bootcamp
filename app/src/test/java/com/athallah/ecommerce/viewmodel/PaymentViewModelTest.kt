package com.athallah.ecommerce.viewmodel

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.firebase.AppFirebaseRemoteConfig
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.fragment.payment.PaymentViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class PaymentViewModelTest {

    private lateinit var paymentViewModel: PaymentViewModel
    private val remoteConfig: AppFirebaseRemoteConfig = mock()
    private val payment = Payment(
        title = "string",
        item = listOf(
            Payment.PaymentItem(
                image = "ss",
                label = "ss",
                status = true
            )
        )
    )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        whenever(remoteConfig.fetchPaymentMethod()).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(listOf(payment))
            )
        )
        paymentViewModel = PaymentViewModel(remoteConfig)
    }

    @Test
    fun getPayment() = runTest {
        paymentViewModel.getPayment()

        assertEquals(
            ResultState.Success(listOf(payment)),
            paymentViewModel.paymentState.value
        )
    }
}

package com.athallah.ecommerce.viewmodel.main

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Transaction
import com.athallah.ecommerce.data.repo.FulfillmentRepository
import com.athallah.ecommerce.fragment.main.transaction.TransactionViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class TransactionViewModelTest {
    val transaction = Transaction(
        date = "String",
        image = "String",
        total = 2,
        review = "String",
        rating = 2,
        name = "String",
        invoiceId = "String",
        payment = "String",
        time = "String",
        items = listOf(
            Transaction.TransactionItem(
                quantity = 2,
                productId = "String",
                variantName = "String"
            )
        ),
        status = true
    )

    private lateinit var transactionViewModel: TransactionViewModel
    private val fulfillmentRepository: FulfillmentRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        whenever(fulfillmentRepository.getTransaction()).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(listOf(transaction))
            )
        )
        transactionViewModel = TransactionViewModel(fulfillmentRepository)
    }

    @Test
    fun getListTransaction() {
        whenever(fulfillmentRepository.getTransaction()).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(listOf(transaction))
            )
        )

        transactionViewModel.getListTransaction()

        assertEquals(
            ResultState.Success(listOf(transaction)),
            transactionViewModel.transactionState.value
        )
    }
}

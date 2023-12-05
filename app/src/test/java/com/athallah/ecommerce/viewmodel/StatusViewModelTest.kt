package com.athallah.ecommerce.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.response.FulfillmentResponse
import com.athallah.ecommerce.data.datasource.api.response.FulfillmentResponseData
import com.athallah.ecommerce.data.repo.FulfillmentRepository
import com.athallah.ecommerce.fragment.status.StatusFragment
import com.athallah.ecommerce.fragment.status.StatusViewModel
import com.athallah.ecommerce.utils.extension.toFulfillment
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class StatusViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var statusViewModel: StatusViewModel
    private val fulfillmentRepository: FulfillmentRepository = mock()
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
    private val savedStateHandle: SavedStateHandle =
        SavedStateHandle(
            mapOf(
                StatusFragment.DATA_BUNDLE_KEY to dummyFulfillmentResponse.data!!.toFulfillment(),
                StatusFragment.RATING_BUNDLE_KEY to 5,
                StatusFragment.REVIEW_BUNDLE_KEY to "review"
            )
        )

    @Before
    fun setUp() {
        statusViewModel = StatusViewModel(
            fulfillmentRepository, savedStateHandle
        )
    }

    @Test
    fun sendRating() = runTest {
        whenever(fulfillmentRepository.rating("", 5, ""))
            .thenReturn(flowOf(ResultState.Loading, ResultState.Success(true)))

        statusViewModel.sendRating("", 5, "")
        advanceUntilIdle()

        assertEquals(
            ResultState.Success(true),
            statusViewModel.ratingState.value
        )
    }
}

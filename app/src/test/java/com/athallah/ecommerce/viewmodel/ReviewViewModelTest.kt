package com.athallah.ecommerce.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.response.ReviewResponse
import com.athallah.ecommerce.data.datasource.api.response.ReviewResponseItem
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.fragment.review.ReviewFragment
import com.athallah.ecommerce.fragment.review.ReviewViewModel
import com.athallah.ecommerce.utils.extension.toReview
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class ReviewViewModelTest {
    private lateinit var reviewViewModel: ReviewViewModel
    private val storeRepository: StoreRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val dummyProductsReviewResponse = ReviewResponse(
        code = 200,
        message = "OK",
        data = arrayListOf(
            ReviewResponseItem(
                userName = "John",
                userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQM4VpzpVw8mR2j9_gDajEthwY3KCOWJ1tOhcv47-H9o1a-s9GRPxdb_6G9YZdGfv0HIg&usqp=CAU",
                userRating = 4,
                userReview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ),
            ReviewResponseItem(
                userName = "Doe",
                userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR3Z6PN8QNVhH0e7rEINu_XJS0qHIFpDT3nwF5WSkcYmr3znhY7LOTkc8puJ68Bts-TMc&usqp=CAU",
                userRating = 5,
                userReview = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
            )
        )
    )

    private val savedStateHandle: SavedStateHandle =
        SavedStateHandle(mapOf(ReviewFragment.BUNDLE_PRODUCT_ID_KEY to "productId"))

    @Before
    fun setUp() {
        whenever(storeRepository.reviewProducts("productId")).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(dummyProductsReviewResponse.data!!.map { it.toReview() })
            )
        )
        reviewViewModel = ReviewViewModel(storeRepository, savedStateHandle)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getListReview() = runTest {
        val productId = "productId"
        reviewViewModel.productId = "productId"
        whenever(storeRepository.reviewProducts(productId)).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(dummyProductsReviewResponse.data!!.map { it.toReview() })
            )
        )

        reviewViewModel.getListReview()
        advanceUntilIdle()

        assertEquals(
            ResultState.Success(dummyProductsReviewResponse.data!!.map { it.toReview() }),
            reviewViewModel.reviewProductState.value
        )
    }
}

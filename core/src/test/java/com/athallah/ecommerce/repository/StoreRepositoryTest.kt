package com.athallah.ecommerce.repository

import app.cash.turbine.test
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.response.ProductVariantItem
import com.athallah.ecommerce.data.datasource.api.response.ProductsDetailResponse
import com.athallah.ecommerce.data.datasource.api.response.ProductsDetailResponseData
import com.athallah.ecommerce.data.datasource.api.response.ReviewResponse
import com.athallah.ecommerce.data.datasource.api.response.ReviewResponseItem
import com.athallah.ecommerce.data.datasource.api.response.SearchResponse
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.data.repo.FulfillmentRepository
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.data.repo.StoreRepositoryImpl
import com.athallah.ecommerce.utils.extension.toDetailProduct
import com.athallah.ecommerce.utils.extension.toReview
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class StoreRepositoryTest {

    private lateinit var storeRepository: StoreRepository
    private val apiService: ApiService = mock()
    private val preferences: UserDataStore = mock()

    @Before
    fun setUp() {
        storeRepository = StoreRepositoryImpl(
            apiService,
            preferences
        )
    }

    @Test
    fun searchProducts() = runTest {
        val searchResponse = SearchResponse(
            code = null,
            message = "",
            data = listOf("bebas")
        )
        whenever(apiService.search("halos")).thenReturn(searchResponse)
        storeRepository.searchProducts("halos").test {
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(ResultState.Success(searchResponse.data ?: emptyList()), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun detailProducts() = runTest {
        val productDetailResponse = ProductsDetailResponse(
            code = 200,
            message = "",
            data = ProductsDetailResponseData(
                image = listOf(""),
                productId = "",
                "",
                2,
                "",
                "",
                2,
                2,
                productVariant = listOf(
                    ProductVariantItem(
                        2,
                        ""
                    )
                ),
                2,
                2f,
                "",
                2,
                2
            )
        )
        whenever(apiService.detailProducts("mm")).thenReturn(productDetailResponse)
        storeRepository.detailProducts("mm").test {
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(
                ResultState.Success(apiService.detailProducts("mm").data!!.toDetailProduct()),
                awaitItem()
            )
            awaitComplete()
        }
    }

    @Test
    fun reviewProducts() = runTest {
        val reviewResponse = ReviewResponse(
            code = null,
            message = "",
            data = listOf(
                ReviewResponseItem(
                    "",
                    "",
                    "",
                    2
                )
            )
        )
        whenever(apiService.reviewProducts("mm")).thenReturn(reviewResponse)
        storeRepository.reviewProducts("mm").test {
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(
                ResultState.Success(apiService.reviewProducts("mm").data!!.map { it.toReview() }),
                awaitItem()
            )
            awaitComplete()
        }
    }
}
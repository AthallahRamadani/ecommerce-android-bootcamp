package com.athallah.ecommerce.viewmodel.main

import app.cash.turbine.test
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.response.SearchResponse
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.fragment.main.store.search.SearchViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class SearchViewModelTest {

    private lateinit var searchViewModel: SearchViewModel
    private val storeRepository: StoreRepository = mock()

    private val dummySearchResponse = SearchResponse(
        code = 200,
        message = "OK",
        data = arrayListOf(
            "Lenovo Legion 3",
            "Lenovo Legion 5",
            "Lenovo Legion 7",
            "Lenovo Ideapad 3",
            "Lenovo Ideapad 5",
            "Lenovo Ideapad 7"
        )
    )

    @Before
    fun setUp() {
        searchViewModel = SearchViewModel(storeRepository)
    }

    @Test
    fun getSearchData() = runTest {
        whenever(storeRepository.searchProducts("query")).thenReturn(
            flowOf(
                ResultState.Loading,
                ResultState.Success(dummySearchResponse.data!!)
            )
        )

        searchViewModel.searchData.test {
            searchViewModel.getSearchData("query")
            assertEquals(ResultState.Loading, awaitItem())
            assertEquals(ResultState.Success(dummySearchResponse.data!!), awaitItem())
        }
    }
}

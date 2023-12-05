package com.athallah.ecommerce.viewmodel.main

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.fragment.main.store.StoreViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class StoreViewModelTest {

    private lateinit var storeViewModel: StoreViewModel
    private val storeRepository: StoreRepository = mock()
    private val appRepository: AppRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        storeViewModel = StoreViewModel(storeRepository, appRepository)
    }

    @Test
    fun prefGetAccToken() = runTest {
        storeViewModel.prefGetAccToken()
        verify(appRepository).getAccToken()
    }

    @Test
    fun resetData() = runTest {
        storeViewModel.resBrandFilterProduct = 3
        storeViewModel.resSortFilterProduct = 2

        storeViewModel.resetData()
        assertEquals(null, storeViewModel.resSortFilterProduct)
        assertEquals(null, storeViewModel.resBrandFilterProduct)
    }
}

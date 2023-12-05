package com.athallah.ecommerce.viewmodel.main

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.fragment.main.home.HomeViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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
class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel
    private val appRepository: AppRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(appRepository)
    }

    @Test
    fun makeLogout() = runTest {
        homeViewModel.makeLogout()
        verify(appRepository).logout()
    }

    @Test
    fun getAppTheme() = runTest {
        whenever(appRepository.getLight()).thenReturn(flowOf(true))
        val actualData = homeViewModel.getAppTheme().first()

        assertEquals(true, actualData)
    }

    @Test
    fun setAppTheme() = runTest {
        homeViewModel.setAppTheme(true)
        verify(appRepository).setLight(true)
    }

    @Test
    fun getAppLanguage() = runTest {
        whenever(appRepository.getLanguage()).thenReturn(flowOf("en"))
        val actualData = homeViewModel.getAppLanguage().first()

        assertEquals("en", actualData)
    }

    @Test
    fun setAppLanguage() = runTest {
        homeViewModel.setAppLanguage(true)
        verify(appRepository).setLanguage("id")
    }
}

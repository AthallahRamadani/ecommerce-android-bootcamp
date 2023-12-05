package com.athallah.ecommerce.viewmodel

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.MainViewModel
import com.athallah.ecommerce.data.repo.AppRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MainViewModelActivityTest {
    private lateinit var mainViewModel: MainViewModel
    private val appRepository: AppRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(appRepository)
    }

    @Test
    fun checkUserLogin() = runTest {
        mainViewModel.checkUserLogin()
        verify(appRepository).checkUserAuthorization()
    }

    @Test
    fun logout() = runTest {
        mainViewModel.logout()
        verify(appRepository).logout()
    }
}

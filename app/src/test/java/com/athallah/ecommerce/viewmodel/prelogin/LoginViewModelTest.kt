package com.athallah.ecommerce.viewmodel.prelogin

import com.athallah.ecommerce.MainDispatcherRuleStandard
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.UserRepository
import com.athallah.ecommerce.fragment.prelogin.LoginViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    private val appRepository: AppRepository = mock()
    private val userRepository: UserRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRuleStandard()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(appRepository, userRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun makeLogin() = runTest {
        whenever(
            userRepository.login(
                "halo@gmail.com",
                "12345678"
            )
        ).thenReturn(flowOf(ResultState.Loading, ResultState.Success(true)))
        loginViewModel.makeLogin("halo@gmail.com", "12345678")
        val values = mutableListOf<ResultState<Boolean>?>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            loginViewModel.loginState.toList(values)
        }
        advanceUntilIdle()
        assertEquals(listOf(null, ResultState.Loading, ResultState.Success(true)), values)
    }

    @Test
    fun prefGetIsOnBoard() = runTest {
        whenever(appRepository.getIsOnboard()).thenReturn(flowOf(true))
        val actualData = loginViewModel.prefGetIsOnboard().first()
        assertEquals(true, actualData)
    }

    @Test
    fun prefGetUsername() = runTest {
        whenever(appRepository.getUsername()).thenReturn(flowOf("hahaha"))
        val actualData = loginViewModel.prefGetUsername()
        assertEquals("hahaha", actualData)
    }
}

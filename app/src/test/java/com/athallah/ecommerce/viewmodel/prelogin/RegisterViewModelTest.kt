package com.athallah.ecommerce.viewmodel.prelogin

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.UserRepository
import com.athallah.ecommerce.fragment.prelogin.RegisterViewModel
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
class RegisterViewModelTest {
    private lateinit var registerViewModel: RegisterViewModel
    private val appRepository: AppRepository = mock()
    private val userRepository: UserRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(userRepository, appRepository)
    }

    @Test
    fun makeRegister() = runTest {
        whenever(
            userRepository.register(
                "halo@gmail.com",
                "12345678"
            )
        ).thenReturn(flowOf(ResultState.Loading, ResultState.Success(true)))
        registerViewModel.makeRegister("halo@gmail.com", "12345678")

        assertEquals(ResultState.Success(true), registerViewModel.registerState.value)
    }
}

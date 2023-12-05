package com.athallah.ecommerce.viewmodel.main

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.UserRepository
import com.athallah.ecommerce.fragment.main.profile.ProfileViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class ProfileViewModelTest {
    private lateinit var profileViewModel: ProfileViewModel
    private val appRepository: AppRepository = mock()
    private val userRepository: UserRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        profileViewModel = ProfileViewModel(appRepository, userRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun uploadProfile() = runTest {
        val nameRequestBody = "halo".toRequestBody()
        val userImage = null
        whenever(
            userRepository.uploadProfile(
                nameRequestBody,
                userImage
            )
        ).thenReturn(flowOf(ResultState.Loading, ResultState.Success(true)))
        profileViewModel.uploadProfile(nameRequestBody, userImage)
        advanceUntilIdle()
        assertEquals(ResultState.Success(true), profileViewModel.uploadState.value)
    }

    @Test
    fun prefSetUsername() = runTest {
        profileViewModel.prefSetUsername("ssss")
        verify(appRepository).setUsername("ssss")
    }
}

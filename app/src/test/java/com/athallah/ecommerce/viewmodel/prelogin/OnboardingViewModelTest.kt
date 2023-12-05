package com.athallah.ecommerce.viewmodel.prelogin

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.fragment.prelogin.OnboardingViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class OnboardingViewModelTest {
    private lateinit var onboardingViewModel: OnboardingViewModel
    private val appRepository: AppRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        onboardingViewModel = OnboardingViewModel(appRepository)
    }

    @Test
    fun setFirstTimeRunApp() = runTest {
        onboardingViewModel.setFirstTimeRunApp(
            true
        )
        verify(appRepository).setIsOnBoard(true)
    }
}

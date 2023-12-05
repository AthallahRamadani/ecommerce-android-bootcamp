package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.ViewModel
import com.athallah.ecommerce.data.repo.AppRepository
import kotlinx.coroutines.runBlocking

class OnboardingViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    fun setFirstTimeRunApp(isFirstTime: Boolean = true) {
        runBlocking {
            appRepository.setIsOnBoard(isFirstTime)
        }
    }
}

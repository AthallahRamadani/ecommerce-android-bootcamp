package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.ViewModel
import com.athallah.ecommerce.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appRepository: com.athallah.ecommerce.data.AppRepository
) : ViewModel() {
    fun setFirstTimeRunApp() {
            appRepository.setIsFirstLaunchToFalse()
    }
}


package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appRepository: com.athallah.ecommerce.data.AppRepository
) : ViewModel() {
    fun isFirstLaunch():Boolean {
           return appRepository.isFirstLaunch()
    }
}
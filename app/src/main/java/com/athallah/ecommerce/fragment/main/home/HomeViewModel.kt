package com.athallah.ecommerce.fragment.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.repo.AppRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(
    private val appRepository: AppRepository,
) : ViewModel() {

    init {
        getAppTheme()
        getAppLanguage()
    }

    fun makeLogout() {
        viewModelScope.launch {
            appRepository.logout()
        }
    }

    fun getAppTheme() = appRepository.getLight()

    fun setAppTheme(isDarkTheme: Boolean) {
        runBlocking { appRepository.setLight(isDarkTheme) }
    }

    fun getAppLanguage() = appRepository.getLanguage()

    fun setAppLanguage(isIdLanguage: Boolean) {
        runBlocking { appRepository.setLanguage(if (isIdLanguage) "id" else null) }
    }
}

package com.athallah.ecommerce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.repo.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000L)
            _isReady.value = true
        }
    }

    fun checkUserLogin() = appRepository.checkUserAuthorization()
    fun logout() {
        viewModelScope.launch {
            appRepository.logout()
        }
    }

    fun getAppLanguage() = appRepository.getLanguage()
}

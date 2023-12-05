package com.athallah.ecommerce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.repo.AppRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    fun checkUserLogin() = appRepository.checkUserAuthorization()
    fun logout() {
        viewModelScope.launch {
            appRepository.logout()
        }
    }
}

package com.athallah.ecommerce

import androidx.lifecycle.ViewModel
import com.athallah.ecommerce.data.repo.AppRepository

class MainViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    fun checkUserLogin() = appRepository.checkUserAuthorization()

}
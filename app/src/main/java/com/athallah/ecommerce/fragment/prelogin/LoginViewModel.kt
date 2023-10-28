package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.athallah.ecommerce.data.ImpAppRepository

class LoginViewModel (
    private val appRepository: ImpAppRepository
) : ViewModel() {
//    fun isFirstLaunch():Boolean {
//           return appRepository.isFirstLaunch()
//    }

    fun prefGetIsOnboard(): LiveData<Boolean> = appRepository.getIsOnboard().asLiveData()


}
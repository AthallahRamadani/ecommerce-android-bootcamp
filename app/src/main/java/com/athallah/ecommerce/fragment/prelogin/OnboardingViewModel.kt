package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.AppRepository
import com.athallah.ecommerce.data.ImpAppRepository
import kotlinx.coroutines.launch


class OnboardingViewModel (
    private val appRepository: ImpAppRepository
) : ViewModel() {

    fun prefGetIsOnboard(): LiveData<Boolean> = appRepository.getIsOnboard().asLiveData()
    fun prefSetIsOnboard(value: Boolean) {
        viewModelScope.launch {
            appRepository.setIsOnBoard(value)
        }
    }




//    fun setFirstTimeRunApp() {
//            appRepository.setIsOnBoard()
//    }
}


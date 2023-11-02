package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.repo.AppRepository
import kotlinx.coroutines.launch

enum class ActionOnboard {
    LOGIN,
    REGISTER
}

class OnboardingViewModel(
    private val appRepository: AppRepository
) : ViewModel() {


    val prefSetIsOnBoard: LiveData<ActionOnboard> = MutableLiveData()

    fun setAlreadyOnboard(action: ActionOnboard) {
        val liveData = prefSetIsOnBoard as MutableLiveData
        viewModelScope.launch {
            appRepository.setIsOnBoard(true)
            liveData.postValue(action)
        }
    }
}
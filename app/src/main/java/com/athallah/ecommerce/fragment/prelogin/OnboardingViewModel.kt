package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.repo.AppRepository
import kotlinx.coroutines.launch

enum class Action {
    LOGIN,
    REGISTER
}

class OnboardingViewModel(
    private val appRepository: AppRepository
) : ViewModel() {


    val prefSetIsOnBoard: LiveData<Action> = MutableLiveData()

    fun setAlreadyOnboard(action: Action) {
        val liveData = prefSetIsOnBoard as MutableLiveData
        viewModelScope.launch {
            appRepository.setIsOnBoard(true)
            liveData.postValue(action)
        }
    }
}



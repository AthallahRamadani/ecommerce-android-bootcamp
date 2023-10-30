package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.model.User
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel (
    private val appRepository: AppRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val loginLiveData: LiveData<ResultState<User>> = MutableLiveData()

    fun prefGetIsOnboard(): LiveData<Boolean> = appRepository.getIsOnboard().asLiveData()

    fun login(email: String, password: String) {
        val liveData = loginLiveData as MutableLiveData
        liveData.postValue(ResultState.Loading)
        viewModelScope.launch {
            try {
                val user = userRepository.login(
                    email = email,
                    password = password
                )
                liveData.postValue(ResultState.Success(user))
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(ResultState.Error(e.message ?: "Error"))
            }
        }
    }
}

//                appRepository.setUsername(user.userName.orEmpty())
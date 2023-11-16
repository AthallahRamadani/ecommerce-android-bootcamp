package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.User
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.UserRepository
import com.athallah.ecommerce.utils.getApiErrorMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class RegisterViewModel(
    private val userRepository: UserRepository,
    private val appRepository: AppRepository
) : ViewModel() {

    val registerLiveData: LiveData<ResultState<User>> = MutableLiveData()

    fun prefSetAccToken(value: String) {
        runBlocking {
            appRepository.setAccToken(value)
        }
    }

    fun prefSetRefToken(value: String) {
        runBlocking {
            appRepository.setRefToken(value)
        }
    }

    fun prefGetUsername(): String = runBlocking { appRepository.getUsername().first() }

    fun setUserAuthorization(value: Boolean){
        runBlocking {
            appRepository.setUserAuthorization(value)
        }
    }

    fun register(email: String, password: String) {
        val liveData = registerLiveData as MutableLiveData
        liveData.postValue(ResultState.Loading)
        viewModelScope.launch {
            try {
                val user = userRepository.register(
                    email = email,
                    password = password
                )
                liveData.postValue(ResultState.Success(user))
            } catch (e: Exception) {
                liveData.postValue(ResultState.Error(e))
            }
        }
    }
}

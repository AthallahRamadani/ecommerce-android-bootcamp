package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.User
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.UserRepository
import com.athallah.ecommerce.utils.getApiErrorMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception


class LoginViewModel(
    private val appRepository: AppRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val loginLiveData: LiveData<ResultState<User>> = MutableLiveData()

    //preference
    fun prefGetIsOnboard(): LiveData<Boolean> = appRepository.getIsOnboard().asLiveData()

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

    fun prefSetUserName(value: String) {
        runBlocking {
            appRepository.setUsername(value)
        }
    }

    fun prefGetUsername(): String = runBlocking { appRepository.getUsername().first() }

    fun setUserAuthorization(value: Boolean){
        runBlocking {
            appRepository.setUserAuthorization(value)
        }
    }



    //api
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
                liveData.postValue(ResultState.Error(e))
            }
        }
    }



}
package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.model.User
import com.athallah.ecommerce.data.repo.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception


class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val registerLiveData: LiveData<ResultState<User>> = MutableLiveData()

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
                e.printStackTrace()
                liveData.postValue(ResultState.Error(e.message ?: "Error"))
            }
        }
    }
}

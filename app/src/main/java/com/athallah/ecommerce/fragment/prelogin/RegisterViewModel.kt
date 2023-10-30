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


class RegisterViewModel (
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<ResultState<User>?>(null)
    val registerState: StateFlow<ResultState<User>?> = _registerState
    fun register(email: String, password: String){
        viewModelScope.launch {
            userRepository.register(email, password).collect(){ resultState->
                _registerState.value = resultState
            }
        }
    }
}

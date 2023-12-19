package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val appRepository: AppRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<ResultState<Boolean>?>(null)
    val registerState: StateFlow<ResultState<Boolean>?> = _registerState

    fun makeRegister(email: String, password: String) {
        viewModelScope.launch {
            userRepository.register(email, password).collect { resultState ->
                _registerState.value = resultState
            }
        }
    }
}

package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val appRepository: AppRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<ResultState<Boolean>?>(null)
    val loginState: StateFlow<ResultState<Boolean>?> = _loginState

    fun makeLogin(email: String, password: String) {
        viewModelScope.launch {
            userRepository.login(email, password).collect { resultState ->
                _loginState.value = resultState
            }
        }
    }

    // preference
    fun prefGetIsOnboard(): Flow<Boolean> = appRepository.getIsOnboard()
}

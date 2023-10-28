package com.athallah.ecommerce.fragment.prelogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.AppRepository
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


//@HiltViewModel
class RegisterViewModel (
//    private val appRepository: AppRepository
) : ViewModel() {

//    private val _registerState = MutableStateFlow<ResultState<User>?>(null)
//    val registerState: StateFlow<ResultState<User>?> = _registerState
//
//    fun makeRegister(email: String, password: String) {
//        viewModelScope.launch {
//            appRepository.register(email, password).collect { resultState ->
//                _registerState.value = resultState
//            }
//        }
//    }
}

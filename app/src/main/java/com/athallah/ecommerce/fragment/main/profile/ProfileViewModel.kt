package com.athallah.ecommerce.fragment.main.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.UserRepository
import com.athallah.ecommerce.utils.getApiErrorMessage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class ProfileViewModel (
    private val appRepository: AppRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var currentImageUri: Uri? = null

    private val _uploadState = MutableLiveData<ResultState<Boolean>?>(null)
    val uploadState: LiveData<ResultState<Boolean>?> = _uploadState

    fun uploadProfile(userName: String, userImage: File?) {
        viewModelScope.launch {
            userRepository.uploadProfile(userName, userImage).collect { value ->
                _uploadState.value = value
            }
        }
    }

    fun prefSetUsername(value: String) {
        runBlocking {
            appRepository.setUsername(value)
        }
    }
}

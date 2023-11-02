package com.athallah.ecommerce.fragment.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.repo.AppRepository
import kotlinx.coroutines.launch

class HomeViewModel (
    private val appRepository: AppRepository
) : ViewModel() {

    fun makeLogout() {
//        Log.d("TAG", "makeLogout: sss")
        viewModelScope.launch {
            appRepository.logout()
//            Log.d("TAG", "makeLogout: ${appRepository.getAccToken().first()}")
        }
    }



}
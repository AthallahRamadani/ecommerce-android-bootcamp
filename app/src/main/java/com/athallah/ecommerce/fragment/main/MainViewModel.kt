package com.athallah.ecommerce.fragment.main

import androidx.lifecycle.ViewModel
import com.athallah.ecommerce.data.repo.AppRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainViewModel (
    private val appRepository: AppRepository
) : ViewModel() {
    fun prefGetAccToken(): String = runBlocking { appRepository.getAccToken().first() }
    fun prefGetUsername(): String = runBlocking { appRepository.getUsername().first() }


}

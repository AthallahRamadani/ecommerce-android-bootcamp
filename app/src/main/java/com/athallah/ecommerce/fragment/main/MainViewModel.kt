package com.athallah.ecommerce.fragment.main

import androidx.lifecycle.ViewModel
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainViewModel (
    private val appRepository: AppRepository,
    private val wishlistRepository: WishlistRepository
) : ViewModel() {
    fun prefGetAccToken(): String = runBlocking { appRepository.getAccToken().first() }
    fun prefGetUsername(): String = runBlocking { appRepository.getUsername().first() }

    val wishlistSize: Flow<Int> = wishlistRepository.getWishlistDataSize()


}

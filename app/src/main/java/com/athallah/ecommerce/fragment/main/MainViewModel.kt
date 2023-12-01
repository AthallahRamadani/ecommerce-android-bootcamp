package com.athallah.ecommerce.fragment.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.NotificationRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainViewModel(
    private val appRepository: AppRepository,
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository,
    private val savedStateHandle: SavedStateHandle,
    private val notificationRepository: NotificationRepository
) : ViewModel() {


    fun prefGetAccToken(): String = runBlocking {
        appRepository.getAccToken().first()
    }

    fun prefGetUsername(): String = runBlocking {
        appRepository.getUsername().first()
    }

    fun wishlistSize(): Flow<Int> = wishlistRepository.getWishlistDataSize()


    var shouldMoveToTransaction =
        savedStateHandle.get<Boolean>(MainFragment.MOVE_TRANSACTION_BUNDLE_KEY) ?: false

    fun cartSize(): Flow<Int> = cartRepository.getCartDataSize()
    fun notificationSize(): Flow<Int> = notificationRepository.getNotificationDataSize()
}

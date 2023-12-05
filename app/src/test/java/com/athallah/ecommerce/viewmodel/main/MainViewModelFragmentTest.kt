package com.athallah.ecommerce.viewmodel.main

import androidx.lifecycle.SavedStateHandle
import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.NotificationRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import com.athallah.ecommerce.fragment.main.MainViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class MainViewModelFragmentTest {

    private lateinit var mainViewModel: MainViewModel
    private val appRepository: AppRepository = mock()
    private val wishlistRepository: WishlistRepository = mock()
    private val cartRepository: CartRepository = mock()
    private val savedStateHandle: SavedStateHandle = mock()
    private val notificationRepository: NotificationRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(
            appRepository,
            wishlistRepository,
            cartRepository,
            savedStateHandle,
            notificationRepository
        )
    }

    @Test
    fun prefGetAccToken() = runTest {
        whenever(appRepository.getAccToken()).thenReturn(flowOf("222"))
        val actualData = mainViewModel.prefGetAccToken()

        assertEquals("222", actualData)
    }

    @Test
    fun prefGetUserName() = runTest {
        whenever(appRepository.getUsername()).thenReturn(flowOf("222"))
        val actualData = mainViewModel.prefGetUsername()

        assertEquals("222", actualData)
    }

    @Test
    fun wishlistSize() = runTest {
        whenever(wishlistRepository.getWishlistDataSize()).thenReturn(flowOf(2))
        val actualData = mainViewModel.wishlistSize().first()

        assertEquals(2, actualData)
    }

    @Test
    fun cartSize() = runTest {
        whenever(cartRepository.getCartDataSize()).thenReturn(flowOf(2))
        val actualData = mainViewModel.cartSize().first()

        assertEquals(2, actualData)
    }

    @Test
    fun notificationSize() = runTest {
        whenever(notificationRepository.getNotificationDataSize()).thenReturn(flowOf(2))
        val actualData = mainViewModel.notificationSize().first()

        assertEquals(2, actualData)
    }
}

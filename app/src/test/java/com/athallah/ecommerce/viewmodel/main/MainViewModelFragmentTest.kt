package com.athallah.ecommerce.viewmodel.main

import androidx.lifecycle.SavedStateHandle
import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.NotificationRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import com.athallah.ecommerce.fragment.main.MainViewModel
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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

    }

    @Test
    fun prefGetUserName() = runTest {

    }

    @Test
    fun wishlistSize() = runTest {

    }

    @Test
    fun cartSize() = runTest {

    }

    @Test
    fun notificationSize() = runTest {

    }
}
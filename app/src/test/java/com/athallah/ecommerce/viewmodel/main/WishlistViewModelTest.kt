package com.athallah.ecommerce.viewmodel.main

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.datasource.model.Wishlist
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import com.athallah.ecommerce.fragment.main.wishlist.WishlistViewModel
import com.athallah.ecommerce.utils.extension.toCart
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
class WishlistViewModelTest {

    private lateinit var wishlistViewModel: WishlistViewModel
    private val wishlistRepository: WishlistRepository = mock()
    private val cartRepository: CartRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val wishlist = Wishlist(
        productId = "String",
        productName = "String",
        productPrice = 2,
        image = "String",
        stock = 2,
        store = "String",
        sale = 2,
        productRating = 2f,
        description = "String",
        totalRating = 2,
        totalSatisfaction = 2,
        brand = "String",
        totalReview = 2,
        variantName = "String",
        variantPrice = 2
    )
    private val cart = wishlist.toCart()

    @Before
    fun setUp() = runTest {
        wishlistViewModel = WishlistViewModel(wishlistRepository, cartRepository)
    }

    @Test
    fun deleteWishlist() = runTest {
        wishlistViewModel.deleteWishlist(wishlist)

        verify(wishlistRepository).deleteWishlist(wishlist)
    }

    @Test
    fun insertCartSuccess() = runTest {
        whenever(cartRepository.isStockReady(cart)).thenReturn(true)
        val actualData = wishlistViewModel.insertCart(wishlist)
        verify(cartRepository).insertCart(cart)

        assertTrue(actualData)
    }
}

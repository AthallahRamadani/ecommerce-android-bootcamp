package com.athallah.ecommerce.viewmodel

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.fragment.cart.CartViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CartViewModelTest {

    private lateinit var cartViewModel: CartViewModel
    private val cartRepository: CartRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        cartViewModel = CartViewModel(cartRepository)
    }

    @Test
    fun deleteCartNotNull() = runTest {
        val cart = Cart(
            "",
            "",
            2,
            "",
            1000000000,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2,
            null,
            true
        )
        cartViewModel.deleteCart(cart)
        verify(cartRepository).deleteCart(cart)
    }

    @Test
    fun deleteCartNull() = runTest {
        val cart = Cart(
            "",
            "",
            2,
            "",
            1000000000,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2,
            null,
            true
        )
        whenever(cartRepository.getCartData()).thenReturn(flowOf(listOf(cart)))
        cartViewModel.deleteCart(null)
        val filteredCart = cartViewModel.cartData().first().filter { it.isChecked }.toTypedArray()
        verify(cartRepository).deleteCart(*filteredCart)
    }

    @Test
    fun updateCartQuantityTrue() = runTest {
        val cart = Cart(
            "",
            "",
            2,
            "",
            1000000000,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2,
            2,
            true
        )
        cartViewModel.updateCartQuantity(cart, true)
        verify(cartRepository).updateCartQuantity(cart, true)
    }

    @Test
    fun updateCartQuantityFalse() = runTest {
        val cart = Cart(
            "",
            "",
            2,
            "",
            1000000000,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2,
            2,
            true
        )
        cartViewModel.updateCartQuantity(cart, false)
        verify(cartRepository).updateCartQuantity(cart, false)
    }

    @Test
    fun updateCartCheckedNotNull() = runTest {
        val cart = Cart(
            "",
            "",
            2,
            "",
            1000000000,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2,
            2,
            true
        )
        cartViewModel.updateCartChecked(true, cart)
        verify(cartRepository).updateCartChecked(true, cart)
    }

    @Test
    fun updateCartCheckedNull() = runTest {
        val cart = Cart(
            "",
            "",
            2,
            "",
            1000000000,
            "",
            2,
            2f,
            "",
            2,
            2,
            "",
            2,
            "",
            2,
            null,
            true
        )
        whenever(cartRepository.getCartData()).thenReturn(flowOf(listOf(cart)))
        cartViewModel.updateCartChecked(true, null)
        val cartData = cartViewModel.cartData().first().toTypedArray()
        verify(cartRepository).updateCartChecked(
            true,
            *cartData
        )
    }
}

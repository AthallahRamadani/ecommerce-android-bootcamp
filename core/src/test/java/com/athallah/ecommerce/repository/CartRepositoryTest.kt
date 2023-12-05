package com.athallah.ecommerce.repository

import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.datasource.room.dao.CartDao
import com.athallah.ecommerce.data.datasource.room.entity.CartEntity
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.CartRepositoryImpl
import com.athallah.ecommerce.utils.extension.toCart
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CartRepositoryTest {

    private lateinit var cartRepository: CartRepository
    private val cartDao: CartDao = mock()

    @Before
    fun setUp() {
        cartRepository =
            CartRepositoryImpl(cartDao)
    }

    @Test
    fun getCartData() = runTest {
        val cart = CartEntity(
            "",
            "",
            2,
            "",
            2,
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
        whenever(cartDao.getData()).thenReturn(flowOf(listOf(cart)))
        val expectedData = listOf(cart.toCart())
        val actualData = cartRepository.getCartData().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getCartDataSize() = runTest {
        whenever(cartDao.getDataSize()).thenReturn(flowOf(2))
        val actualData = cartRepository.getCartDataSize().first()
        assertEquals(2, actualData)
    }

    @Test
    fun isStockReady() = runTest {
        val cart = CartEntity(
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
        ).toCart()

        val actualData = cartRepository.isStockReady(cart)

        assertEquals(true, actualData)
    }

    @Test
    fun insertCart() = runTest {
        val cart = CartEntity(
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

        whenever(cartDao.checkExistData(cart.productId)).thenReturn(false)
        cartRepository.insertCart(cart.toCart())
        verify(cartDao).insert(cart)
    }

    @Test
    fun updateCartQuantity() = runTest {
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
        val cartEntity = CartEntity(
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
            4,
            true
        )
        whenever(cartDao.getDetailData(cart.productId)).thenReturn(flowOf(cartEntity))
        cartRepository.updateCartQuantity(cart, true)
        verify(cartDao).update(cartEntity.copy(quantity = 5))
    }

    @Test
    fun updateCartChecked() = runTest {
        val cartEntity = CartEntity(
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
            4,
            true
        )
        cartRepository.updateCartChecked(isChecked = true, cartEntity.toCart())
        verify(cartDao).update(cartEntity)
    }

    @Test
    fun deleteCart() = runTest {
        cartRepository.deleteCart()
        verify(cartDao).delete()
    }
}

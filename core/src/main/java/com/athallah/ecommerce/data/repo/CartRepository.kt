package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.datasource.model.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getCartData(): Flow<List<Cart>>
    fun getCartDataSize(): Flow<Int>
    suspend fun isStockReady(cart: Cart): Boolean
    suspend fun insertCart(cart: Cart)
    suspend fun updateCartQuantity(cart: Cart, isInsert: Boolean)
    suspend fun updateCartChecked(isChecked: Boolean, vararg cart: Cart)
    suspend fun deleteCart(vararg cart: Cart)
}
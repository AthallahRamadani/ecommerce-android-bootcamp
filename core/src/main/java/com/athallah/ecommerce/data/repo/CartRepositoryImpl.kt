package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.datasource.room.dao.CartDao
import com.athallah.ecommerce.utils.extension.toCart
import com.athallah.ecommerce.utils.extension.toCartEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {
    override fun getCartData(): Flow<List<Cart>> = cartDao.getData().map { value ->
        value.map { it.toCart() }
    }

    override fun getCartDataSize(): Flow<Int> = cartDao.getDataSize()

    override suspend fun isStockReady(cart: Cart): Boolean {
        val stock = cart.stock
        val quantity = if (cart.quantity == null) {
            val cartIsExisted = cartDao.checkExistData(cart.productId)
            if (cartIsExisted) {
                cartDao.getDetailData(cart.productId).first().quantity
            } else {
                0
            }
        } else {
            cart.quantity
        }
        return quantity < stock
    }

    override suspend fun insertCart(cart: Cart) {
        val cartIsExisted = cartDao.checkExistData(cart.productId)
        if (cartIsExisted) {
            updateCartQuantity(cart, true)
        } else {
            cartDao.insert(cart.toCartEntity())
        }
    }

    override suspend fun updateCartQuantity(cart: Cart, isInsert: Boolean) {
        val number = if (isInsert) 1 else -1

        val cartEntity = if (cart.quantity == null) {
            cartDao.getDetailData(cart.productId).first()
        } else {
            cart.toCartEntity()
        }

        cartDao.update(cartEntity.copy(quantity = cartEntity.quantity + number))
    }

    override suspend fun updateCartChecked(isChecked: Boolean, vararg cart: Cart) {
        val arrayCartEntity = cart.map {
            it.toCartEntity().copy(isChecked = isChecked)
        }.toTypedArray()
        cartDao.update(*arrayCartEntity)
    }

    override suspend fun deleteCart(vararg cart: Cart) {
        val arrayCartEntity = cart.map { it.toCartEntity() }.toTypedArray()
        cartDao.delete(*arrayCartEntity)
    }
}

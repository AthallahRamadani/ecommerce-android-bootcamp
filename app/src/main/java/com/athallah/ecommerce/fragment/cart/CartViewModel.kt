package com.athallah.ecommerce.fragment.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.repo.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
    var totalPrice = 0

    fun cartData(): Flow<List<Cart>> = cartRepository.getCartData()

    fun deleteCart(cart: Cart? = null) {
        viewModelScope.launch {
            if (cart != null) {
                cartRepository.deleteCart(cart)
            } else {
                val filteredCart = cartData().first().filter { it.isChecked}.toTypedArray()
                cartRepository.deleteCart(*filteredCart)
            }
        }
    }

    fun updateCartQuantity(cart: Cart, isInsert: Boolean) {
        viewModelScope.launch {
            if (isInsert && cart.stock > cart.quantity!!) {
                cartRepository.updateCartQuantity(cart, true)
            } else if (!isInsert && cart.quantity!! > 1) {
                cartRepository.updateCartQuantity(cart, false)
            }
        }
    }

    fun updateCartChecked(isChecked: Boolean, cart: Cart? = null) {
        viewModelScope.launch {
            if (cart!= null){
                cartRepository.updateCartChecked(isChecked, cart)
            }
            else {
                cartRepository.updateCartChecked(isChecked, *cartData().first().toTypedArray())
            }
        }
    }

}
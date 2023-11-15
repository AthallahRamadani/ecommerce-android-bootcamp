package com.athallah.ecommerce.fragment.checkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.data.repo.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _listData: MutableStateFlow<List<Cart>> = MutableStateFlow(ArrayList())
    val listData: StateFlow<List<Cart>> = _listData

    var paymentItem: Payment.PaymentItem? = null
    var totalPrice = 0

    init {
        setData(savedStateHandle.get<ArrayList<Cart>>(CheckoutFragment.ARG_DATA) ?: ArrayList())
    }

    fun setData(data: ArrayList<Cart>) {
        _listData.value = data.map { it.copy(quantity = it.quantity ?: 1) }
    }

    fun updateDataQuantity(cart: Cart, isInsert: Boolean) {
        if (isInsert && cart.stock > cart.quantity!!) {
            updateQuantity(cart, 1)
        } else if (!isInsert && cart.quantity!! > 1) {
            updateQuantity(cart, -1)
        }
    }

    private fun updateQuantity(cart: Cart, number: Int) {
        _listData.update {
            it.map { data ->
                if (data.productId == cart.productId) {
                    data.copy(quantity = data.quantity?.plus(number))
                } else {
                    data
                }
            }
        }
    }
}
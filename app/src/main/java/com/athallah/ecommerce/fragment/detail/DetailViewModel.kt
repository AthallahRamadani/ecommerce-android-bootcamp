package com.athallah.ecommerce.fragment.detail

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.DetailProduct
import com.athallah.ecommerce.data.datasource.model.Wishlist
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import com.athallah.ecommerce.utils.extension.toCart
import com.athallah.ecommerce.utils.extension.toWishlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailViewModel(
    private val storeRepository: StoreRepository,
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var isWishlist: Boolean = false


    var productId: String = savedStateHandle["product_id"]?:""

    var detailProduct: DetailProduct? = null
    var productVariant: DetailProduct.ProductVariant? = null



    private val _detailProductState = MutableStateFlow<ResultState<DetailProduct>?>(null)
    val detailProductState: StateFlow<ResultState<DetailProduct>?> = _detailProductState

    init {
        getDetailProduct()
    }


    fun getDetailProduct() {
        viewModelScope.launch {
            storeRepository.detailProducts(productId).collect { resultState ->
                Log.d("TAG", "getDetailProduct: ${resultState}")
                Log.d("TAG", "getDetailProduct: ${productId}")
                _detailProductState.value = resultState
            }
        }
    }

    fun checkExistWishlist(): Boolean {
        return runBlocking {
            wishlistRepository.checkExistWishlistData(productId)
        }
    }

    fun insertWishlist() {
        if (detailProduct != null) {
            viewModelScope.launch {
                wishlistRepository.insertWishlist(detailProduct!!.toWishlist(productVariant!!))
            }
            isWishlist = true
        }
    }

    fun deleteWishlist() {
        if (detailProduct != null) {
            viewModelScope.launch {
                wishlistRepository.deleteWishlist(detailProduct!!.toWishlist(productVariant!!))
            }
            isWishlist = false
        }
    }


    fun insertCart(): Boolean {
        return if (detailProduct != null) {

            val cart = detailProduct!!.toCart(productVariant!!)
            val isStockReady = runBlocking { cartRepository.isStockReady(cart) }
            if (isStockReady) {
                viewModelScope.launch {
                    Log.d("yolo", "insertCart: ${cart}")
                    cartRepository.insertCart(cart)
                }
                true
            } else {
                false
            }
        } else false
    }
}
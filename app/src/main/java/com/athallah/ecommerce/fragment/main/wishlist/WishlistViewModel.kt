package com.athallah.ecommerce.fragment.main.wishlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.datasource.model.DetailProduct
import com.athallah.ecommerce.data.datasource.model.Wishlist
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import com.athallah.ecommerce.utils.extension.toCart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WishlistViewModel(
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    var recyclerViewType = WishlistAdapter.ONE_COLUMN_VIEW_TYPE
    val wishlistData: Flow<List<Wishlist>> = wishlistRepository.getWishlistData()
    var detailProduct: DetailProduct? = null
    var productVariant: DetailProduct.ProductVariant? = null

    fun deleteWishlist(wishlist: Wishlist) {
        viewModelScope.launch {
            wishlistRepository.deleteWishlist(wishlist)
        }
    }

    fun insertCart(wishlist: Wishlist): Boolean {
        val cart = wishlist.toCart()
        val isStockReady = runBlocking { cartRepository.isStockReady(cart) }
        return if (isStockReady) {
            viewModelScope.launch {
                cartRepository.insertCart(cart)
            }
            true
        } else {
            false
        }
    }

}
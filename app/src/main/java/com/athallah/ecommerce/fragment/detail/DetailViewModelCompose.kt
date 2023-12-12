package com.athallah.ecommerce.fragment.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.DetailProduct
import com.athallah.ecommerce.data.repo.CartRepository
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.data.repo.WishlistRepository
import com.athallah.ecommerce.utils.extension.toCart
import com.athallah.ecommerce.utils.extension.toWishlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailViewModelCompose(
    private val storeRepository: StoreRepository,
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailComposeState = MutableStateFlow(DetailComposeState())
    val detailComposeState = _detailComposeState.asStateFlow()

    val productId: String = savedStateHandle[DetailFragment.BUNDLE_PRODUCT_ID_KEY] ?: ""

    init {
        initState()
        getDetailProduct()
    }

    fun getDetailProduct() {
        viewModelScope.launch {
            storeRepository.detailProducts(productId).collect { resultState ->
                _detailComposeState.update {
                    if (resultState is ResultState.Success) {
                        it.copy(
                            resultState = resultState,
                            detailProduct = resultState.data,
                            productVariant = resultState.data.productVariant[0],
                            isWishlist = checkExistWishlist()
                        )
                    } else {
                        it.copy(resultState = resultState)
                    }
                }
            }
        }
    }

    private fun checkExistWishlist(): Boolean {
        return runBlocking {
            wishlistRepository.checkExistWishlistData(productId)
        }
    }

    fun insertWishlist() {
        viewModelScope.launch {
            val product = _detailComposeState.value.detailProduct
            val variant = _detailComposeState.value.productVariant
            wishlistRepository.insertWishlist(product!!.toWishlist(variant!!))
        }
        _detailComposeState.update {
            it.copy(isWishlist = true)
        }
    }

    fun deleteWishlist() {
        viewModelScope.launch {
            val product = _detailComposeState.value.detailProduct
            val variant = _detailComposeState.value.productVariant
            wishlistRepository.deleteWishlist(product!!.toWishlist(variant!!))
        }
        _detailComposeState.update {
            it.copy(isWishlist = false)
        }
    }

    fun updateVariant(variant: DetailProduct.ProductVariant) {
        _detailComposeState.update {
            it.copy(productVariant = variant)
        }
    }

    fun insertCart(): Boolean {
        val product = detailComposeState.value.detailProduct!!
        val variant = detailComposeState.value.productVariant!!
        val cart = product.toCart(variant)
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

    private fun initState() {
        _detailComposeState.update {
            it.copy(productId = productId)
        }
    }
}

data class DetailComposeState(
    val productId: String = "",
    val isWishlist: Boolean = false,
    val detailProduct: DetailProduct? = null,
    val productVariant: DetailProduct.ProductVariant? = null,
    val resultState: ResultState<DetailProduct>? = null
)

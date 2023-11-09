package com.athallah.ecommerce.fragment.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.model.DetailProduct
import com.athallah.ecommerce.data.repo.StoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val storeRepository: StoreRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {



    var productId: String? = savedStateHandle["product_id"]

    var variant: String? = null
    var detailProduct: DetailProduct? = null

    init {
        getDetailProduct()
    }


    private val _detailProductState = MutableStateFlow<ResultState<DetailProduct>?>(null)
    val detailProductState: StateFlow<ResultState<DetailProduct>?> = _detailProductState

    fun getDetailProduct() {
        if (productId != null) {
            viewModelScope.launch {
                storeRepository.detailProducts(productId!!).collect {resultState ->
                    _detailProductState.value = resultState
                }
            }
        }
    }
}
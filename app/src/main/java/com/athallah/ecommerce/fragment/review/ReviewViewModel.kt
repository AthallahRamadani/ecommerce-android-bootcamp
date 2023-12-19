package com.athallah.ecommerce.fragment.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Review
import com.athallah.ecommerce.data.repo.StoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val storeRepository: StoreRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var productId: String? = savedStateHandle[ReviewFragmentCompose.BUNDLE_PRODUCT_ID_KEY] ?: ""

    private val _reviewProductsState = MutableStateFlow<ResultState<List<Review>>?>(null)
    val reviewProductState: StateFlow<ResultState<List<Review>>?> = _reviewProductsState

    init {
        getListReview()
    }

    fun getListReview() {
        viewModelScope.launch {
            storeRepository.reviewProducts(productId!!).collect { resultState ->
                _reviewProductsState.value = resultState
            }
        }
    }
}

package com.athallah.ecommerce.fragment.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Fulfillment
import com.athallah.ecommerce.data.repo.FulfillmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatusViewModel(
    private val fulfillmentRepository: FulfillmentRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val detailTransaction = savedStateHandle.get<Fulfillment>(StatusFragment.DATA_BUNDLE_KEY)
    val rating = savedStateHandle.get<Int>(StatusFragment.RATING_BUNDLE_KEY)
    val review = savedStateHandle.get<String>(StatusFragment.REVIEW_BUNDLE_KEY)

    private val _ratingState = MutableStateFlow<ResultState<Boolean>?>(null)
    val ratingState: StateFlow<ResultState<Boolean>?> = _ratingState

    fun sendRating(invoiceId: String, rating: Int?, review: String?) {
        viewModelScope.launch {
            fulfillmentRepository.rating(invoiceId, rating, review).collect { result ->
                _ratingState.value = result
            }
        }
    }

}
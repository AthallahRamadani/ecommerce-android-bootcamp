package com.athallah.ecommerce.fragment.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Payment
import com.athallah.ecommerce.data.repo.FulfillmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PaymentViewModel(
private val fulfillmentRepository: FulfillmentRepository
) : ViewModel() {

    private val _paymentState = MutableStateFlow<ResultState<List<Payment>>?>(null)
    val paymentState: StateFlow<ResultState<List<Payment>>?> = _paymentState

    fun getDetailProduct() {
        viewModelScope.launch {
            fulfillmentRepository.getPaymentMethod().collect {resultState ->
                _paymentState.value = resultState
            }
        }
    }

}
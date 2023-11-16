package com.athallah.ecommerce.fragment.main.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Transaction
import com.athallah.ecommerce.data.repo.FulfillmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val fulfillmentRepository: FulfillmentRepository
): ViewModel() {
    private val _transactionState = MutableStateFlow<ResultState<List<Transaction>>?>(null)
    val transactionState: StateFlow<ResultState<List<Transaction>>?> = _transactionState

    init {
        getListTransaction()
    }

    fun getListTransaction() {
        viewModelScope.launch {
            fulfillmentRepository.getTransaction().collect { resultState ->
                _transactionState.value = resultState
            }
        }
    }
}
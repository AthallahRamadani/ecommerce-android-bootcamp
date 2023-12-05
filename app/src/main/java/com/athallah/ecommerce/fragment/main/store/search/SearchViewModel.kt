package com.athallah.ecommerce.fragment.main.store.search

import androidx.lifecycle.ViewModel
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.repo.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.runBlocking

class SearchViewModel(
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _searchEditText: MutableStateFlow<String> = MutableStateFlow("")
    private val _searchQuery: Flow<String> = _searchEditText.debounce(1000)
    val searchData: Flow<ResultState<List<String>>> = _searchQuery.flatMapLatest { query ->
        storeRepository.searchProducts(query)
    }

    fun getSearchData(query: String) {
        runBlocking {
            _searchEditText.value = query
        }
    }
}

package com.athallah.ecommerce.fragment.main.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import com.athallah.ecommerce.data.datasource.model.Product
import com.athallah.ecommerce.data.repo.AppRepository
import com.athallah.ecommerce.data.repo.StoreRepository
import com.athallah.ecommerce.fragment.main.store.storeadapter.ProductAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel(
    private val storeRepository: StoreRepository,
    private val appRepository: AppRepository
) : ViewModel() {

    var recyclerViewType = ProductAdapter.ONE_COLUMN_VIEW_TYPE

    var resSortFilterProduct: Int? = null
    var resBrandFilterProduct: Int? = null

    var productsQuery: MutableStateFlow<ProductsQuery> = MutableStateFlow(ProductsQuery())

    val productsData: Flow<PagingData<Product>> = productsQuery.flatMapLatest { query ->
        Log.d("bebaslah", ": bau ll ")
        storeRepository.getProducts(query)
    }.cachedIn(viewModelScope)

    fun getFilterData(query: ProductsQuery) {
        viewModelScope.launch {
            productsQuery.update {
                query.copy(search = it.search)
            }
        }
    }

    fun getSearchData(query: String?) {
        viewModelScope.launch {
            productsQuery.update {
                it.copy(search = query)
            }
        }
    }

    fun prefGetAccToken(): Flow<String> = appRepository.getAccToken()
    fun resetData() {
        viewModelScope.launch {
            productsQuery.value = ProductsQuery()
            resSortFilterProduct = null
            resBrandFilterProduct = null
        }
    }
}

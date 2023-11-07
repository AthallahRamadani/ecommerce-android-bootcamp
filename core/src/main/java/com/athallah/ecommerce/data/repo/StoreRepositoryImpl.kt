package com.athallah.ecommerce.data.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.ProductsPagingSource
import com.athallah.ecommerce.data.datasource.api.model.Product
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.utils.extension.getErrorMessage
import com.athallah.ecommerce.utils.extension.toProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StoreRepositoryImpl(
    private val apiService: ApiService,
    private val preferences: UserDataStore
) : StoreRepository {
    override fun getProducts(query: ProductsQuery): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 1, initialLoadSize = 10),
            pagingSourceFactory = {
                ProductsPagingSource(apiService, query, preferences)
            },
        ).flow.map {
            it.map { productsResponseItem ->
                productsResponseItem.toProduct()
            }
        }
    }

    override fun searchProducts(query: String): Flow<ResultState<List<String>>> = flow {
        emit(ResultState.Loading)
        try {
            if (query.isNotEmpty()) {
                val response = apiService.search(query)
                val listSearch = response.data ?: emptyList()
                emit(ResultState.Success(listSearch))
            } else {
                emit(ResultState.Success(emptyList()))
            }
        } catch (e: Exception) {
            val message = e.getErrorMessage()
            emit(ResultState.Error(message.toString()))
        }
    }

}
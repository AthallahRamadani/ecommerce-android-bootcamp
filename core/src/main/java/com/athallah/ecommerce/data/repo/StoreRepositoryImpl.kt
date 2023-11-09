package com.athallah.ecommerce.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.ProductsPagingSource
import com.athallah.ecommerce.data.datasource.api.model.DetailProduct
import com.athallah.ecommerce.data.datasource.api.model.Product
import com.athallah.ecommerce.data.datasource.api.model.Review
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.utils.extension.getErrorMessage
import com.athallah.ecommerce.utils.extension.toDetailProduct
import com.athallah.ecommerce.utils.extension.toProduct
import com.athallah.ecommerce.utils.extension.toReview
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

    override fun detailProducts(id: String): Flow<ResultState<DetailProduct>> = flow{
        emit(ResultState.Loading)
        try {
            val response = apiService.detailProducts(id)
            val data = response.data?.toDetailProduct()
            if (data != null) emit(ResultState.Success(data))
            else emit(ResultState.Error("No data"))
        } catch (e: Exception) {
            val message = e.getErrorMessage()
            emit(ResultState.Error(message.toString()))
        }
    }

    override fun reviewProducts(id: String): Flow<ResultState<List<Review>>> = flow {
        emit(ResultState.Loading)
        try{
            val response = apiService.reviewProducts(id)
            val data = response.data?.map{ it.toReview()} ?: emptyList()
            emit(ResultState.Success(data))
        } catch (e: Exception) {
            val message = e.getErrorMessage()
            emit(ResultState.Error(message.toString()))
        }
    }
}
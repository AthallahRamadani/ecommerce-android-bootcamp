package com.athallah.ecommerce.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.model.Product
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    fun getProducts(query: ProductsQuery): Flow<PagingData<Product>>
    fun searchProducts(query: String): Flow<ResultState<List<String>>>

}
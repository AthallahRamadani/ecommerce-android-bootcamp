package com.athallah.ecommerce.data.repo

import androidx.paging.PagingData
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.model.DetailProduct
import com.athallah.ecommerce.data.datasource.api.model.Product
import com.athallah.ecommerce.data.datasource.api.model.Review
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    fun getProducts(query: ProductsQuery): Flow<PagingData<Product>>
    fun searchProducts(query: String): Flow<ResultState<List<String>>>
    fun detailProducts(id: String): Flow<ResultState<DetailProduct>>
    fun reviewProducts(id: String): Flow<ResultState<List<Review>>>
}
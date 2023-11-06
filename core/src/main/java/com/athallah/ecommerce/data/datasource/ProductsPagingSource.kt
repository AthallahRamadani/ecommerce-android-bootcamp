package com.athallah.ecommerce.data.datasource

import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import com.athallah.ecommerce.data.datasource.api.response.ProductsResponseItem
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.utils.extension.toBearerToken
import com.athallah.ecommerce.utils.extension.toQueryMap
import kotlinx.coroutines.flow.first

class ProductsPagingSource(
    private val apiService: ApiService,
    private val productsQuery: ProductsQuery,
    private val preferences: UserDataStore
) : PagingSource<Int, ProductsResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, ProductsResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductsResponseItem> {
        return try {
            val position = params.key ?: 1
            val response = apiService.products(
                productsQuery.copy(
                    page = position,
                    limit = params.loadSize
                ).toQueryMap(),
                preferences.getAccToken().first().toBearerToken()
            )
            LoadResult.Page(
                data = response.data?.items ?: emptyList(),
                prevKey = null,
                nextKey = if (position == response.data?.totalPages) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

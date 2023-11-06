package com.athallah.ecommerce.data.datasource.api.request

data class ProductsQuery(
    val search: String? = null,
    val brand: String? = null,
    val lowest: String? = null,
    val highest: String? = null,
    val sort: String? = null,
    val limit: Int? = null,
    val page: Int? = null
)

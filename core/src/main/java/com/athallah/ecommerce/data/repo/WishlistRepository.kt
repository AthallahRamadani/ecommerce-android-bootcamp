package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.datasource.model.Wishlist
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlistData(): Flow<List<Wishlist>>
    fun getWishlistDataSize(): Flow<Int>
    suspend fun checkExistWishlistData(wishlistId: String): Boolean
    suspend fun insertWishlist(wishlist: Wishlist)
    suspend fun deleteWishlist(wishlist: Wishlist)
}
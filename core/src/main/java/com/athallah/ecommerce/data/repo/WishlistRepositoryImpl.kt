package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.datasource.model.Wishlist
import com.athallah.ecommerce.data.datasource.api.service.ApiService
import com.athallah.ecommerce.data.datasource.preference.UserDataStore
import com.athallah.ecommerce.data.datasource.room.dao.WishlistDao
import com.athallah.ecommerce.data.datasource.room.entity.WishlistEntity
import com.athallah.ecommerce.utils.extension.toWishlist
import com.athallah.ecommerce.utils.extension.toWishlistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WishlistRepositoryImpl(
    private val wishlistDao: WishlistDao
) : WishlistRepository {
    override fun getWishlistData(): Flow<List<Wishlist>> =
        wishlistDao.getData().map{value: List<WishlistEntity> ->
        value.map { it.toWishlist() }
    }

    override fun getWishlistDataSize(): Flow<Int> = wishlistDao.getDataSize()

    override suspend fun checkExistWishlistData(wishlistId: String): Boolean =
        wishlistDao.checkExistData(wishlistId)

    override suspend fun insertWishlist(wishlist: Wishlist) {
        wishlistDao.insert(wishlist.toWishlistEntity())
    }

    override suspend fun deleteWishlist(wishlist: Wishlist) {
        wishlistDao.delete(wishlist.toWishlistEntity())
    }


}
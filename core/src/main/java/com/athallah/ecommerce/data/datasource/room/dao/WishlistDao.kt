package com.athallah.ecommerce.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.athallah.ecommerce.data.datasource.room.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist")
    fun getData(): Flow<List<WishlistEntity>>

    @Query("SELECT COUNT(product_id) FROM wishlist")
    fun getDataSize(): Flow<Int>

    @Query("SELECT EXISTS(SELECT * FROM wishlist WHERE product_id = :wishlistId)")
    suspend fun checkExistData(wishlistId: String): Boolean

    @Insert
    suspend fun insert(wishlist: WishlistEntity)

    @Delete
    suspend fun delete(wishlist: WishlistEntity)

    @Query("DELETE FROM wishlist")
    suspend fun clearTable()
}

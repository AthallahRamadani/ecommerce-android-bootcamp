package com.athallah.ecommerce.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.athallah.ecommerce.data.datasource.room.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    fun getData(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart WHERE product_id = :cartId")
    fun getDetailData(cartId: String): Flow<CartEntity>

    @Query("SELECT COUNT(product_id) FROM cart")
    fun getDataSize(): Flow<Int>

    @Query("SELECT EXISTS(SELECT * FROM cart WHERE product_id = :cartId)")
    suspend fun checkExistData(cartId: String): Boolean

    @Update
    suspend fun update(vararg cart: CartEntity)

    @Insert
    suspend fun insert(cart: CartEntity)

    @Delete
    suspend fun delete(vararg cart: CartEntity)

    @Query("DELETE FROM cart")
    suspend fun clearTable()
}
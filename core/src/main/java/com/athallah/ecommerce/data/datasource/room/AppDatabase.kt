package com.athallah.ecommerce.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.athallah.ecommerce.data.datasource.room.dao.WishlistDao
import com.athallah.ecommerce.data.datasource.room.entity.WishlistEntity

@Database(entities = [WishlistEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wishlistDao() : WishlistDao
}
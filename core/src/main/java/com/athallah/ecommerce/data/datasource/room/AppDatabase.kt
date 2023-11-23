package com.athallah.ecommerce.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.athallah.ecommerce.data.datasource.room.dao.CartDao
import com.athallah.ecommerce.data.datasource.room.dao.NotificationDao
import com.athallah.ecommerce.data.datasource.room.dao.WishlistDao
import com.athallah.ecommerce.data.datasource.room.entity.CartEntity
import com.athallah.ecommerce.data.datasource.room.entity.NotificationEntity
import com.athallah.ecommerce.data.datasource.room.entity.WishlistEntity

@Database(entities = [WishlistEntity::class, CartEntity::class, NotificationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
    abstract fun cartDao(): CartDao
    abstract fun notificationDao(): NotificationDao
}
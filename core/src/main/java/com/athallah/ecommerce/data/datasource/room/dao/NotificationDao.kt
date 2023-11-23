package com.athallah.ecommerce.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.athallah.ecommerce.data.datasource.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import androidx.room.Update
import com.athallah.ecommerce.data.datasource.model.Notification

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification ORDER BY id DESC")
    fun getData(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(id) FROM notification WHERE is_read = 0")
    fun getDataSize(): Flow<Int>

    @Update
    suspend fun update(notification: NotificationEntity)

    @Insert
    suspend fun insert(notification: NotificationEntity)

    @Query("DELETE FROM notification")
    suspend fun clearTable()
}
package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.datasource.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotificationData(): Flow<List<Notification>>
    fun getNotificationDataSize(): Flow<Int>
    suspend fun insertNotification(notification: Notification)
    suspend fun updateNotification(notification: Notification)
}

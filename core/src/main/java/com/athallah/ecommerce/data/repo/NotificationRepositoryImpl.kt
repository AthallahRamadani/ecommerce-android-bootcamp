package com.athallah.ecommerce.data.repo

import com.athallah.ecommerce.data.datasource.model.Notification
import com.athallah.ecommerce.data.datasource.room.dao.NotificationDao
import com.athallah.ecommerce.data.datasource.room.entity.NotificationEntity
import com.athallah.ecommerce.utils.extension.toNotification
import com.athallah.ecommerce.utils.extension.toNotificationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepositoryImpl(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    override fun getNotificationData(): Flow<List<Notification>> =
        notificationDao.getData().map { value: List<NotificationEntity> ->
            value.map { it.toNotification() }
        }

    override fun getNotificationDataSize(): Flow<Int> = notificationDao.getDataSize()

    override suspend fun insertNotification(notification: Notification) {
        notificationDao.insert(notification.toNotificationEntity())
    }

    override suspend fun updateNotification(notification: Notification) {
        notificationDao.update(notification.toNotificationEntity())
    }
}

package com.athallah.ecommerce.repository

import com.athallah.ecommerce.data.datasource.model.Notification
import com.athallah.ecommerce.data.datasource.room.dao.NotificationDao
import com.athallah.ecommerce.data.datasource.room.entity.NotificationEntity
import com.athallah.ecommerce.data.repo.CartRepositoryImpl
import com.athallah.ecommerce.data.repo.NotificationRepository
import com.athallah.ecommerce.data.repo.NotificationRepositoryImpl
import com.athallah.ecommerce.utils.extension.toCart
import com.athallah.ecommerce.utils.extension.toNotification
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class NotificationRepositoryTest {

    private lateinit var notificationRepository: NotificationRepository
    private val notificationDao: NotificationDao = mock()

    @Before
    fun setUp() {
        notificationRepository =
            NotificationRepositoryImpl(notificationDao)
    }

    @Test
    fun getNotificationData() = runTest {
        val notification = NotificationEntity(
            id = 2,
            title = "null",
            body = "null",
            image = "null",
            type = "null",
            date = "null",
            time = "null",
            isRead = true
        )

        whenever(notificationDao.getData()).thenReturn(flowOf(listOf(notification)))
        val expectedData = listOf(notification.toNotification())
        val actualData = notificationRepository.getNotificationData().first()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getNotificationDataSize() = runTest {
        whenever(notificationDao.getDataSize()).thenReturn(flowOf(2))
        val actualData = notificationRepository.getNotificationDataSize().first()
        Assert.assertEquals(2, actualData)
    }

    @Test
    fun insertNotification() = runTest {
        val notification = NotificationEntity(
            id = 2,
            title = "null",
            body = "null",
            image = "null",
            type = "null",
            date = "null",
            time = "null",
            isRead = true
        )
        notificationRepository.insertNotification(notification.toNotification())
        verify(notificationDao).insert(notification)
    }

    @Test
    fun updateNotification() = runTest {
        val notificationEntity = NotificationEntity(
            id = 2,
            title = "null",
            body = "null",
            image = "null",
            type = "null",
            date = "null",
            time = "null",
            isRead = true
        )

        notificationRepository.updateNotification(notificationEntity.toNotification())
        verify(notificationDao).update(notificationEntity)
    }
}
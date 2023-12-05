package com.athallah.ecommerce.viewmodel

import com.athallah.ecommerce.MainDispatcherRule
import com.athallah.ecommerce.data.datasource.model.Notification
import com.athallah.ecommerce.data.repo.NotificationRepository
import com.athallah.ecommerce.fragment.notification.NotificationViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class NotificationViewModelTest {
    private lateinit var notificationViewModel: NotificationViewModel
    private val notificationRepository: NotificationRepository = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val notification = Notification(
        id = null,
        title = "String",
        body = "String",
        image = "String",
        type = "String",
        date = "String",
        time = "String",
        isRead = false
    )

    @Before
    fun setUp() {
        notificationViewModel = NotificationViewModel(notificationRepository)
    }

    @Test
    fun notificationData() = runTest {
        whenever(notificationRepository.getNotificationData()).thenReturn(
            flowOf(listOf(notification))
        )

        val actualData = notificationViewModel.notificationData().first()

        assertEquals(
            listOf(notification),
            actualData
        )
    }

    @Test
    fun readNotification() = runTest {
        notificationViewModel.readNotification(notification)

        verify(notificationRepository).updateNotification(notification.copy(isRead = true))
    }
}

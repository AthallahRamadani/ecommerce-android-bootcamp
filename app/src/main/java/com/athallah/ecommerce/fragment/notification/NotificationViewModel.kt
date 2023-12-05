package com.athallah.ecommerce.fragment.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athallah.ecommerce.data.datasource.model.Notification
import com.athallah.ecommerce.data.repo.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    fun notificationData(): Flow<List<Notification>> =
        notificationRepository.getNotificationData()

    fun readNotification(data: Notification) {
        viewModelScope.launch {
            notificationRepository.updateNotification(data.copy(isRead = true))
        }
    }
}

package com.athallah.ecommerce.firebase

import android.util.Log
import com.athallah.ecommerce.utils.extension.toNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseCloudMessage : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val data = message.data.toNotification()
        Log.d("TAGTAGTAG", "onMessageReceived: $message")
    }
}
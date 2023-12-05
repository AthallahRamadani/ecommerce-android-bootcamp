package com.athallah.ecommerce.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.athallah.ecommerce.MainActivity
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Notification
import com.athallah.ecommerce.data.datasource.room.dao.NotificationDao
import com.athallah.ecommerce.utils.extension.toNotification
import com.athallah.ecommerce.utils.extension.toNotificationEntity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FirebaseCloudMessage : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private val notificationDao: NotificationDao by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val data = message.data.toNotification()
        sendNotification(data)
        saveNotification(data)
        Log.d("TAGTAGTAG", "onMessageReceived: $data")
    }

    private fun sendNotification(data: Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.app_navigation)
            .setDestination(R.id.notificationFragment)
            .createPendingIntent()
        val imageBitmap = Picasso.get().load(data.image).get()
        val channelId = getString(R.string.sale)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(data.title)
            .setContentText(data.body)
            .setLargeIcon(imageBitmap)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun saveNotification(data: Notification) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationDao.insert(data.toNotificationEntity())
        }
    }
}

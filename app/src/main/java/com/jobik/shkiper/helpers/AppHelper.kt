package com.jobik.shkiper.helpers

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.jobik.shkiper.services.notification_service.NotificationScheduler

fun areChanelNotificationsEnabled(context: Context, channelId: String): Boolean {
    runCatching {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        var channel = notificationManager.getNotificationChannel(channelId)
        if (channel == null) {
            val notificationScheduler = NotificationScheduler(context)
            notificationScheduler.createNotificationChannel(
                NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL,
                context
            )
        }
        channel = notificationManager.getNotificationChannel(channelId)
        return channel.importance != NotificationManager.IMPORTANCE_NONE
    }
    return false
}

fun areNotificationsEnabled(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}
package com.jobik.shkiper.helpers

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat

fun areChanelNotificationsEnabled(context: Context, channelId: String): Boolean {
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    val channel = notificationManager.getNotificationChannel(channelId)
    return channel.importance != NotificationManager.IMPORTANCE_NONE
}

fun areNotificationsEnabled(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}
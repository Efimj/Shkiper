package com.jobik.shkiper.helpers

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.jobik.shkiper.services.notification.NotificationScheduler

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

fun areEXACTNotificationsEnabled(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java) ?: return true
        return alarmManager.canScheduleExactAlarms()
    }
    return true
}

fun areNotificationsEnabled(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}
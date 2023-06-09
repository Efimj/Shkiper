package com.example.notepadapp.notification_service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class NotificationScheduler(private val context: Context) {
    companion object {
        enum class NotificationChannels(val channelId: String, val channelName: String, val importance: Int) {
            NOTECHANNEL(
                "NOTECHANNEL",
                "notechennel",
                NotificationManager.IMPORTANCE_HIGH
            )
        }
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun scheduleNotification(
        notificationData: NotificationData,
        delay: Long,
        notificationId: String,
        requestID: Int = 0,
        channel: NotificationChannels = NotificationChannels.NOTECHANNEL
    ) {
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        notificationIntent.putExtra("channelId", channel.channelId)
        notificationIntent.putExtra("id", notificationId)
        notificationIntent.putExtra("title", notificationData.title)
        notificationIntent.putExtra("message", notificationData.message)
        notificationIntent.putExtra("icon", notificationData.icon)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestID,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + delay
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    fun cancelScheduledNotification() {
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    fun createNotificationChannel(channel: NotificationChannels) {
        val createdChannel = NotificationChannel(channel.channelId, channel.channelName, channel.importance)
        notificationManager.createNotificationChannel(createdChannel)
    }
}

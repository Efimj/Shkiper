package com.example.notepadapp.notification_service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.SyncStateContract.Constants
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.database.models.Reminder
import com.example.notepadapp.database.models.RepeatMode

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
        requestCode: Int,
        trigger: Long,
        channel: NotificationChannels = NotificationChannels.NOTECHANNEL
    ) {
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        notificationIntent.putExtra("channelId", channel.channelId)
        notificationIntent.putExtra("id", notificationData.id)
        notificationIntent.putExtra("title", notificationData.title)
        notificationIntent.putExtra("message", notificationData.message)
        notificationIntent.putExtra("icon", notificationData.icon)
        notificationIntent.putExtra("repeatMode", notificationData.repeatMode.name)
        notificationIntent.putExtra("requestCode", requestCode)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        when (notificationData.repeatMode) {
            RepeatMode.DAILY -> {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, trigger, AlarmManager.INTERVAL_DAY, pendingIntent)
            }

            RepeatMode.WEEKLY -> {
                val millsInWeek: Long = 24 * 60 * 60 * 1000 * 7
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, trigger, millsInWeek, pendingIntent)
            }

            else ->
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent)
        }
    }

    fun cancelScheduledNotification(requestCode: Int) {
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
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

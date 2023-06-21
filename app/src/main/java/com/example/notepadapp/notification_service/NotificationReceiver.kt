package com.example.notepadapp.notification_service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.notepadapp.R
import com.example.notepadapp.database.models.RepeatMode
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationScheduler = NotificationScheduler(context)
        when (intent.action) {
            Intent.ACTION_DATE_CHANGED -> {
                notificationScheduler.restoreNotifications()
            }

            Intent.ACTION_TIME_CHANGED -> {
                notificationScheduler.restoreNotifications()
            }

            Intent.ACTION_TIMEZONE_CHANGED -> {
                notificationScheduler.restoreNotifications()
            }

            Intent.ACTION_BOOT_COMPLETED -> {
                notificationScheduler.restoreNotifications()
            }

            else -> sendNotification(intent, context)
        }
    }

    private fun sendNotification(intent: Intent, context: Context) {
        val requestCode = intent.getIntExtra("requestCode", 0)
        val notification = NotificationStorage(context).getNotification(requestCode) ?: return
        val notificationBuilder =
            NotificationCompat.Builder(context, notification.channel.channelId)
                .setContentTitle(notification.title)
                .setSmallIcon(notification.icon)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.active))
                .setColorized(true)
                .setStyle(
                    NotificationCompat
                        .BigTextStyle()
                        .bigText(notification.message)
                )


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notification.notificationId, notificationBuilder.build())

        var newNotificationDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(notification.trigger),
            OffsetDateTime.now().offset
        )
        newNotificationDate = when (notification.repeatMode) {
            RepeatMode.DAILY -> {
                newNotificationDate.plusDays(1)
            }

            RepeatMode.WEEKLY -> {
                newNotificationDate.plusDays(7)
            }

            RepeatMode.MONTHLY -> {
                newNotificationDate.plusMonths(1)
            }

            RepeatMode.YEARLY -> {
                newNotificationDate.plusYears(1)
            }

            else -> return
        }

        val milliseconds = newNotificationDate.toInstant(OffsetDateTime.now().offset).toEpochMilli()
        NotificationScheduler(context).scheduleNotification(
            notification.copy(trigger = milliseconds)
        )
    }
}
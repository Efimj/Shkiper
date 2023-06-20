package com.example.notepadapp.notification_service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.notepadapp.database.models.RepeatMode
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime

class NotificationScheduler(private val context: Context) {
    companion object {
        enum class NotificationChannels(val channelId: String, val channelName: String, val importance: Int) {
            NOTECHANNEL(
                "NOTECHANNEL", "notechennel", NotificationManager.IMPORTANCE_HIGH
            )
        }
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationStorage: NotificationStorage = NotificationStorage(context)

    fun scheduleNotification(notificationData: NotificationData) {
        notificationStorage.addOrUpdate(notificationData)
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        notificationIntent.putExtra("requestCode", notificationData.requestCode)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationData.requestCode,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationData.trigger, pendingIntent)
    }

    fun deleteNotification(notification: NotificationData) {
        notificationStorage.remove(notification)
        cancelNotification(notification)
    }

    fun cancelNotification(notification: NotificationData) {
        val notificationIntent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notification.requestCode,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    fun restoreNotifications() {
        notificationStorage.getAll().forEach { notification ->
            //cancelNotification(notification)
            val oldReminderDate = getNotificationDateTime(notification)
            if (oldReminderDate.isBefore(LocalDateTime.now())) {
                scheduleNotification(notification)
            }
            val newReminderDate = newDateForReminder(notification)

            val milliseconds = newReminderDate.toInstant(OffsetDateTime.now().offset).toEpochMilli()
            val newNotification = notification.copy(trigger = milliseconds)
            scheduleNotification(newNotification)
        }
    }

    private fun newDateForReminder(
        notification: NotificationData,
    ): LocalDateTime {
        val oldReminderDate = getNotificationDateTime(notification)

        val newReminderDate = when (notification.repeatMode) {
            RepeatMode.NONE -> {
                if (oldReminderDate.isBefore(LocalDateTime.now())) {
                    notificationStorage.remove(notification)
                    oldReminderDate
                } else {
                    oldReminderDate
                }
            }

            RepeatMode.DAILY -> {
                val updatedReminderDate = LocalDateTime.now()
                    .withHour(oldReminderDate.hour)
                    .withMinute(oldReminderDate.minute)

                if (updatedReminderDate.isBefore(LocalDateTime.now())) {
                    updatedReminderDate.plusDays(1)
                } else {
                    updatedReminderDate
                }
            }

            RepeatMode.WEEKLY -> {
                val updatedReminderDate = LocalDateTime.now()
                    .with(DayOfWeek.from(oldReminderDate.dayOfWeek))
                    .withHour(oldReminderDate.hour)
                    .withMinute(oldReminderDate.minute)

                if (updatedReminderDate.isBefore(LocalDateTime.now())) {
                    updatedReminderDate.plusDays(7)
                } else {
                    updatedReminderDate
                }
            }

            RepeatMode.MONTHLY -> {
                val updatedReminderDate = LocalDateTime.now()
                    .withDayOfMonth(oldReminderDate.dayOfMonth)
                    .withHour(oldReminderDate.hour)
                    .withMinute(oldReminderDate.minute)

                if (updatedReminderDate.isBefore(LocalDateTime.now())) {
                    updatedReminderDate.plusMonths(1)
                } else {
                    updatedReminderDate
                }
            }

            RepeatMode.YEARLY -> {
                val updatedReminderDate = LocalDateTime.now()
                    .withMonth(oldReminderDate.monthValue)
                    .withDayOfMonth(oldReminderDate.dayOfMonth)
                    .withHour(oldReminderDate.hour)
                    .withMinute(oldReminderDate.minute)

                if (updatedReminderDate.isBefore(LocalDateTime.now())) {
                    updatedReminderDate.plusYears(1)
                } else {
                    updatedReminderDate
                }
            }
        }
        return newReminderDate
    }

    private fun getNotificationDateTime(notification: NotificationData): LocalDateTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(notification.trigger),
            OffsetDateTime.now().offset
        )
    }

    fun createNotificationChannel(channel: NotificationChannels) {
        val createdChannel = NotificationChannel(channel.channelId, channel.channelName, channel.importance)
        notificationManager.createNotificationChannel(createdChannel)
    }
}

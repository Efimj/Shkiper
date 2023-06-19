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
import java.util.*

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
        when (notificationData.repeatMode) {
            RepeatMode.DAILY -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP, notificationData.trigger, AlarmManager.INTERVAL_DAY, pendingIntent
                )
            }

            RepeatMode.WEEKLY -> {
                val millsInWeek: Long = 24 * 60 * 60 * 1000 * 7
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationData.trigger, millsInWeek, pendingIntent)
            }

            else -> alarmManager.set(AlarmManager.RTC_WAKEUP, notificationData.trigger, pendingIntent)
        }
    }

    fun cancelScheduledNotification(requestCode: Int) {
        //????
//        val notificationIntent = Intent(context, NotificationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            requestCode,
//            notificationIntent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.cancel(pendingIntent)
    }

    fun restoreNotifications() {
        val now = LocalDateTime.now()

        notificationStorage.getAll().forEach { notification ->
            val oldReminderDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(notification.trigger),
                OffsetDateTime.now().offset
            )

            val newReminderDate = when (notification.repeatMode) {
                RepeatMode.NONE -> {
                    if (oldReminderDate.isBefore(now)) {
                        notificationStorage.remove(notification)
                        oldReminderDate
                    } else {
                        oldReminderDate
                    }
                }

                RepeatMode.DAILY -> {
                    val updatedReminderDate = oldReminderDate.withHour(now.hour)
                        .withMinute(now.minute)

                    if (updatedReminderDate.isBefore(now)) {
                        updatedReminderDate.plusDays(1)
                    } else {
                        updatedReminderDate
                    }
                }

                RepeatMode.WEEKLY -> {
                    val updatedReminderDate = oldReminderDate.with(DayOfWeek.from(now.dayOfWeek))
                        .withHour(now.hour)
                        .withMinute(now.minute)

                    if (updatedReminderDate.isBefore(now)) {
                        updatedReminderDate.plusDays(7)
                    } else {
                        updatedReminderDate
                    }
                }

                RepeatMode.MONTHLY -> {
                    val updatedReminderDate = oldReminderDate.withDayOfMonth(now.dayOfMonth)
                        .withHour(now.hour)
                        .withMinute(now.minute)

                    if (updatedReminderDate.isBefore(now)) {
                        updatedReminderDate.plusMonths(1)
                    } else {
                        updatedReminderDate
                    }
                }

                RepeatMode.YEARLY -> {
                    val updatedReminderDate = oldReminderDate.withMonth(now.monthValue)
                        .withDayOfMonth(now.dayOfMonth)
                        .withHour(now.hour)
                        .withMinute(now.minute)

                    if (updatedReminderDate.isBefore(now)) {
                        updatedReminderDate.plusYears(1)
                    } else {
                        updatedReminderDate
                    }
                }
            }

            val milliseconds = newReminderDate.toInstant(OffsetDateTime.now().offset).toEpochMilli()
            val newNotification = notification.copy(trigger = milliseconds)
            scheduleNotification(newNotification)
        }
    }

    fun createNotificationChannel(channel: NotificationChannels) {
        val createdChannel = NotificationChannel(channel.channelId, channel.channelName, channel.importance)
        notificationManager.createNotificationChannel(createdChannel)
    }
}

package com.android.notepad.services.notification_service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import com.android.notepad.R
import com.android.notepad.database.models.RepeatMode
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime

class NotificationScheduler(private val context: Context) {
    companion object {
        enum class NotificationChannels(val channelId: String, @StringRes val channelName: Int, val importance: Int) {
            NOTECHANNEL(
                "NOTECHANNEL", R.string.Reminders, NotificationManager.IMPORTANCE_HIGH
            )
        }
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationStorage: NotificationStorage = NotificationStorage(context)

    fun scheduleNotification(notificationData: NotificationData) {
        notificationStorage.addOrUpdate(notificationData)
        setNotification(notificationData.requestCode, notificationData.trigger)
    }

    fun updateNotificationTime(requestId: Int, date: LocalDate, time: LocalTime, repeatMode: RepeatMode) {
        val localDateTime = LocalDateTime.of(date, time)
        val trigger = localDateTime.toInstant(OffsetDateTime.now().offset).toEpochMilli()
        notificationStorage.updateNotificationTime(requestId, trigger, repeatMode)
        setNotification(requestId, trigger)
    }

    fun updateNotificationData(noteId: String, title: String, message: String) {
        notificationStorage.updateNotificationData(noteId, title, message)
    }

    private fun setNotification(requestId: Int, trigger: Long) {
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        notificationIntent.putExtra("requestCode", requestId)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestId,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, pendingIntent)
    }

    fun deleteNotification(notification: NotificationData) {
        notificationStorage.remove(notification)
        cancelNotification(notification)
    }

    fun deleteNotification(requestCode: Int) {
        notificationStorage.remove(requestCode)
        cancelNotification(requestCode)
    }

    fun deleteNotification(noteId: String) {
        val requestCode = notificationStorage.remove(noteId) ?: return
        cancelNotification(requestCode)
    }

    fun cancelNotification(requestCode: Int) {
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

    fun createNotificationChannel(channel: NotificationChannels, context: Context) {
        val createdChannel =
            NotificationChannel(channel.channelId, context.getString(channel.channelName), channel.importance)
        notificationManager.createNotificationChannel(createdChannel)
    }
}

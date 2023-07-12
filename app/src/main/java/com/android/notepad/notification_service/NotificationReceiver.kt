package com.android.notepad.notification_service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import com.android.notepad.SharedPreferencesKeys
import com.android.notepad.activity.MainActivity
import com.android.notepad.app_handlers.ThemePreferenceManager
import com.android.notepad.database.models.RepeatMode
import com.android.notepad.util.ThemeUtil
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

    @OptIn(ExperimentalAnimationApi::class)
    private fun sendNotification(intent: Intent, context: Context) {
        val requestCode = intent.getIntExtra("requestCode", 0)
        val notification = NotificationStorage(context).getNotification(requestCode) ?: return
        val savedColors = ThemeUtil.getCurrentColors(ThemePreferenceManager(context).getSavedUserTheme())

        // Create an Intent for the activity you want to start
        val mainIntent = Intent(context, MainActivity::class.java)
            .putExtra(SharedPreferencesKeys.NoteIdExtra, notification.noteId)

        // Create the TaskStackBuilder
        val mainPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(mainIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(
                notification.requestCode,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notificationBuilder =
            NotificationCompat.Builder(context, notification.channel.channelId)
                .setSmallIcon(notification.icon)
                .setAutoCancel(true)
                .setColor(savedColors.active.toArgb())
                .setColorized(true)
                .setContentIntent(mainPendingIntent)
        if (notification.title.isNotEmpty())
            notificationBuilder.setContentTitle(notification.title)
        if (notification.message.isNotEmpty())
            notificationBuilder.setContentText(notification.message)
                .setStyle(
                    NotificationCompat
                        .BigTextStyle()
                        .bigText(notification.message)
                )

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notification.notificationId, notificationBuilder.build())

        scheduleRepeatableNotification(notification, context)
    }

    private fun scheduleRepeatableNotification(
        notification: NotificationData,
        context: Context
    ) {
        var newNotificationDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(notification.trigger),
            OffsetDateTime.now().offset
        )
        val notificationScheduler = NotificationScheduler(context)
        newNotificationDate = when (notification.repeatMode) {
            RepeatMode.NONE -> {
                notificationScheduler.deleteNotification(notification.requestCode)
                return
            }

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
        }

        val milliseconds = newNotificationDate.toInstant(OffsetDateTime.now().offset).toEpochMilli()
        notificationScheduler.scheduleNotification(
            notification.copy(trigger = milliseconds)
        )
    }
}
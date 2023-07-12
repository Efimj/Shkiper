package com.android.notepad.notification_service

import com.android.notepad.database.models.RepeatMode

data class NotificationData(
    val noteId: String,
    val notificationId: Int,
    val title: String,
    val message: String,
    val icon: Int,
    val repeatMode: RepeatMode,
    val requestCode: Int,
    val trigger: Long,
    val channel: NotificationScheduler.Companion.NotificationChannels = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL
)

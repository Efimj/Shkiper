package com.example.notepadapp.notification_service

import com.example.notepadapp.database.models.RepeatMode

data class NotificationData(
    val notificationId: Int,
    val title: String,
    val message: String,
    val icon: Int,
    val repeatMode: RepeatMode,
    val requestCode: Int,
    val trigger: Long,
    val channel: NotificationScheduler.Companion.NotificationChannels = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL
)

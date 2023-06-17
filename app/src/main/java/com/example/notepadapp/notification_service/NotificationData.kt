package com.example.notepadapp.notification_service

import com.example.notepadapp.database.models.RepeatMode

data class NotificationData(
    val id: Int,
    val repeatMode: RepeatMode,
    val title: String,
    val message: String,
    val icon: Int
)
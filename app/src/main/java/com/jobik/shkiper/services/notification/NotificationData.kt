package com.jobik.shkiper.services.notification

import androidx.annotation.Keep
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.NotificationColor
import com.jobik.shkiper.database.models.NotificationIcon
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.database.models.RepeatMode
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class NotificationData(
    val noteId: String,
    val notificationId: Int,
    val title: String,
    val message: String,
    val repeatMode: RepeatMode,
    val requestCode: Int,
    val trigger: Long,
    val icon: NotificationIcon = NotificationIcon.EVENT,
    val color: NotificationColor = NotificationColor.MATERIAL,
    val channel: NotificationScheduler.Companion.NotificationChannels = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL
)

fun createNotificationData(note: Note, reminder: Reminder, trigger: Long): NotificationData {
    return NotificationData(
        noteId = note._id.toHexString(),
        notificationId = reminder._id.timestamp,
        title = note.header,
        message = note.body,
        repeatMode = reminder.repeat,
        requestCode = reminder._id.timestamp,
        icon = reminder.icon,
        color = reminder.color,
        trigger = trigger
    )
}
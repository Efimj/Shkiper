package com.example.notepadapp.notification_service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.notepadapp.R
import com.example.notepadapp.database.models.RepeatMode
import java.util.*

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        sendNotification(intent, context)
    }

    private fun sendNotification(intent: Intent, context: Context) {
        val channelId = intent.getStringExtra("channelId") ?: ""
        val id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title") ?: ""
        val message = intent.getStringExtra("message") ?: ""
        val icon = intent.getIntExtra("icon", R.drawable.first)
        val repeatModeString = intent.getStringExtra("repeatMode") ?: RepeatMode.NONE.name
        val repeatMode = RepeatMode.valueOf(repeatModeString)
        val requestCode = intent.getIntExtra("requestCode", 0)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(icon)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notificationBuilder.build())

        if (repeatMode != RepeatMode.NONE) {
            // Create a new memory for the next time
            val calendar = Calendar.getInstance()
            when (repeatMode) {
                RepeatMode.MONTHLY -> {
                    calendar.add(Calendar.MONTH, 1)
                }
                RepeatMode.YEARLY -> {
                    calendar.add(Calendar.YEAR, 1)
                }
                else-> return
            }
            NotificationScheduler(context).scheduleNotification(
                NotificationData(id, repeatMode, title, message, icon),
                requestCode,
                calendar.timeInMillis
            )
        }
    }
}
package com.example.notepadapp.notification_service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.notepadapp.R

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

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(icon)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notificationBuilder.build())
    }
}
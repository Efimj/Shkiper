package com.jobik.shkiper.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.provider.Settings.*
import androidx.annotation.Keep
import androidx.compose.animation.ExperimentalAnimationApi
import com.jobik.shkiper.R
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.activity.MainActivity
import com.jobik.shkiper.services.notification_service.NotificationScheduler

@Keep
class IntentHelper {
    fun sendMailIntent(context: Context, mailList: List<String>, header: String, text: String = "") {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, mailList.toTypedArray())
        intent.putExtra(Intent.EXTRA_SUBJECT, header)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.setType("message/rfc822")
        intent.setPackage("com.google.android.gm")
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            intent.setPackage(null)
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.ChooseEmailClient)))
        }
    }

    fun shareTextIntent(context: Context, sharedText: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, sharedText)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val chooser = Intent.createChooser(intent, "Share")
        context.startActivity(chooser)
    }

    fun openBrowserIntent(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        context.startActivity(intent)
    }

    fun StartActivityAndOpenNote(
        context: Context,
        noteId: String
    ) {
        context.startActivity(getStartActivityAndOpenNoteIntent(context, noteId))
    }

    @OptIn(ExperimentalAnimationApi::class)
    fun getStartActivityAndOpenNoteIntent(context: Context, noteId: String): Intent {
        // Create an Intent for the activity you want to start
        val mainIntent = Intent(context, MainActivity::class.java)
            .putExtra(SharedPreferencesKeys.NoteIdExtra, noteId)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return mainIntent
    }

    fun startIntentAppNotificationSettings(context: Context, channelId: String) {
        if (areNotificationsEnabled(context = context)) {
            val notificationChannelIntent = Intent(ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                .putExtra(EXTRA_APP_PACKAGE, context.packageName)
                .putExtra(EXTRA_CHANNEL_ID, channelId)
            try {
                context.startActivity(notificationChannelIntent)
            } catch (e: Exception) {
                runCatching {
                    val notificationIntent: Intent = Intent(ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(EXTRA_APP_PACKAGE, context.packageName)
                    context.startActivity(notificationIntent)
                }
            }
            return
        }
        runCatching {
            val notificationIntent: Intent = Intent(ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(EXTRA_APP_PACKAGE, context.packageName)
            context.startActivity(notificationIntent)
        }
    }
}


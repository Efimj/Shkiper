package com.jobik.shkiper.helpers

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings.*
import androidx.annotation.Keep
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.jobik.shkiper.BuildConfig
import com.jobik.shkiper.R
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.activity.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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

    fun startIntentAppEXACTNotificationSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
            if (alarmManager?.canScheduleExactAlarms() == false) {
                runCatching {
                    Intent().also { intent ->
                        intent.action = ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    fun shareImageToOthers(
        context: Context,
        text: String?,
        bitmap: ImageBitmap,
        format: CompressFormat = CompressFormat.JPEG
    ) {
        shareImageToOthers(context, text, bitmap.asAndroidBitmap(), format)
    }

    fun shareImageToOthers(
        context: Context,
        text: String?,
        bitmap: Bitmap,
        format: CompressFormat = CompressFormat.JPEG
    ) {
        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val stream = FileOutputStream("$cachePath/image.${format.name}")
            bitmap.compress(format, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val imagePath = File(context.getCacheDir(), "images")
        val newFile = File(imagePath, "image.${format.name}")
        val contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".file_provider", newFile)

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.setAction(Intent.ACTION_SEND)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.setType("image/${format.name}")
            context.startActivity(Intent.createChooser(shareIntent, "Choose an app"))
        }
    }
}


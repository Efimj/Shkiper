package com.android.notepad.helpers

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import com.android.notepad.BuildConfig


fun openAppRating(context: Context) {
    // you can also use BuildConfig.APPLICATION_ID
    val appId = context.packageName
    val rateIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("market://details?id=$appId")
    )
    var marketFound = false

    // find all applications able to handle our rateIntent
    val otherApps = context.packageManager
        .queryIntentActivities(rateIntent, 0)
    for (otherApp in otherApps) {
        // look for Google Play application
        if (otherApp.activityInfo.applicationInfo.packageName
            == "com.android.vending"
        ) {
            val otherAppActivity = otherApp.activityInfo
            val componentName = ComponentName(
                otherAppActivity.applicationInfo.packageName,
                otherAppActivity.name
            )
            // make sure it does NOT open in the stack of your activity
            rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // task reparenting if needed
            rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            // if the Google Play was already open in a search result
            //  this make sure it still go to the app page you requested
            rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // this make sure only the Google Play app is allowed to
            // intercept the intent
            rateIntent.setComponent(componentName)
            context.startActivity(rateIntent)
            marketFound = true
            break
        }
    }

    // if GP not present on device, open web browser
    if (!marketFound) {
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$appId")
        )
        context.startActivity(webIntent)
    }
}

fun openRateScreen(context: Context) {
    openAppRating(context)
//    val packageName = BuildConfig.APPLICATION_ID
//    val uri: Uri = try {
//        Uri.parse("market://details?id=$packageName")
//    } catch (e: ActivityNotFoundException) {
//        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
//    }
//    val intent = Intent(Intent.ACTION_VIEW, uri)
//    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//    Log.i("dsa",uri.toString())
//    ContextCompat.startActivity(
//        context,
//        intent,
//        null
//    )
}
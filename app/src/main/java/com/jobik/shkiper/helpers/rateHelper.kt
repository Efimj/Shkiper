package com.jobik.shkiper.helpers

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri


class RateScreenHelper(val context: Context){
    private val appId: String = context.packageName

    @SuppressLint("QueryPermissionsNeeded")
    private fun openAppRating() {
        // you can also use BuildConfig.APPLICATION_ID
        val uri = Uri.parse("market://details?id=$appId")

        val intent = Intent(
            Intent.ACTION_VIEW,
            uri
        )

        var marketFound = false

        // find all applications able to handle our rateIntent
        val otherApps = context.packageManager.queryIntentActivities(intent, 0)
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
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // task reparenting if needed
                intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                intent.setComponent(componentName)
                context.startActivity(intent)
                marketFound = true
                break
            }
        }

        // if GP not present on device, open web browser
        if (!marketFound)
            openBrowserForRate()
    }

    private fun openBrowserForRate() {
        val uri =  Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
        val intent = Intent(
            Intent.ACTION_VIEW,
            uri
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun openRateScreen(){
        try {
            openAppRating()
        } catch (e: Exception) {
            openBrowserForRate()
        }
    }
}
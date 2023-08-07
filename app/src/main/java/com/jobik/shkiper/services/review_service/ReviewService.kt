package com.jobik.shkiper.services.review_service

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.services.statistics_service.StatisticsService
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class ReviewService(val context: Context) {
    private val appId: String = context.packageName
    private val countOfferReviewKey = SharedPreferencesKeys.CountOfferReview
    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKeys.ApplicationStorageName, Context.MODE_PRIVATE)

    fun needShowOfferReview(): Boolean {
        val currentOpenAppDate = StatisticsService(context).appStatistics.statisticsData.firstOpenDate.value
        val countReviewOffer = sharedPreferences.getInt(countOfferReviewKey, 0)

        if (currentOpenAppDate == null) return false

        val daysSinceFirstOpen = ChronoUnit.DAYS.between(currentOpenAppDate, LocalDate.now())

        val result = when (countReviewOffer) {
            0 -> daysSinceFirstOpen >= 5
            1 -> daysSinceFirstOpen >= 55
            2 -> daysSinceFirstOpen >= 145
            else -> false
        }
        if (result) incrementCountOfferReview()

        return result
    }

    fun incrementCountOfferReview(count: Int = 1) {
        val countReviewOffer = sharedPreferences.getInt(countOfferReviewKey, 0)
        val editor = sharedPreferences.edit()
        editor.putInt(countOfferReviewKey, countReviewOffer + count)
        editor.apply()
    }

    fun openRateScreen() {
        try {
            openAppRating()
        } catch (e: Exception) {
            openBrowserForRate()
        }
    }

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
        val uri = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
        val intent = Intent(
            Intent.ACTION_VIEW,
            uri
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
package com.jobik.shkiper.services.review

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.Keep
import com.jobik.shkiper.SharedPreferencesKeys
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private const val CountDaysToShowReviewOffer = 12

class ReviewService(val context: Context) {
    private val appId: String = context.packageName
    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKeys.ApplicationStorageName, Context.MODE_PRIVATE)

    fun needShowOfferReview(): Boolean {
        val lastOfferDate = getLastOfferDate()
        val daysSinceFirstOpen = ChronoUnit.DAYS.between(lastOfferDate, LocalDate.now())
        val needShowDialogue = daysSinceFirstOpen > CountDaysToShowReviewOffer
        if (needShowDialogue) {
            incrementCountOfferReview()
            updateLastOfferReviewDate()
        }
        return needShowDialogue
    }

    private fun getLastOfferDate(): LocalDate {
        val lastOfferDate =
            sharedPreferences.getString(SharedPreferencesKeys.LastDateReviewOffer, "")
        if(lastOfferDate.isNullOrBlank()){
            updateLastOfferReviewDate()
        }
        return try {
            LocalDate.parse(lastOfferDate)
        } catch (e: Exception) {
            LocalDate.now()
        }

    }

    private fun incrementCountOfferReview(count: Int = 1) {
        val countReviewOffer = sharedPreferences.getInt(SharedPreferencesKeys.CountOfferReview, 0)
        val editor = sharedPreferences.edit()
        editor.putInt(SharedPreferencesKeys.CountOfferReview, countReviewOffer + count)
        editor.apply()
    }

    private fun updateLastOfferReviewDate(localDate: LocalDate = LocalDate.now()) {
        val editor = sharedPreferences.edit()
        editor.putString(SharedPreferencesKeys.LastDateReviewOffer, localDate.toString())
        editor.apply()
    }

    fun openRateScreen() {
        try {
            openAppRating()
        } catch (e: Exception) {
            openBrowserForRate()
        }
    }

    @Keep
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

    @Keep
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
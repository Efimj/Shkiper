package com.jobik.shkiper.util

import android.content.Context
import androidx.annotation.Keep
import com.jobik.shkiper.SharedPreferencesKeys
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Keep
object SupportTheDeveloperBannerUtil {
    const val MonthsSpan = 3

    fun isBannerNeeded(context: Context): Boolean {
        return ChronoUnit.MONTHS.between(getLastShowingDate(context), LocalDateTime.now()) >= MonthsSpan
    }

    fun updateLastShowingDate(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.ApplicationStorageName, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(SharedPreferencesKeys.LastBannerSupportDeveloperShowingDate, LocalDateTime.now().toString())
            .apply()
    }

    private fun getLastShowingDate(context: Context): LocalDateTime {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.ApplicationStorageName, Context.MODE_PRIVATE)
        return try {
            LocalDateTime.parse(
                sharedPreferences.getString(
                    SharedPreferencesKeys.LastBannerSupportDeveloperShowingDate,
                    "noLastDate"
                )
            )
        } catch (e: Exception) {
            updateLastShowingDate(context)
            LocalDateTime.now()
        }
    }
}
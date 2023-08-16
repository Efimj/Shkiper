package com.jobik.shkiper

import androidx.annotation.Keep

@Keep
object SharedPreferencesKeys {
    // Theme preferences
    const val ThemePreferencesName = "ThemePreferencesName"
    const val AppThemeKey = "AppThemeKey"

    // Notification storage
    const val NotificationStorageName = "NotificationStorageName"
    const val NotificationListKey = "NotificationListKey"
    const val NoteIdExtra = "NoteIdExtra"

    // Application storage
    const val ApplicationStorageName = "ApplicationStorageName"
    const val IsOnboardingPageFinished = "IsOnboardingPageFinished"
    const val CountOfferReview = "CountOfferReview"
    const val Localization = "Localization"
    const val Statistics = "Statistics"
    const val LastBannerSupportDeveloperShowingDate = "LastBannerSupportDeveloperShowingDate"
}
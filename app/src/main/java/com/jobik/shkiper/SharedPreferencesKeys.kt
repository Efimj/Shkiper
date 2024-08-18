package com.jobik.shkiper

import androidx.annotation.Keep

@Keep
object SharedPreferencesKeys {
    // Notification storage
    const val NotificationStorageName = "NotificationStorageName"
    const val NotificationListKey = "NotificationListKey"
    const val NoteIdExtra = "NoteIdExtra"

    // Application storage
    const val ApplicationStorageName = "ApplicationStorageName"
    const val ApplicationSettings = "ApplicationSettings"
    const val OnboardingFinishedData = "2"
    const val OnboardingPageFinishedData = "OnboardingPageFinishedData"
    const val CountOfferReview = "CountOfferReview"
    const val LastDateReviewOffer = "LastDateReviewOffer"
    const val Statistics = "Statistics"
}
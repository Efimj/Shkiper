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
    const val ApplicationUiMode = "ApplicationUiMode"
    const val OnboardingFinishedData = "1"
    const val OnboardingPageFinishedData = "OnboardingPageFinishedData"
    const val CountOfferReview = "CountOfferReview"
    const val LastDateReviewOffer = "LastDateReviewOffer"
    const val Localization = "Localization"
    const val Statistics = "Statistics"
    const val LastBannerSupportDeveloperShowingDate = "LastBannerSupportDeveloperShowingDate"

}
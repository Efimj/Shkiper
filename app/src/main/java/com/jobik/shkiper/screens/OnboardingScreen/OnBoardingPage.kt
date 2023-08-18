package com.jobik.shkiper.screens.OnboardingScreen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jobik.shkiper.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int
) {
    object Greetings : OnBoardingPage(
        image = R.drawable.onboarding_first,
        title = R.string.app_name,
        description = R.string.AppDescription,
    )

    object Hashtags : OnBoardingPage(
        image = R.drawable.onboarding_hashtag,
        title = R.string.Tags,
        description = R.string.TagsDescription,
    )

    object Reminders : OnBoardingPage(
        image = R.drawable.onboarding_reminder,
        title = R.string.Reminders,
        description = R.string.RemindersDescription
    )

    object Themes : OnBoardingPage(
        image = R.drawable.onboarding_themes,
        title = R.string.Themes,
        description = R.string.ThemesDescription
    )

    object PageList {
        val PageList = listOf(Greetings, Hashtags, Reminders, Themes)
        val Count = PageList.size
    }
}
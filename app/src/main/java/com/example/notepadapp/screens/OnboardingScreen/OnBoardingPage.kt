package com.example.notepadapp.screens.OnboardingScreen

import androidx.annotation.DrawableRes
import com.example.notepadapp.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: Int,
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

    object PageList {
        val PageList = listOf(Greetings, Hashtags, Reminders)
        val Count = PageList.size
    }
}
package com.example.notepadapp.screens.OnboardingScreen

import androidx.annotation.DrawableRes
import com.example.notepadapp.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object Greetings : OnBoardingPage(
        image = R.drawable.ic_notification,
        title = "Greetings",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    object Hashtags : OnBoardingPage(
        image = R.drawable.onboarding_hashtag,
        title = "Tags",
        description = "Tags are necessary for quickly filtering your notes; multiple tags can be added to each note."
    )

    object Reminders : OnBoardingPage(
        image = R.drawable.onboarding_reminder,
        title = "Reminders",
        description = "Reminders allow you to schedule a notification for an important event on a specified day and time, with a chosen interval."
    )

    object PageList {
        val PageList = listOf(Greetings, Hashtags, Reminders)
        val Count = PageList.size
    }
}
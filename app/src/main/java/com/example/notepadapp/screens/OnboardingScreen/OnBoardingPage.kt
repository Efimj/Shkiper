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
        image = R.drawable.ic_notification,
        title = "Hashtags",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    object Reminders : OnBoardingPage(
        image = R.drawable.ic_notification,
        title = "Reminders",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod."
    )

    object PageList {
        val PageList = listOf(Greetings, Hashtags, Reminders)
        val Count = PageList.size
    }
}
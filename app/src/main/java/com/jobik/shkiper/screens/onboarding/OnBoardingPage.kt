package com.jobik.shkiper.screens.onboarding

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
    data object Hashtags : OnBoardingPage(
        image = R.drawable.onboarding_1,
        title = R.string.Tags,
        description = R.string.TagsDescription,
    )

    data object TextFormatting : OnBoardingPage(
        image = R.drawable.onboarding_2,
        title = R.string.TextFormatting,
        description = R.string.TextFormattingDescription,
    )

    data object Reminders : OnBoardingPage(
        image = R.drawable.onboarding_3,
        title = R.string.Reminders,
        description = R.string.RemindersDescription
    )

    data object Statistics : OnBoardingPage(
        image = R.drawable.onboarding_4,
        title = R.string.StatisticsPage,
        description = R.string.StatisticsPageDescription
    )

    data object Widgets : OnBoardingPage(
        image = R.drawable.onboarding_5,
        title = R.string.Widgets,
        description = R.string.WidgetsDescription
    )

    data object MadeWithLove : OnBoardingPage(
        image = R.drawable.onboarding_6,
        title = R.string.MadeWithLove,
        description = R.string.MadeWithLoveDescription
    )

    object PageList {
        val PageList = listOf(Hashtags, TextFormatting, Reminders, Statistics, Widgets, MadeWithLove)
        val Count = PageList.size
    }
}
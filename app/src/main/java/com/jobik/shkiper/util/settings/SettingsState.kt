package com.jobik.shkiper.util.settings

import androidx.annotation.Keep
import com.jobik.shkiper.database.models.NotificationColor
import com.jobik.shkiper.database.models.NotificationIcon
import com.jobik.shkiper.ui.theme.CustomThemeStyle
import java.time.Duration
import java.time.LocalDateTime
import java.util.Locale

@Keep
enum class NightMode {
    Light,
    Dark,
    System,
}

@Keep
data class SettingsState(
    val firstAppOpening: LocalDateTime? = null,
    val fontScale: Float = 1f,
    val checkUpdates: Boolean = true,
    val secureMode: Boolean = false,
    val nightMode: NightMode = NightMode.System,
    val theme: CustomThemeStyle = CustomThemeStyle.MaterialDynamicColors,
    val defaultNotificationIcon: NotificationIcon = NotificationIcon.EVENT,
    val defaultNotificationColor: NotificationColor = NotificationColor.MATERIAL,
    val localization: Localization = SettingsHandler.defaultLocalization(),
    val lastShowingDonateBanner: LocalDateTime = SettingsHandler.defaultLastShowingDonateBanner(),
    val lastShowingRateBanner: LocalDateTime = SettingsHandler.defaultLastShowingRateBanner(),
)

object SettingsHandler {
    const val DaysBeetwinShowingDonateBanner = 12L
    const val DaysBeetwinShowingRateBanner = 16L

    fun checkIsDonateBannerNeeded(settingsState: SettingsState): Boolean {
        val duration = Duration.between(settingsState.lastShowingDonateBanner, LocalDateTime.now())
        return duration.toDays() >= DaysBeetwinShowingDonateBanner
    }

    fun checkIsRateBannerNeeded(settingsState: SettingsState): Boolean {
        val duration = Duration.between(settingsState.lastShowingRateBanner, LocalDateTime.now())
        return duration.toDays() >= DaysBeetwinShowingRateBanner
    }

    fun defaultLastShowingDonateBanner(): LocalDateTime = LocalDateTime.now()
        .minusDays(DaysBeetwinShowingDonateBanner - 1)

    fun defaultLastShowingRateBanner(): LocalDateTime = LocalDateTime.now()
        .minusDays(DaysBeetwinShowingRateBanner - 1)

    fun defaultLocalization() = Localization.entries.find {
        (Locale.getDefault().language.equals(
            Locale(it.name).language
        ))
    } ?: Localization.EN
}


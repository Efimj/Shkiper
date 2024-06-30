package com.jobik.shkiper.util.settings

import androidx.annotation.Keep
import com.jobik.shkiper.database.models.NotificationColor
import com.jobik.shkiper.database.models.NotificationIcon
import com.jobik.shkiper.ui.theme.CustomThemeStyle
import java.util.Locale

@Keep
enum class NightMode {
    Light,
    Dark,
    System,
}

@Keep
data class SettingsState(
    val fontScale: Float = 1f,
    val checkUpdates: Boolean = true,
    val secureMode: Boolean = false,
    val nightMode: NightMode = NightMode.System,
    val theme: CustomThemeStyle = CustomThemeStyle.MaterialDynamicColors,
    val defaultNotificationIcon: NotificationIcon = NotificationIcon.EVENT,
    val defaultNotificationColor: NotificationColor = NotificationColor.MATERIAL,
    val localization: Localization = defaultLocalization()
)

private fun defaultLocalization() = Localization.entries.find {
    (Locale.getDefault().language.equals(
        Locale(it.name).language
    ))
} ?: Localization.EN
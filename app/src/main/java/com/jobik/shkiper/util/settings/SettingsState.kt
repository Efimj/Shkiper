package com.jobik.shkiper.util.settings

data class SettingsState(
    val fontScale: Float = 1f,
    val checkUpdates: Boolean = true,
    val secureMode: Boolean = false
)
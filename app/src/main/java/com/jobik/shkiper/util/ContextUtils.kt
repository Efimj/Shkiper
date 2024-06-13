package com.jobik.shkiper.util

import android.content.Context

object ContextUtils {
    fun Context.adjustFontSize(
        scale: Float?
    ): Context {
        val configuration = resources.configuration
        configuration.fontScale = scale ?: resources.configuration.fontScale
        return createConfigurationContext(configuration)
    }
}
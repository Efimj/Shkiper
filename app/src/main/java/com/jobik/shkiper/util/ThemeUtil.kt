package com.jobik.shkiper.util

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import com.jobik.shkiper.ui.theme.ColorThemes
import com.jobik.shkiper.ui.theme.ExtendedColors
import com.jobik.shkiper.ui.theme.UserTheme

object ThemeUtil {
    private var _theme = UserTheme(true, ColorThemes.Default.name, ColorThemes.Default.name)
    var theme: UserTheme
        get() = _theme
        set(value) {
            _theme = value
            themeColors = getCurrentColors(_theme)
        }

    var themeColors by mutableStateOf(ColorThemes.Default.colorTheme.darkColors)
        private set

    fun changeColorTheme(context: Context, colorTheme: ColorThemes) {
        val newUserTheme = if (theme.isDarkTheme) UserTheme(theme.isDarkTheme, colorTheme.name, theme.lightThemeName)
        else UserTheme(theme.isDarkTheme, theme.darkThemeName, colorTheme.name)
        theme = newUserTheme
        saveTheme(context)
    }

    fun getCurrentColors(userTheme: UserTheme): ExtendedColors {
        return try {
            val currentThemeName = if (userTheme.isDarkTheme) userTheme.darkThemeName else userTheme.lightThemeName
            val currentTheme = ColorThemes.valueOf(currentThemeName)
            if (userTheme.isDarkTheme) currentTheme.colorTheme.darkColors else currentTheme.colorTheme.lightColors
        } catch (ex: IllegalArgumentException) {
            Log.d("ThemeUtil -> themeColors", ex.toString())
            if (theme.isDarkTheme) ColorThemes.Default.colorTheme.darkColors else ColorThemes.Default.colorTheme.lightColors
        }
    }

    fun toggleTheme(context: Context) {
        _theme = UserTheme(!_theme.isDarkTheme, _theme.darkThemeName, _theme.lightThemeName)
        saveTheme(context)
    }

    private fun saveTheme(context: Context) {
        val themePreferenceUtil = ThemePreferenceUtil(context)
        themePreferenceUtil.saveTheme(
            UserTheme(
                _theme.isDarkTheme,
                _theme.darkThemeName,
                _theme.lightThemeName
            )
        )
    }
}

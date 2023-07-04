package com.example.notepadapp.util

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.ui.theme.ColorTheme
import com.example.notepadapp.ui.theme.ColorThemes
import com.example.notepadapp.ui.theme.ExtendedColors
import com.example.notepadapp.ui.theme.UserTheme

object ThemeUtil {
    private var _theme = UserTheme(true, ColorThemes.Default.name, ColorThemes.Default.name)
    var theme: UserTheme
        get() = _theme
        set(value) {
            _theme = value
            themeColors = getCurrentColors()
        }

    var themeColors by mutableStateOf(ColorThemes.Default.colorTheme.darkColors)
        private set

    fun changeColorTheme(context: Context, colorTheme: ColorThemes) {
        val newUserTheme = if (theme.isDarkTheme) UserTheme(theme.isDarkTheme, colorTheme.name, theme.lightThemeName)
        else UserTheme(theme.isDarkTheme, theme.darkThemeName, colorTheme.name)
        theme = newUserTheme
        saveTheme(context)
    }

    private fun getCurrentColors(): ExtendedColors {
        return try {
            val currentThemeName = if (theme.isDarkTheme) theme.darkThemeName else theme.lightThemeName
            val currentTheme = ColorThemes.valueOf(currentThemeName)
            if (theme.isDarkTheme) currentTheme.colorTheme.darkColors else currentTheme.colorTheme.lightColors
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
        val themePreferenceManager = ThemePreferenceManager(context)
        themePreferenceManager.saveTheme(
            UserTheme(
                _theme.isDarkTheme,
                _theme.darkThemeName,
                _theme.lightThemeName
            )
        )
    }
}

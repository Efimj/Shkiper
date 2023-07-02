package com.example.notepadapp.util

import androidx.compose.runtime.*
import com.example.notepadapp.ui.theme.ColorTheme
import com.example.notepadapp.ui.theme.UserTheme

object ThemeUtil {
    var currentTheme by mutableStateOf(UserTheme(true, ColorTheme.DefaultColorTheme.darkColors))

    fun toggleTheme() {
        currentTheme = UserTheme(currentTheme.isDarkTheme, currentTheme.themeColors)
    }
}

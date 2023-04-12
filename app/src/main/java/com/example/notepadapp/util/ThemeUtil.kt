package com.example.notepadapp.util

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeUtil {
    var isDarkTheme by mutableStateOf(false)

    fun toggleTheme(){
        isDarkTheme = !isDarkTheme
    }
}

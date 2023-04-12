package com.example.notepadapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.notepadapp.util.ThemeUtil

class ThemeViewModel : ViewModel() {
    private var _isDarkTheme = mutableStateOf(false)

    val isDarkTheme: State<Boolean>
        get() = _isDarkTheme

//    fun toggleTheme() {
//        ThemeUtil.isDakTheme = !ThemeUtil.isDakTheme
//    }
}

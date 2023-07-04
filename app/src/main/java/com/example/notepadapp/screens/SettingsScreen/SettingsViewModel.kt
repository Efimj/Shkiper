package com.example.notepadapp.screens.SettingsScreen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.notepadapp.NotepadApplication
import com.example.notepadapp.helpers.localization.LocaleHelper
import com.example.notepadapp.helpers.localization.Localization
import com.example.notepadapp.ui.theme.ColorTheme
import com.example.notepadapp.ui.theme.ColorThemes
import com.example.notepadapp.util.ThemeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
) : ViewModel() {

    /*******************
     * App theming
     *******************/

    val themeColorList = mutableStateOf(getThemeColorList())

    fun toggleAppTheme() {
        ThemeUtil.toggleTheme(application.applicationContext)
    }

    fun selectColorTheme(themeName: String) {
        val selectedTheme = ColorThemes.values().find { it.name == themeName }?: return
        ThemeUtil.changeColorTheme(application.applicationContext, selectedTheme)
    }

    private fun getThemeColorList(): List<ColorThemes> {
        return ColorThemes.values().toList()
    }

    /*******************
     * App languages
     *******************/

    val localizationList = mutableStateOf(getLocalizationList())

    private fun getLocalizationList(): List<String> {
        return Localization.values().filter { it.name != NotepadApplication.currentLanguage.name }
            .map { it.getLocalizedValue(application.applicationContext) }
    }

    fun selectLocalization(selectedIndex: Int) {
        try {
            val newLocalization: Localization =
                Localization.values().filter { it.name != NotepadApplication.currentLanguage.name }[selectedIndex]
            LocaleHelper.setLocale(application.applicationContext, newLocalization)
            localizationList.value = getLocalizationList()
        } catch (e: Exception) {
            Log.d("ChangeLocalizationError", e.message.toString())
        }
    }
}
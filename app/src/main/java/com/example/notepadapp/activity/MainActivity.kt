package com.example.notepadapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.notepadapp.SharedPreferencesKeys
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.navigation.AppScreens
import com.example.notepadapp.ui.components.modals.MainMenuBottomSheet
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.util.ThemeUtil
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.isDarkTheme = ThemePreferenceManager(this).getSavedTheme()
        val startDestination = getStartDestination()

        setContent {
            CustomAppTheme(darkTheme = ThemeUtil.isDarkTheme) {
                Box(Modifier.fillMaxSize().background(CustomAppTheme.colors.mainBackground)) {
                    MainMenuBottomSheet(startDestination)
                }
            }
        }
    }

    private fun getStartDestination(): String {
        // Retrieve the extras from the Intent
        val extras = intent.extras
        var noteId: String? = null
        if (extras != null) {
            noteId = extras.getString(SharedPreferencesKeys.NoteIdExtra, null)
        }
        val startDestination = if (noteId != null) {
            AppScreens.Note.noteId(noteId)
        } else {
            AppScreens.NoteList.route
        }
        return startDestination
    }
}

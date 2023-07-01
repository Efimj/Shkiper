package com.example.notepadapp.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.notepadapp.NotepadApplication
import com.example.notepadapp.R
import com.example.notepadapp.SharedPreferencesKeys
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.helpers.localization.LocaleHelper
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
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.setLocale(newBase, NotepadApplication.currentLanguage)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.isDarkTheme = ThemePreferenceManager(this).getSavedTheme()
        val startDestination = getStartDestination()

        setContent {
            CustomAppTheme(darkTheme = ThemeUtil.isDarkTheme) {
                Box(
                    Modifier.fillMaxSize().background(CustomAppTheme.colors.mainBackground)
//                        .paint(
//                        painterResource(id = R.drawable.screen_style_1),
//                        contentScale = ContentScale.FillBounds
//                    )
                ) {
                    MainMenuBottomSheet(startDestination)
                }
            }
        }
    }

    private fun getStartDestination(): String {
        val route = getNotificationRoute()
        if (route != null)
            return route
        return getOnboardingRoute(applicationContext) ?: AppScreens.NoteList.route
    }

    private fun getOnboardingRoute(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.ApplicationStorageName, Context.MODE_PRIVATE)
        val isOnboardingPageFinished =
            sharedPreferences.getBoolean(SharedPreferencesKeys.IsOnboardingPageFinished, false)
        return if (isOnboardingPageFinished) null else AppScreens.Onboarding.route
    }

    private fun getNotificationRoute(): String? {
        // Retrieve the extras from the Intent
        val extras = intent.extras ?: return null
        val noteId = extras.getString(SharedPreferencesKeys.NoteIdExtra, null) ?: return null
        return AppScreens.Note.noteId(noteId)
    }
}

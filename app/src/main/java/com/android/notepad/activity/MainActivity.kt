package com.android.notepad.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.android.notepad.NotepadApplication
import com.android.notepad.SharedPreferencesKeys
import com.android.notepad.app_handlers.ThemePreferenceManager
import com.android.notepad.helpers.localization.LocaleHelper
import com.android.notepad.navigation.AppScreens
import com.android.notepad.ui.components.modals.MainMenuBottomSheet
import com.android.notepad.ui.theme.CustomAppTheme
import com.android.notepad.util.ThemeUtil
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity () {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.setLocale(newBase, NotepadApplication.currentLanguage)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.theme = ThemePreferenceManager(this).getSavedUserTheme()
        val startDestination = getStartDestination()

        setContent {
            CustomAppTheme(ThemeUtil.themeColors) {
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

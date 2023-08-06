package com.jobik.shkiper.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jobik.shkiper.NotepadApplication
import com.jobik.shkiper.R
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.app_handlers.ThemePreferenceManager
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.services.localization.LocaleHelper
import com.jobik.shkiper.navigation.AppScreens
import com.jobik.shkiper.services.in_app_updates_service.InAppUpdatesService
import com.jobik.shkiper.services.review_service.ReviewService
import com.jobik.shkiper.services.statistics_service.StatisticsService
import com.jobik.shkiper.ui.components.modals.MainMenuBottomSheet
import com.jobik.shkiper.ui.components.modals.OfferWriteReview
import com.jobik.shkiper.ui.theme.CustomAppTheme
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.jobik.shkiper.util.ThemeUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.setLocale(newBase, NotepadApplication.currentLanguage)
        )
    }

    private lateinit var inAppUpdatesService: InAppUpdatesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.theme = ThemePreferenceManager(this).getSavedUserTheme()
        val startDestination = getStartDestination()
        val canShowOfferReview = mutableStateOf(ReviewService(applicationContext).needShowOfferReview())
        checkForUpdates()

        setContent {
            CustomAppTheme(ThemeUtil.themeColors) {
                Box(
                    Modifier.fillMaxSize().background(CustomAppTheme.colors.mainBackground)
                ) {
                    MainMenuBottomSheet(startDestination)
                }
                if (canShowOfferReview.value)
                    OfferWriteReview { canShowOfferReview.value = false }
            }
        }
    }

    private fun checkForUpdates() {
        inAppUpdatesService = InAppUpdatesService(this)
        inAppUpdatesService.checkForUpdate()
    }

    override fun onResume() {
        super.onResume()
        inAppUpdatesService.checkForUpdate()
    }

    private fun getStartDestination(): String {
        val route = getNotificationRoute()
        if (route != null)
            return route
        return getOnboardingRoute(applicationContext) ?: AppScreens.NoteList.notePosition(NotePosition.MAIN.name)
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

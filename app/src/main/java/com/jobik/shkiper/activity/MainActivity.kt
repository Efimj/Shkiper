package com.jobik.shkiper.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jobik.shkiper.NotepadApplication
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.SharedPreferencesKeys.OnboardingFinishedData
import com.jobik.shkiper.crash.CrashActivity
import com.jobik.shkiper.crash.GlobalExceptionHandler
import com.jobik.shkiper.navigation.Screen
import com.jobik.shkiper.screens.layout.AppLayout
import com.jobik.shkiper.services.billing.BillingService
import com.jobik.shkiper.services.inAppUpdates.InAppUpdatesService
import com.jobik.shkiper.services.statistics.StatisticsService
import com.jobik.shkiper.ui.components.modals.OfferWriteReview
import com.jobik.shkiper.ui.components.modals.onboarding.OnboardingDialog
import com.jobik.shkiper.ui.helpers.SecureModeManager
import com.jobik.shkiper.ui.theme.ShkiperTheme
import com.jobik.shkiper.util.ContextUtils
import com.jobik.shkiper.util.ContextUtils.adjustFontSize
import com.jobik.shkiper.util.settings.NightMode
import com.jobik.shkiper.util.settings.SettingsHandler
import com.jobik.shkiper.util.settings.SettingsManager
import com.jobik.shkiper.util.settings.SettingsManager.settings
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@OptIn(ExperimentalAnimationApi::class)
class StartupActivity : MainActivity()

@ExperimentalAnimationApi
@AndroidEntryPoint
open class MainActivity : ComponentActivity() {
    private lateinit var billingClientLifecycle: BillingService
    private lateinit var inAppUpdatesService: InAppUpdatesService

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            ContextUtils.setLocale(
                context = newBase,
                language = SettingsManager.settings.localization
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        adjustFontSize(SettingsManager.settings.fontScale)
        GlobalExceptionHandler.initialize(
            applicationContext = applicationContext,
            activityToBeLaunched = CrashActivity::class.java,
        )

        // Billing APIs are all handled in the this lifecycle observer.
        billingClientLifecycle = (application as NotepadApplication).billingClientLifecycle
        lifecycle.addObserver(billingClientLifecycle)
        inAppUpdatesService = InAppUpdatesService(this)

        val startDestination = getStartDestination()
        var canShowOfferReview by mutableStateOf(SettingsHandler.checkIsRateBannerNeeded(settings))

        checkForUpdates()

        setContent {
            SecureModeManager()
            UpdateStatistics()

            ShkiperTheme(
                darkTheme = when (SettingsManager.settings.nightMode) {
                    NightMode.Light -> false
                    NightMode.Dark -> true
                    else -> isSystemInDarkTheme()
                },
                style = SettingsManager.settings.theme
            ) {
                OnboardingProvider()
                AppLayout(startDestination)
                if (canShowOfferReview) {
                    OfferWriteReview { canShowOfferReview = false }
                }
            }
        }
    }

    @Composable
    private fun OnboardingProvider() {
        val onboarding = rememberSaveable { mutableStateOf(checkIsOnboarding(this)) }

        val context = LocalContext.current
        OnboardingDialog(
            isVisible = onboarding.value,
            onFinish = {
                onboarding.value = false;
                try {
                    val sharedPreferences =
                        context.getSharedPreferences(
                            SharedPreferencesKeys.ApplicationStorageName,
                            MODE_PRIVATE
                        )
                    sharedPreferences.edit()
                        .putString(
                            SharedPreferencesKeys.OnboardingPageFinishedData,
                            OnboardingFinishedData
                        )
                        .apply()
                } catch (e: Exception) {
                    Log.i("onboarding - onFinished", e.toString())
                }
            })
    }

    @Composable
    private fun UpdateStatistics() {
        val context = LocalContext.current
        val isUpdated = rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (isUpdated.value) return@LaunchedEffect
            isUpdated.value = true
            val statisticsService = StatisticsService(context)

            statisticsService.appStatistics.apply {
                fistOpenDate.increment()
                openAppCount.increment()
                if (LocalDate.now().isBefore(LocalDate.of(2024, 1, 1))) {
                    isPioneer.increment()
                }
            }
            statisticsService.saveStatistics()
        }
    }

    private val updateActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            // Handle the result of the update flow here.
            if (result.resultCode != Activity.RESULT_OK) {
                // If the update flow fails, you can retry it or notify the user.
                // Note: In a flexible update, the user has the option to postpone the update.
            }
        }

    private fun checkForUpdates() {
        val settings = SettingsManager.settings
        if (settings.checkUpdates.not()) return

        if (InAppUpdatesService.isUpdatedChecked) return
        // Initialize the updateActivityResultLauncher.
        inAppUpdatesService.checkForUpdate(updateActivityResultLauncher)
    }

    override fun onResume() {
        super.onResume()
        billingClientLifecycle.queryProductPurchases()
        inAppUpdatesService.checkForDownloadedUpdate()
    }

    private fun getStartDestination(): Screen {
        val route = getNotificationRoute()
        if (route != null)
            return route
        return Screen.NoteList
    }

    private fun checkIsOnboarding(context: Context): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(
                SharedPreferencesKeys.ApplicationStorageName,
                Context.MODE_PRIVATE
            )
        val isOnboardingPageFinished =
            sharedPreferences.getString(SharedPreferencesKeys.OnboardingPageFinishedData, "")
        return isOnboardingPageFinished != OnboardingFinishedData
    }

    private fun getNotificationRoute(): Screen? {
        // Retrieve the extras from the Intent
        val extras = intent.extras ?: return null
        val noteId = extras.getString(SharedPreferencesKeys.NoteIdExtra, null) ?: return null
        return Screen.Note(id = noteId, sharedElementOrigin = Screen.NoteList.name)
    }
}
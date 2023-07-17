package com.android.notepad.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.notepad.NotepadApplication
import com.android.notepad.helpers.localization.LocaleHelper
import com.android.notepad.screens.NoteListScreen.NotesViewModel
import com.android.notepad.services.statistics_service.StatisticsService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("CustomSplashScreen")
@ExperimentalAnimationApi
@AndroidEntryPoint
@OptIn(DelicateCoroutinesApi::class)
class SplashActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.setLocale(newBase, NotepadApplication.currentLanguage)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        val context = this.applicationContext
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }

        // Initialization Realm database
        setContent {
            val noteViewModel: NotesViewModel = hiltViewModel()
        }

        GlobalScope.launch {
            val statisticsService = StatisticsService(context)
            statisticsService.appStatistics.apply {
                openAppCount.increment()
                if (LocalDate.now().isBefore(LocalDate.of(2024, 1, 1))) {
                    isPioneer.increment()
                }
            }
            statisticsService.saveStatistics()
        }

        GlobalScope.launch {
            delay(250)
            moveNext()
        }
    }

    private fun moveNext() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

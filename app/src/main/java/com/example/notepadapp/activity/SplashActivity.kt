package com.example.notepadapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.util.ThemeUtil
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
@OptIn(DelicateCoroutinesApi::class)
class SplashActivity  : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }

        GlobalScope.launch {
            delay(350)
            moveNext()
        }
    }

    private fun moveNext() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

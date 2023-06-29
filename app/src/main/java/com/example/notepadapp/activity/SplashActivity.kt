package com.example.notepadapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.screens.NoteListScreen.NotesViewModel
import com.example.notepadapp.screens.NoteScreen.NoteViewModel
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
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }

        // Initialization Realm database
        setContent {
            val noteViewModel: NotesViewModel = hiltViewModel()
        }

        GlobalScope.launch {
            delay(300)
            moveNext()
        }
    }

    private fun moveNext() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

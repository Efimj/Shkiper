package com.example.notepadapp.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.navigation.Screen
import com.example.notepadapp.navigation.SetupNavGraph
import com.example.notepadapp.ui.theme.CustomAppTheme
import com.example.notepadapp.util.ThemeUtil
import com.example.notepadapp.viewmodel.ThemeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomAppTheme(darkTheme = ThemeUtil.isDarkTheme) {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, startDestination = Screen.AppSettings.route)
            }
        }
    }
}

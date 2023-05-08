package com.example.notepadapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.compose.rememberNavController
import com.example.notepadapp.app_handlers.ThemePreferenceManager
import com.example.notepadapp.navigation.AppScreen
import com.example.notepadapp.navigation.SetupAppNavGraph
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
        setContent {
            CustomAppTheme(darkTheme = ThemeUtil.isDarkTheme) {
                val navController = rememberNavController()
                SetupAppNavGraph(navController = navController, startDestination = AppScreen.Home.route)
            }
        }
    }
}

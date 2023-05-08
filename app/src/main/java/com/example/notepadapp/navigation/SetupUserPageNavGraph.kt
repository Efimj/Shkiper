package com.example.notepadapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notepadapp.screen.SettingsScreen
import com.example.notepadapp.screen.WelcomeScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun SetupUserPageNavGraph(
    navController: NavHostController,
    startDestination: String
)  {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = UserPage.Settings.route) {
            SettingsScreen()
        }
    }
}

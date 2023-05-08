package com.example.notepadapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notepadapp.screen.HomeScreen
import com.example.notepadapp.screen.SettingsScreen
import com.example.notepadapp.screen.WelcomeScreen
import com.example.notepadapp.ui.components.MainPageWithBottomSheetMenu
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun SetupAppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppScreen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(route = AppScreen.Home.route) {
            HomeScreen()
        }
        composable(route = AppScreen.AppSettings.route) {
            MainPageWithBottomSheetMenu()
        }
    }
}


package com.example.notepadapp.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.notepadapp.page.NotesPage
import com.example.notepadapp.page.SettingsPage
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


@ExperimentalAnimationApi
@Composable
fun SetupHomePageNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            UserPage.Notes.route,
            enterTransition = {
                when (initialState.destination.route) {
                    UserPage.Settings.route ->
                        slideInVertically(initialOffsetY = { -40 }) + fadeIn()
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    UserPage.Settings.route ->
                        slideOutVertically(targetOffsetY = {50}) + fadeOut()
                    else -> null
                }
            }
        ) {
            NotesPage()
        }
        composable(
            route = UserPage.Settings.route,
            enterTransition = {
                when (initialState.destination.route) {
                    UserPage.Notes.route ->
                        slideInVertically(initialOffsetY = { -40 }) + fadeIn()
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    UserPage.Notes.route ->
                        slideOutVertically(targetOffsetY = {50}) + fadeOut()
                    else -> null
                }
            }
        ) {
            SettingsPage()
        }
    }
}

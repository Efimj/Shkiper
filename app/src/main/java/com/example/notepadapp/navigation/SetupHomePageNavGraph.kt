package com.example.notepadapp.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.notepadapp.page.NoteListPage
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
            route = UserPage.NoteList.route,
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
            NoteListPage(navController)
        }

        composable(
            route = UserPage.Archive.route,
            enterTransition = {
                when (initialState.destination.route) {
                    UserPage.NoteList.route ->
                        slideInVertically(initialOffsetY = { -40 }) + fadeIn()
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    UserPage.NoteList.route ->
                        slideOutVertically(targetOffsetY = {50}) + fadeOut()
                    else -> null
                }
            }
        ) {

        }

        composable(
            route = UserPage.Basket.route,
            enterTransition = {
                when (initialState.destination.route) {
                    UserPage.NoteList.route ->
                        slideInVertically(initialOffsetY = { -40 }) + fadeIn()
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    UserPage.NoteList.route ->
                        slideOutVertically(targetOffsetY = {50}) + fadeOut()
                    else -> null
                }
            }
        ) {

        }

        composable(
            route = UserPage.Settings.route,
            enterTransition = {
                when (initialState.destination.route) {
                    UserPage.NoteList.route ->
                        slideInVertically(initialOffsetY = { -40 }) + fadeIn()
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    UserPage.NoteList.route ->
                        slideOutVertically(targetOffsetY = {50}) + fadeOut()
                    else -> null
                }
            }
        ) {
            SettingsPage()
        }

        composable(
            route = UserPage.Note.route,
            arguments = listOf(navArgument(ARGUMENT_NOTE_ID){
                type = NavType.StringType
            }),
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

        }
    }
}

package com.example.notepadapp.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.notepadapp.page.NoteListPage.NoteListPage
import com.example.notepadapp.page.NotePage.NotePage
import com.example.notepadapp.page.SettingsPage.SettingsPage
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
            route = UserPages.NoteList.route,
            enterTransition = {
                when (initialState.destination.route) {
                    UserPages.Note.route -> null
                    else -> slideInVertically(initialOffsetY = { -40 }) + fadeIn()
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    UserPages.Note.route -> null
                    else -> slideOutVertically(targetOffsetY = {50}) + fadeOut()
                }
            }
        ) {
            NoteListPage(navController)
        }

        composable(
            route = UserPages.Archive.route,
            enterTransition = {
                when (initialState.destination.route) {
                    UserPages.Note.route -> null
                    else -> slideInVertically(initialOffsetY = { -40 }) + fadeIn()
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    UserPages.Note.route -> null
                    else -> slideOutVertically(targetOffsetY = {50}) + fadeOut()
                }
            }
        ) {

        }

        composable(
            route = UserPages.Basket.route,
            enterTransition = {
                when (initialState.destination.route) {
                    UserPages.Note.route -> null
                    else -> slideInVertically(initialOffsetY = { -40 }) + fadeIn()
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    UserPages.Note.route -> null
                    else -> slideOutVertically(targetOffsetY = {50}) + fadeOut()
                }
            }
        ) {

        }

        composable(
            route = UserPages.Settings.route,
            enterTransition = {
                when (initialState.destination.route) {
                    UserPages.Note.route -> null
                    else -> slideInVertically(initialOffsetY = { -40 }) + fadeIn()
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    UserPages.Note.route -> null
                    else -> slideOutVertically(targetOffsetY = {50}) + fadeOut()
                }
            }
        ) {
            SettingsPage()
        }

        composable(
            route = UserPages.Note.route,
            arguments = listOf(navArgument(ARGUMENT_NOTE_ID){
                type = NavType.StringType
            }),
            enterTransition = {
                fadeIn() + scaleIn(initialScale = 0.9f)
            },
            exitTransition = {
                fadeOut() + scaleOut(targetScale = 0.9f)
            }
        ) {
            NotePage(navController)
        }
    }
}

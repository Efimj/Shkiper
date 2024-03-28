package com.jobik.shkiper.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jobik.shkiper.screens.AboutNotepadScreen.AboutNotepadScreen
import com.jobik.shkiper.screens.ArchiveNotesScreen.ArchiveNotesScreen
import com.jobik.shkiper.screens.BasketNotesScreen.BasketNotesScreen
import com.jobik.shkiper.screens.NoteListScreen.NoteListScreen
import com.jobik.shkiper.screens.NoteScreen.NoteScreen
import com.jobik.shkiper.screens.OnboardingScreen.OnBoardingScreen
import com.jobik.shkiper.screens.PurchaseScreen.PurchaseScreen
import com.jobik.shkiper.screens.SettingsScreen.SettingsScreen
import com.jobik.shkiper.screens.StatisticsScreen.StatisticsScreen


@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalAnimationApi
@Composable
fun SetupAppScreenNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    fun isSecondaryRoute(targetRoute: String?): Boolean {
        return AppScreens.isSecondaryRoute(targetRoute ?: "")
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    ) {
        composable(
            route = AppScreens.NoteList.route,
            enterTransition = {
                when (initialState.destination.route) {
                    AppScreens.Note.route -> null
                    else -> if (isSecondaryRoute(initialState.destination.route)) null else slideInVertically(
                        initialOffsetY = { -40 }) + fadeIn()
                }
            },
            exitTransition = { null }
        ) {
            NoteListScreen(navController)
        }

        composable(
            route = AppScreens.Archive.route,
            enterTransition = {
                when (initialState.destination.route) {
                    AppScreens.Note.route -> null
                    else -> if (isSecondaryRoute(initialState.destination.route)) null else slideInVertically(
                        initialOffsetY = { -40 }) + fadeIn()
                }
            },
            exitTransition = { null }
        ) {
            ArchiveNotesScreen(navController)
        }

        composable(
            route = AppScreens.Basket.route,
            enterTransition = {
                when (initialState.destination.route) {
                    AppScreens.Note.route -> null
                    else -> if (isSecondaryRoute(initialState.destination.route)) null else slideInVertically(
                        initialOffsetY = { -40 }) + fadeIn()
                }
            },
            exitTransition = { null }
        ) {
            BasketNotesScreen(navController)
        }

        composable(
            route = AppScreens.Settings.route,
//            exitTransition = { null },
//            enterTransition = { null }
//            enterTransition = {
//                when (initialState.destination.route) {
//                    AppScreens.Note.route -> null
//                    else -> if (isSecondaryRoute(initialState.destination.route)) null else slideInVertically(
//                        initialOffsetY = { -40 }) + fadeIn()
//                }
//            },
//            exitTransition = { null }
        ) {
            SettingsScreen(navController)
        }

        composable(
            route = AppScreens.Note.route,
            arguments = listOf(navArgument(Argument_Note_Id) { type = NavType.StringType }),
            enterTransition = { fadeIn() + scaleIn(initialScale = 0.9f) },
            exitTransition = { fadeOut() + scaleOut(targetScale = 0.9f) }
        ) { NoteScreen(navController) }

        composable(
            route = AppScreens.Onboarding.route,
            enterTransition = { secondaryScreenEnterTransition() },
            exitTransition = { secondaryScreenExitTransition() }
        ) { OnBoardingScreen(navController) }

        composable(
            route = AppScreens.Statistics.route,
            enterTransition = { secondaryScreenEnterTransition() },
            exitTransition = { secondaryScreenExitTransition() }
        ) { StatisticsScreen() }

        composable(
            route = AppScreens.Purchases.route,
            enterTransition = { secondaryScreenEnterTransition() },
            exitTransition = { secondaryScreenExitTransition() }
        ) { PurchaseScreen() }

        composable(
            route = AppScreens.AboutNotepad.route,
            enterTransition = { secondaryScreenEnterTransition() },
            exitTransition = { secondaryScreenExitTransition() }
        ) { AboutNotepadScreen() }
    }
}

private fun secondaryScreenEnterTransition() = slideInHorizontally { it } + fadeIn()
private fun secondaryScreenExitTransition() = slideOutHorizontally { it } + fadeOut()


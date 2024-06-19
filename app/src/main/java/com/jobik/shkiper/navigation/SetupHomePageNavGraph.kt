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
import com.jobik.shkiper.screens.about.AboutNotepadScreen
import com.jobik.shkiper.screens.advancedSettings.AdvancedSettings
import com.jobik.shkiper.screens.archive.ArchiveNotesScreen
import com.jobik.shkiper.screens.basket.BasketNotesScreen
import com.jobik.shkiper.screens.noteListScreen.NoteListScreen
import com.jobik.shkiper.screens.note.NoteScreen
import com.jobik.shkiper.screens.purchase.PurchaseScreen
import com.jobik.shkiper.screens.settings.SettingsScreen
import com.jobik.shkiper.screens.statistics.StatisticsScreen

@OptIn(ExperimentalComposeUiApi::class, ExperimentalSharedTransitionApi::class)
@ExperimentalAnimationApi
@Composable
fun SetupAppScreenNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
) {
    SharedTransitionLayout(modifier = modifier) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.semantics { testTagsAsResourceId = true }
        ) {
            composable(
                route = Route.NoteList.route,
                enterTransition = { mainScreenEnterTransition() },
                exitTransition = { mainScreenExitTransition() }
            ) {
                NoteListScreen(
                    navController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                )
            }

            composable(
                route = Route.Archive.route,
                enterTransition = { mainScreenEnterTransition() },
                exitTransition = { mainScreenExitTransition() }
            ) { ArchiveNotesScreen(navController) }

            composable(
                route = Route.Basket.route,
                enterTransition = { mainScreenEnterTransition() },
                exitTransition = { mainScreenExitTransition() },
            ) { BasketNotesScreen(navController) }

            composable(
                route = Route.Settings.route,
                enterTransition = { mainScreenEnterTransition() },
                exitTransition = { mainScreenExitTransition() },
            ) { SettingsScreen(navController) }

            composable(
                route = Route.Note.route,
                arguments = listOf(navArgument(Argument_Note_Id) { type = NavType.StringType }),
                enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
            ) {
                NoteScreen(
                    onBack = { navController.popBackStack() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                )
            }

            composable(
                route = Route.AdvancedSettings.route,
                enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
            ) { AdvancedSettings() }

            composable(
                route = Route.Statistics.route,
                enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
            ) { StatisticsScreen() }

            composable(
                route = Route.Purchases.route,
                enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
            ) { PurchaseScreen() }

            composable(
                route = Route.AboutNotepad.route,
                enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
            ) { AboutNotepadScreen() }
        }
    }
}

package com.jobik.shkiper.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
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

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

@OptIn(ExperimentalComposeUiApi::class, ExperimentalSharedTransitionApi::class)
@ExperimentalAnimationApi
@Composable
fun SetupAppScreenNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
) {
    SharedTransitionLayout(modifier = modifier) {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
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
                    CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                        NoteListScreen(
                            navController = navController,
                        )
                    }
                }

                composable(
                    route = Route.Archive.route,
                    enterTransition = { mainScreenEnterTransition() },
                    exitTransition = { mainScreenExitTransition() }
                ) {
                    CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                        ArchiveNotesScreen(navController)
                    }
                }

                composable(
                    route = Route.Basket.route,
                    enterTransition = { mainScreenEnterTransition() },
                    exitTransition = { mainScreenExitTransition() },
                ) {
                    CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                        BasketNotesScreen(navController)
                    }
                }

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
                    CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                        NoteScreen(
                            onBack = { navController.popBackStack() },
                        )
                    }
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
}

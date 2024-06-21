package com.jobik.shkiper.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.screens.about.AboutNotepadScreen
import com.jobik.shkiper.screens.advancedSettings.AdvancedSettings
import com.jobik.shkiper.screens.archive.ArchiveNotesScreen
import com.jobik.shkiper.screens.basket.BasketNotesScreen
import com.jobik.shkiper.screens.calendar.CalendarScreen
import com.jobik.shkiper.screens.note.NoteScreen
import com.jobik.shkiper.screens.noteListScreen.NoteListScreen
import com.jobik.shkiper.screens.purchase.PurchaseScreen
import com.jobik.shkiper.screens.settings.SettingsScreen
import com.jobik.shkiper.screens.statistics.StatisticsScreen
import com.jobik.shkiper.ui.helpers.LocalNavAnimatedVisibilityScope
import com.jobik.shkiper.ui.helpers.LocalSharedElementKey
import com.jobik.shkiper.ui.helpers.LocalSharedTransitionScope

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
                    route = Screen.NoteList.value,
                    enterTransition = { mainScreenEnterTransition() },
                    exitTransition = { mainScreenExitTransition() }
                ) {
                    CompositionLocalProvider(
                        LocalNavAnimatedVisibilityScope provides this,
                        LocalSharedElementKey provides Screen.NoteList.name
                    ) {
                        NoteListScreen(
                            navController = navController,
                        )
                    }
                }

                composable(
                    route = Screen.Calendar.value,
                    enterTransition = { secondaryToNoteEnterTransition() },
                    exitTransition = { secondaryToNoteExitTransition() }
                ) {
                    CompositionLocalProvider(
                        LocalNavAnimatedVisibilityScope provides this,
                        LocalSharedElementKey provides Screen.Calendar.name
                    ) {
                        CalendarScreen(
                            navController = navController,
                        )
                    }
                }

                composable(
                    route = Screen.Archive.value,
                    enterTransition = { mainScreenEnterTransition() },
                    exitTransition = { mainScreenExitTransition() }
                ) {
                    CompositionLocalProvider(
                        LocalNavAnimatedVisibilityScope provides this,
                        LocalSharedElementKey provides Screen.Calendar.name
                    ) {
                        ArchiveNotesScreen(navController)
                    }
                }

                composable(
                    route = Screen.Basket.value,
                    enterTransition = { mainScreenEnterTransition() },
                    exitTransition = { mainScreenExitTransition() },
                ) {
                    CompositionLocalProvider(
                        LocalNavAnimatedVisibilityScope provides this,
                        LocalSharedElementKey provides Screen.Calendar.name
                    ) {
                        BasketNotesScreen(navController)
                    }
                }

                composable(
                    route = Screen.Settings.value,
                    enterTransition = { mainScreenEnterTransition() },
                    exitTransition = { mainScreenExitTransition() },
                ) { SettingsScreen(navController) }

                composable(
                    route = Screen.Note.value,
                    arguments = listOf(
                        navArgument(Argument_Note_Id) { type = NavType.StringType },
                        navArgument(Argument_Shared_Origin) { type = NavType.StringType }),
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) {
                    CompositionLocalProvider(
                        LocalNavAnimatedVisibilityScope provides this,
                        LocalSharedElementKey provides it.arguments?.getString(
                            Argument_Shared_Origin
                        ).toString()
                    ) {
                        val context = LocalContext.current
                        NoteScreen(
                            onBack = {
                                if (navController.previousBackStackEntry == null) {
                                    IntentHelper().startAppActivity(context)
                                }
                                navController.popBackStack()
                            },
                        )
                    }
                }

                composable(
                    route = Screen.AdvancedSettings.value,
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) { AdvancedSettings() }

                composable(
                    route = Screen.Statistics.value,
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) { StatisticsScreen() }

                composable(
                    route = Screen.Purchases.value,
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) { PurchaseScreen() }

                composable(
                    route = Screen.AboutNotepad.value,
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) { AboutNotepadScreen() }
            }
        }
    }
}

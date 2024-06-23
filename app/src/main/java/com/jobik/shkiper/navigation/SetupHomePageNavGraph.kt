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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.navigation.NavigationHelpers.Companion.canNavigate
import com.jobik.shkiper.screens.about.AboutNotepadScreen
import com.jobik.shkiper.screens.advancedSettings.AdvancedSettings
import com.jobik.shkiper.screens.calendar.CalendarScreen
import com.jobik.shkiper.screens.note.NoteScreen
import com.jobik.shkiper.screens.noteList.NoteList
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
    startDestination: Screen,
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
                composable<Screen.NoteList>(
                    enterTransition = { ScreenTransition().mainScreenEnterTransition(this) },
                    exitTransition = { ScreenTransition().mainScreenExitTransition(this) }
                ) {
                    CompositionLocalProvider(
                        LocalNavAnimatedVisibilityScope provides this,
                        LocalSharedElementKey provides Screen.NoteList.toString()
                    ) {
                        NoteList(
                            navController = navController,
                        )
                    }
                }

                composable<Screen.Calendar>(
                    enterTransition = { ScreenTransition().secondaryToNoteEnterTransition(this) },
                    exitTransition = { ScreenTransition().secondaryToNoteExitTransition(this) }
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

                composable<Screen.Settings>(
                    enterTransition = { ScreenTransition().mainScreenEnterTransition(this) },
                    exitTransition = { ScreenTransition().mainScreenExitTransition(this) }
                ) { SettingsScreen(navController) }

                composable<Screen.Note>(
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) {
                    val noteScreenArgs: Screen.Note = it.toRoute()

                    CompositionLocalProvider(
                        LocalNavAnimatedVisibilityScope provides this,
                        LocalSharedElementKey provides noteScreenArgs.sharedElementOrigin
                    ) {
                        val context = LocalContext.current
                        NoteScreen(
                            onBack = {
                                if (navController.canNavigate().not()) return@NoteScreen
                                if (navController.previousBackStackEntry == null) {
                                    IntentHelper().startAppActivity(context)
                                } else {
                                    navController.popBackStack()
                                }
                            },
                        )
                    }
                }

                composable<Screen.AdvancedSettings>(
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) { AdvancedSettings() }

                composable<Screen.Statistics>(
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) { StatisticsScreen() }

                composable<Screen.Purchases>(
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) { PurchaseScreen() }

                composable<Screen.AboutNotepad>(
                    enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
                    exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
                ) { AboutNotepadScreen() }
            }
        }
    }
}

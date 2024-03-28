package com.jobik.shkiper.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavBackStackEntry
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
            NoteListScreen(navController)
        }

        composable(
            route = Route.Archive.route,
            enterTransition = { mainScreenEnterTransition() },
            exitTransition = { mainScreenExitTransition() }
        ) {
            ArchiveNotesScreen(navController)
        }

        composable(
            route = Route.Basket.route,
            enterTransition = { mainScreenEnterTransition() },
            exitTransition = { mainScreenExitTransition() }
        ) {
            BasketNotesScreen(navController)
        }

        composable(
            route = Route.Settings.route,
            enterTransition = { mainScreenEnterTransition() },
            exitTransition = { mainScreenExitTransition() }
        ) {
            SettingsScreen(navController)
        }

        composable(
            route = Route.Note.route,
            arguments = listOf(navArgument(Argument_Note_Id) { type = NavType.StringType }),
            enterTransition = { fadeIn() + scaleIn(initialScale = 0.9f) },
            exitTransition = { fadeOut() + scaleOut(targetScale = 0.9f) }
        ) { NoteScreen(navController) }

        composable(
            route = Route.Onboarding.route,
            enterTransition = { secondaryScreenEnterTransition() },
            exitTransition = { secondaryScreenExitTransition() }
        ) { OnBoardingScreen(navController) }

        composable(
            route = Route.Statistics.route,
            enterTransition = { secondaryScreenEnterTransition() },
            exitTransition = { secondaryScreenExitTransition() }
        ) { StatisticsScreen() }

        composable(
            route = Route.Purchases.route,
            enterTransition = { secondaryScreenEnterTransition() },
            exitTransition = { secondaryScreenExitTransition() }
        ) { PurchaseScreen() }

        composable(
            route = Route.AboutNotepad.route,
            enterTransition = { secondaryScreenEnterTransition() },
            exitTransition = { secondaryScreenExitTransition() }
        ) { AboutNotepadScreen() }
    }
}

private fun secondaryScreenEnterTransition() = slideInHorizontally { it } + fadeIn()
private fun secondaryScreenExitTransition() = slideOutHorizontally { it } + fadeOut()

private fun AnimatedContentTransitionScope<NavBackStackEntry>.mainScreenEnterTransition(): EnterTransition? {
    val initial = initialState.destination.route ?: return null
    val target = targetState.destination.route ?: return null

    if (initial == Route.Note.route) {
        return null
    }

    val initiatorRouteNumber = RouteHelper().getRouteNumber(initial) ?: return null
    val targetRouteNumber = RouteHelper().getRouteNumber(target) ?: return null

    if (initiatorRouteNumber > targetRouteNumber) {
        return slideInHorizontally { -it }
    } else if (initiatorRouteNumber < targetRouteNumber) {
        return slideInHorizontally { it }
    }

    return null
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.mainScreenExitTransition(): ExitTransition? {
    val initial = initialState.destination.route ?: return null
    val target = targetState.destination.route ?: return null

    if (initial == Route.Note.route) {
        return null
    }

    val initiatorRouteNumber = RouteHelper().getRouteNumber(initial) ?: return null
    val targetRouteNumber = RouteHelper().getRouteNumber(target) ?: return null

    if (initiatorRouteNumber > targetRouteNumber) {
        return slideOutHorizontally { it }
    } else if (initiatorRouteNumber < targetRouteNumber) {
        return slideOutHorizontally { -it }
    }

    return null
}
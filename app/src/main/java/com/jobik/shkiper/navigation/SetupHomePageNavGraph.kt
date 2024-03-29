package com.jobik.shkiper.navigation

import android.util.Log
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
            exitTransition = { mainScreenExitTransition() },
        ) {
            BasketNotesScreen(navController)
        }

        composable(
            route = Route.Settings.route,
            enterTransition = { mainScreenEnterTransition() },
            exitTransition = { mainScreenExitTransition() },
        ) {
            SettingsScreen(navController)
        }

        composable(
            route = Route.Note.route,
            arguments = listOf(navArgument(Argument_Note_Id) { type = NavType.StringType }),
            enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
            exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
        ) { NoteScreen(navController) }

        composable(
            route = Route.Onboarding.route,
            enterTransition = { ScreenTransition().secondaryScreenEnterTransition() },
            exitTransition = { ScreenTransition().secondaryScreenExitTransition() }
        ) { OnBoardingScreen(navController) }

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

class ScreenTransition {
    fun secondaryScreenEnterTransition() = slideInHorizontally { it }
    fun secondaryScreenExitTransition() = slideOutHorizontally { it }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.mainScreenEnterTransition(): EnterTransition? {
    val initial = initialState.destination.route?.substringBefore("/") ?: return null
    val target = targetState.destination.route?.substringBefore("/") ?: return null

//    // transition to note
//    if (initial == Route.Note.route.substringBefore("/")) {
//        return null
//    }

    // transition after secondary screen
    if (RouteHelper().isSecondaryRoute(initial)) {
        return slideInHorizontally { -150 }
    }

    // transition between main screens
    val initiatorRouteNumber = RouteHelper().getRouteNumber(initial) ?: return null
    val targetRouteNumber = RouteHelper().getRouteNumber(target) ?: return null

    if (initiatorRouteNumber > targetRouteNumber) {
        return slideInHorizontally { -it } + fadeIn()
    } else if (initiatorRouteNumber < targetRouteNumber) {
        return slideInHorizontally { it } + fadeIn()
    }

    return null
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.mainScreenExitTransition(): ExitTransition? {
    val initial = initialState.destination.route?.substringBefore("/") ?: return null
    val target = targetState.destination.route?.substringBefore("/") ?: return null

//    // transition to note
//    if (target == Route.Note.route.substringBefore("/")) {
//        return null
//    }

    // transition before secondary screen
    if (RouteHelper().isSecondaryRoute(target)) {
        return slideOutHorizontally { -150 }
    }

    // transition between main screens
    val initiatorRouteNumber = RouteHelper().getRouteNumber(initial) ?: return null
    val targetRouteNumber = RouteHelper().getRouteNumber(target) ?: return null

    if (initiatorRouteNumber > targetRouteNumber) {
        return slideOutHorizontally { it } + fadeOut()
    } else if (initiatorRouteNumber < targetRouteNumber) {
        return slideOutHorizontally { -it } + fadeOut()
    }

    return null
}

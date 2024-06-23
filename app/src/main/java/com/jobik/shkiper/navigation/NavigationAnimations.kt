package com.jobik.shkiper.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.jobik.shkiper.navigation.RouteHelper.Companion.getScreen

class ScreenTransition {
    fun secondaryScreenEnterTransition() = slideInHorizontally { it }

    fun secondaryScreenExitTransition() = slideOutHorizontally { it }

    fun mainScreenEnterTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition? {
        val initial: Screen = scope.initialState.getScreen() ?: return null
        val target: Screen = scope.targetState.getScreen() ?: return null

        // transition after Note screen
        if (initial.name == Screen.Note("").name) {
            return null
        }

        // transition after secondary screen
        if (RouteHelper().isSecondaryRoute(initial)) {
            return slideInHorizontally()
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

    fun mainScreenExitTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition? {
        val initial: Screen = scope.initialState.getScreen() ?: return null
        val target: Screen = scope.targetState.getScreen() ?: return null

        // transition before Note screen
        if (target.name == Screen.Note("").name) {
            return null
        }

        // transition before secondary screen
        if (RouteHelper().isSecondaryRoute(target)) {
            return slideOutHorizontally()
        }

        // transition between main screens
        val initiatorRouteNumber = RouteHelper().getRouteNumber(initial) ?: return null
        val targetRouteNumber = RouteHelper().getRouteNumber(target) ?: return null

        if (initiatorRouteNumber > targetRouteNumber) {
            return slideOutHorizontally { it }
        } else if (initiatorRouteNumber < targetRouteNumber) {
            return slideOutHorizontally { -it }
        }

        return null
    }

    fun secondaryToNoteEnterTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition? {
        val initial: Screen = scope.initialState.getScreen() ?: return null

        // transition before Note screen
        if (initial.name == Screen.Note("").name) {
            return null
        }

        return ScreenTransition().secondaryScreenEnterTransition()
    }

    fun secondaryToNoteExitTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition? {
        val target: Screen = scope.targetState.getScreen() ?: return null

        // transition after Note screen
        if (target.name == Screen.Note("").name) {
            return null
        }

        return ScreenTransition().secondaryScreenExitTransition()
    }
}


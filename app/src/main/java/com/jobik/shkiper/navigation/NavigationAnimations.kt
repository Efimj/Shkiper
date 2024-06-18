package com.jobik.shkiper.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

class ScreenTransition {
    fun secondaryScreenEnterTransition() = slideInHorizontally { it }
    fun secondaryScreenExitTransition() = slideOutHorizontally { it }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.mainScreenEnterTransition(): EnterTransition? {
    val initial = initialState.destination.route?.substringBefore("/") ?: return null
    val target = targetState.destination.route?.substringBefore("/") ?: return null

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

fun AnimatedContentTransitionScope<NavBackStackEntry>.mainScreenExitTransition(): ExitTransition? {
    val initial = initialState.destination.route?.substringBefore("/") ?: return null
    val target = targetState.destination.route?.substringBefore("/") ?: return null

    // transition before secondary screen
    if (RouteHelper().isSecondaryRoute(target)) {
        return slideOutHorizontally { -150 }
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

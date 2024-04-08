package com.jobik.shkiper.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

class NavigationHelpers {
    companion object {

        fun NavController.canNavigate(): Boolean {
            return this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
        }

        fun NavController.checkIsDestinationCurrent(route: String): Boolean {
            return currentDestination?.route?.substringBefore("/") == route.substringBefore("/")
        }

        fun NavController.navigateToMain(destination: String) = run {
            if (canNavigate().not()) return@run
            if (checkIsDestinationCurrent(destination)) return@run
            navigate(destination) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }

        fun NavController.navigateToSecondary(destination: String) = run {
            if (canNavigate().not()) return@run
            if (checkIsDestinationCurrent(destination)) return@run
            navigate(destination) {
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    }
}
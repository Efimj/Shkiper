package com.jobik.shkiper.navigation

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

class NavigationHelpers {
    companion object {

        fun NavController.canNavigate(): Boolean {
            return this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
        }

        fun NavController.navigateToMain(destination: Screen) = run {
            if (canNavigate().not()) return@run
            if (checkIsDestinationCurrent(destination)) return@run
            navigate(destination) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                try {
                    popUpTo(graph.findStartDestination().id) {
                        saveState = true
                    }
                } catch (e: Exception) {
                    Log.e("navigateToMain", "findStartDestination", e)
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }

        fun NavController.navigateToSecondary(destination: Screen) = run {
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

        private fun NavController.checkIsDestinationCurrent(destination: Screen): Boolean {
            val backStackEntry = this.currentBackStackEntry ?: return false
            return backStackEntry.destination.hierarchy.any {
                it.hasRoute(destination::class)
            }
        }
    }
}
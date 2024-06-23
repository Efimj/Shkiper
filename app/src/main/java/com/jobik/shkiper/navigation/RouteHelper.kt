package com.jobik.shkiper.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

class RouteHelper {
    val NumberedRoutes = listOf(Screen.NoteList, Screen.Settings)
    val SecondaryRoutes = listOf(
        Screen.Note(id = ""),
        Screen.Calendar,
        Screen.Statistics,
        Screen.Purchases,
        Screen.AboutNotepad,
        Screen.AdvancedSettings
    )

    /**
     * To find the route number to determine the direction of transition
     */
    fun getRouteNumber(route: Screen): Int? {
        NumberedRoutes.forEachIndexed { index, element ->
            if (element.name == route.name) return index
        }
        return null
    }

    /**
     * This routes not show navigation button
     */
    fun isSecondaryRoute(route: Screen): Boolean {
        return SecondaryRoutes.any { it.name == route.name }
    }

    companion object {
        /**
         * To get route
         */
        fun NavHostController.getScreen(): Screen? {
            val currentDestination = this.currentBackStackEntry?.destination ?: return null
            (RouteHelper().NumberedRoutes + RouteHelper().SecondaryRoutes).forEach { screen ->
                currentDestination.hierarchy.any {
                    it.hasRoute(screen::class)
                } == true && return screen
            }
            return null
        }

        fun NavBackStackEntry.getScreen(): Screen? {
            val currentDestination = this.destination
            (RouteHelper().NumberedRoutes + RouteHelper().SecondaryRoutes).forEach { screen ->
                currentDestination.hierarchy.any {
                    it.hasRoute(screen::class)
                } == true && return screen
            }
            return null
        }
    }
}
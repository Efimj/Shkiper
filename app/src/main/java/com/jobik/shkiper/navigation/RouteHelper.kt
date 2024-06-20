package com.jobik.shkiper.navigation

class RouteHelper {
    val NumberedRoutes = listOf(Route.NoteList, Route.Archive, Route.Basket, Route.Settings)
    val SecondaryRoutes = listOf(
        Route.Note,
        Route.Calendar,
        Route.Statistics,
        Route.Purchases,
        Route.AboutNotepad,
        Route.AdvancedSettings
    )

    /**
     * To find the route number to determine the direction of transition
     */
    fun getRouteNumber(route: String): Int? {
        NumberedRoutes.forEachIndexed { index, element ->
            if (element.value.substringBefore("/") == route.substringBefore("/")) return index
        }
        return null
    }

    /**
     * This routes not show navigation button
     */
    fun isSecondaryRoute(route: String): Boolean {
        return SecondaryRoutes.any { it.value.substringBefore("/") == route.substringBefore("/") }
    }
}
package com.jobik.shkiper.navigation

class RouteHelper {
    val SecondaryRoutes = listOf(Route.Onboarding, Route.Note, Route.Statistics, Route.Purchases, Route.AboutNotepad)
    val NumberedRoutes = listOf(Route.NoteList, Route.Archive, Route.Basket, Route.Settings)

    /**
     * To find the route number to determine the direction of transition
     */
    fun getRouteNumber(route: String): Int? {
        NumberedRoutes.forEachIndexed { index, element ->
            if (element.route.substringBefore("/") == route.substringBefore("/")) return index
        }
        return null
    }

    /**
     * This routes not show navigation button
     */
    fun isSecondaryRoute(route: String): Boolean {
        return SecondaryRoutes.any { it.route.substringBefore("/") == route.substringBefore("/") }
    }
}
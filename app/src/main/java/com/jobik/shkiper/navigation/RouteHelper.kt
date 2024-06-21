package com.jobik.shkiper.navigation

class RouteHelper {
    val NumberedRoutes = listOf(Screen.NoteList, Screen.Archive, Screen.Basket, Screen.Settings)
    val SecondaryRoutes = listOf(
        Screen.Note,
        Screen.Calendar,
        Screen.Statistics,
        Screen.Purchases,
        Screen.AboutNotepad,
        Screen.AdvancedSettings
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
package com.jobik.shkiper.navigation

const val Argument_Note_Id = "noteId"
const val Argument_Note_Position = "position"

sealed class AppScreens(val route: String) {
    object Onboarding : AppScreens(route = "onboarding")
    object NoteList : AppScreens(route = "note_list/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    object Archive : AppScreens(route = "archive/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    object Basket : AppScreens(route = "basket/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    object Settings : AppScreens(route = "settings")
    object Statistics : AppScreens(route = "statistics")
    object Note : AppScreens(route = "note/{$Argument_Note_Id}") {
        fun noteId(id: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Id}", newValue = id)
        }
    }

    /**
     * This routes not show navigation button
     */
    object SecondaryRoutes {
        val secondaryRoutes = listOf(Onboarding, Note, Statistics)
        fun isSecondaryRoute(route: String): Boolean {
            return secondaryRoutes.any { it.route.substringBefore("/") == route }
        }
    }
}

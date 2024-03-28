package com.jobik.shkiper.navigation

import androidx.annotation.Keep

@Keep
const val Argument_Note_Id = "noteId"

@Keep
const val Argument_Note_Position = "position"

@Keep
sealed class AppScreens(val route: String) {
    data object Onboarding : AppScreens(route = "onboarding")
    data object NoteList : AppScreens(route = "note_list/{$Argument_Note_Position}") {
        const val number = 1

        fun notePosition(position: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Archive : AppScreens(route = "archive/{$Argument_Note_Position}") {
        const val number = 2

        fun notePosition(position: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Basket : AppScreens(route = "basket/{$Argument_Note_Position}") {
        const val number = 3

        fun notePosition(position: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Settings : AppScreens(route = "settings"){
        const val number = 4
    }

    data object Note : AppScreens(route = "note/{$Argument_Note_Id}") {
        fun noteId(id: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Id}", newValue = id)
        }
    }

    data object Statistics : AppScreens(route = "statistics")
    data object AboutNotepad : AppScreens(route = "about_notepad")
    data object Purchases : AppScreens(route = "purchases")

    companion object{
        /**
         * This routes not show navigation button
         */
        val secondaryRoutes = listOf(Onboarding, Note, Statistics, Purchases, AboutNotepad)
        fun isSecondaryRoute(route: String): Boolean {
            return secondaryRoutes.any { it.route.substringBefore("/") == route }
        }
    }
}

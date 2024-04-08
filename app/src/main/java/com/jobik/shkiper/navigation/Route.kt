package com.jobik.shkiper.navigation

import androidx.annotation.Keep

@Keep
const val Argument_Note_Id = "noteId"

@Keep
const val Argument_Note_Position = "position"

@Keep
sealed class Route(val route: String) {

    data object Onboarding : Route(route = "onboarding")
    data object NoteList : Route(route = "note_list/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Archive : Route(route = "archive/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Basket : Route(route = "basket/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Settings : Route(route = "settings")

    data object Note : Route(route = "note/{$Argument_Note_Id}") {
        fun noteId(id: String): String {
            return this.route.replace(oldValue = "{$Argument_Note_Id}", newValue = id)
        }
    }

    data object Statistics : Route(route = "statistics")
    data object AboutNotepad : Route(route = "about_notepad")
    data object Purchases : Route(route = "purchases")
}
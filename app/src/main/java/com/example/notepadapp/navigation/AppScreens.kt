package com.example.notepadapp.navigation

import android.util.Log

const val ARGUMENT_NOTE_ID = "noteId"

sealed class AppScreens(val route: String) {
    object Onboarding : AppScreens(route = "onboarding")
    object NoteList : AppScreens(route = "note_list")
    object Archive : AppScreens(route = "archive")
    object Basket : AppScreens(route = "basket")
    object Settings : AppScreens(route = "settings")
    object Note : AppScreens(route = "note/{$ARGUMENT_NOTE_ID}") {
        fun noteId(id: String): String {
            return this.route.replace(oldValue = "{$ARGUMENT_NOTE_ID}", newValue = id)
        }
    }

    object SecondaryRoutes {
        val secondaryRoutes = listOf(Onboarding, Note)
        fun isSecondaryRoute(route: String): Boolean {
            return secondaryRoutes.any { it.route.substringBefore("/") == route }
        }
    }
}

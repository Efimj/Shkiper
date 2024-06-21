package com.jobik.shkiper.navigation

import androidx.annotation.Keep
import com.jobik.shkiper.ui.components.cards.NoteSharedOriginDefault

@Keep
const val Argument_Shared_Origin = NoteSharedOriginDefault

@Keep
const val Argument_Note_Id = "noteId"

@Keep
const val Argument_Note_Position = "position"

@Keep
sealed class Screen(val value: String) {
    data object NoteList : Screen(value = "note_list/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.value.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Archive : Screen(value = "archive/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.value.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Basket : Screen(value = "basket/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.value.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Settings : Screen(value = "settings")

    data object Note : Screen(value = "note/{$Argument_Note_Id}/{$Argument_Shared_Origin}") {
        fun configure(id: String, sharedElementOrigin: String): String {
            return this.value.replace(oldValue = "{$Argument_Note_Id}", newValue = id)
                .replace(oldValue = "{$Argument_Shared_Origin}", newValue = sharedElementOrigin)
        }
    }

    data object Calendar : Screen(value = "calendar")
    data object AdvancedSettings : Screen(value = "advanced_settings")
    data object Statistics : Screen(value = "statistics")
    data object AboutNotepad : Screen(value = "about_notepad")
    data object Purchases : Screen(value = "purchases")

    val name: String
        get() {
            return this.value.substringBefore("/")
        }
}
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
sealed class Route(val value: String) {
    data object NoteList : Route(value = "note_list/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.value.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Archive : Route(value = "archive/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.value.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Basket : Route(value = "basket/{$Argument_Note_Position}") {
        fun notePosition(position: String): String {
            return this.value.replace(oldValue = "{$Argument_Note_Position}", newValue = position)
        }
    }

    data object Settings : Route(value = "settings")

    data object Note : Route(value = "note/{$Argument_Note_Id}/{$Argument_Shared_Origin}") {
        fun configure(id: String, sharedElementOrigin: String): String {
            return this.value.replace(oldValue = "{$Argument_Note_Id}", newValue = id)
                .replace(oldValue = "{$Argument_Shared_Origin}", newValue = sharedElementOrigin)
        }
    }

    data object Calendar : Route(value = "calendar")
    data object AdvancedSettings : Route(value = "advanced_settings")
    data object Statistics : Route(value = "statistics")
    data object AboutNotepad : Route(value = "about_notepad")
    data object Purchases : Route(value = "purchases")

    val name: String
        get() {
            return this.value.substringBefore("/")
        }
}
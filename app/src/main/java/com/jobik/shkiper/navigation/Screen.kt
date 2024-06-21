package com.jobik.shkiper.navigation

import androidx.annotation.Keep
import com.jobik.shkiper.ui.components.cards.NoteSharedOriginDefault
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Screen(val name: String) {

    @Serializable
    data object NoteList : Screen(name = "NoteList")

    @Serializable
    data object Archive : Screen(name = "Archive")

    @Serializable
    data object Basket : Screen(name = "Basket")

    @Serializable
    data object Settings : Screen(name = "Settings")

    @Serializable
    data class Note(val id: String, val sharedElementOrigin: String = NoteSharedOriginDefault) :
        Screen(name = "Note")

    @Serializable
    data object Calendar : Screen(name = "Calendar")

    @Serializable
    data object AdvancedSettings : Screen(name = "AdvancedSettings")

    @Serializable
    data object Statistics : Screen(name = "Statistics")

    @Serializable
    data object AboutNotepad : Screen(name = "AboutNotepad")

    @Serializable
    data object Purchases : Screen(name = "Purchases")
}
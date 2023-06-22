package com.example.notepadapp.navigation

const val ARGUMENT_NOTE_ID = "noteId"
sealed class UserPages(val route: String) {
        object NoteList: AppScreen(route = "note_list")
        object Archive: AppScreen(route = "archive")
        object Basket: AppScreen(route = "basket")
        object Settings: AppScreen(route = "settings")
        object Note: AppScreen(route = "note/{$ARGUMENT_NOTE_ID}"){
                fun noteId(id: String):String{
                        return this.route.replace(oldValue = "{$ARGUMENT_NOTE_ID}", newValue = id)
                }
        }
}

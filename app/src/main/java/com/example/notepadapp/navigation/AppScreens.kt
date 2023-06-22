package com.example.notepadapp.navigation

const val ARGUMENT_NOTE_ID = "noteId"
sealed class AppScreens(val route: String) {
        object NoteList: AppScreens(route = "note_list")
        object Archive: AppScreens(route = "archive")
        object Basket: AppScreens(route = "basket")
        object Settings: AppScreens(route = "settings")
        object Note: AppScreens(route = "note/{$ARGUMENT_NOTE_ID}"){
                fun noteId(id: String):String{
                        return this.route.replace(oldValue = "{$ARGUMENT_NOTE_ID}", newValue = id)
                }
        }
}

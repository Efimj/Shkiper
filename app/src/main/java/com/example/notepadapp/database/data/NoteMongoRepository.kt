package com.example.notepadapp.database.data

import com.example.notepadapp.database.models.Note
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface NoteMongoRepository {
    fun getNotes(): Flow<List<Note>>
    fun getNote(id: ObjectId): Note?
    fun filterNotesByContains(name: String): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(id: ObjectId)
    suspend fun deleteNote(id: List<ObjectId>)
}
package com.android.notepad.database.data.note

import com.android.notepad.database.models.Note
import com.android.notepad.database.models.NotePosition
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface NoteMongoRepository {
    fun getNotes(): Flow<List<Note>>
    fun getNote(id: ObjectId): Note?
    fun getNotes(position: NotePosition): Flow<List<Note>>
    fun getNotes(ids: List<ObjectId>): List<Note>
    fun getAllNotes(): List<Note>
    fun getNotesByHashtag(position: NotePosition, hashtag: String): Flow<List<Note>>
    fun getHashtags(position: NotePosition): Flow<Set<String>>
    fun filterNotesByContains(text: String): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun insertOrUpdateNotes(notes: List<Note>, updateStatistics: Boolean = true)
    suspend fun updateNote(id: ObjectId, updateParams: (Note) -> Unit)
    suspend fun updateNote(ids: List<ObjectId>, updateParams: (Note) -> Unit)
    suspend fun deleteNote(id: ObjectId)
    suspend fun deleteNote(ids: List<ObjectId>)
}
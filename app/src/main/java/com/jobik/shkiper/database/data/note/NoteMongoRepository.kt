package com.jobik.shkiper.database.data.note

import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.NotePosition
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface NoteMongoRepository {
    fun getNotesFlow(): Flow<List<Note>>
    fun getNote(id: ObjectId): Note?
    fun getNotes(position: NotePosition): List<Note>
    fun getNotesFlow(position: NotePosition): Flow<List<Note>>
    fun getNotesFlow(ids: List<ObjectId>): List<Note>
    fun getAllNotes(): List<Note>
    fun getNotesByHashtag(position: NotePosition, hashtag: String): Flow<List<Note>>
    fun getHashtags(): Set<String>
    fun getHashtags(position: NotePosition): Flow<Set<String>>
    suspend fun insertNote(note: Note)
    suspend fun insertOrUpdateNotes(notes: List<Note>, updateStatistics: Boolean = true)
    suspend fun updateNote(id: ObjectId, updateParams: (Note) -> Unit)
    suspend fun updateNote(ids: List<ObjectId>, updateParams: (Note) -> Unit)
    suspend fun deleteNote(id: ObjectId)
    suspend fun deleteNote(ids: List<ObjectId>)
}
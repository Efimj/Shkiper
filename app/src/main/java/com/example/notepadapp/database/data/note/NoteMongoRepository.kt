package com.example.notepadapp.database.data.note

import android.content.Context
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.database.models.NotePosition
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.util.*

interface NoteMongoRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getNote(id: ObjectId): Note?
    fun getNotes(position: NotePosition): Flow<List<Note>>
    fun getNotes(ids: List<ObjectId>): List<Note>
    fun getNotesByHashtag(position: NotePosition, hashtag: String): Flow<List<Note>>
    fun getHashtags(position: NotePosition): Flow<Set<String>>
    fun filterNotesByContains(text: String): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(id: ObjectId, context: Context, updateParams: (Note) -> Unit)
    suspend fun updateNote(ids: List<ObjectId>, context: Context, updateParams: (Note) -> Unit)
    suspend fun deleteNote(id: ObjectId, context: Context)
    suspend fun deleteNote(ids: List<ObjectId>, context: Context)
}
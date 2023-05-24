package com.example.notepadapp.database.data

import android.util.Log
import com.example.notepadapp.database.models.Note
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class NoteMongoRepositoryImpl(val realm: Realm): NoteMongoRepository {
    override fun getNotes(): Flow<List<Note>> {
        return realm.query<Note>().asFlow().map { it.list }
    }

    override fun filterNotesByContains(name: String): Flow<List<Note>> {
        return realm.query<Note>(query = "header CONTAINS[c] $0", name).asFlow().map { it.list }
    }

    override suspend fun insertNote(note: Note) {
        realm.write { copyToRealm(note) }
    }

    override suspend fun updateNote(note: Note) {
        realm.write {
            val queriedNote = query<Note>(query = "_id == $0", note._id).first().find()
            queriedNote?.header = note.header
            queriedNote?.body = note.body
            queriedNote?.updateDate = note.updateDate
            queriedNote?.creationDate = note.creationDate
            queriedNote?.deletionDate = note.deletionDate
            queriedNote?.hashtags = note.hashtags
            queriedNote?.isPinned = note.isPinned
            queriedNote?.position = note.position
        }
    }

    override suspend fun deleteNote(id: ObjectId) {
        realm.write {
            val note = query<Note>(query = "_id == $0", id).first().find()
            try {
                note?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("NoteMongoRepositoryImpl", "${e.message}")
            }
        }
    }
}
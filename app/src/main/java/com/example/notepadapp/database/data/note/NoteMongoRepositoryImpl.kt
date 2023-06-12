package com.example.notepadapp.database.data.note

import android.util.Log
import com.example.notepadapp.database.models.Note
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class NoteMongoRepositoryImpl(val realm: Realm) : NoteMongoRepository {
    override fun getAllNotes(): Flow<List<Note>> {
        return realm.query<Note>()
            .sort("_id", Sort.DESCENDING)
            .asFlow()
            .map { it.list }
    }

    override fun getNotes(pinned: Boolean): Flow<List<Note>> {
        return realm.query<Note>(query = "isPinned == $0", pinned)
            .sort("_id", Sort.DESCENDING)
            .asFlow()
            .map { it.list }
    }

    override fun getNote(id: ObjectId): Note? {
        return realm.query<Note>(query = "_id == $0", id).first().find()
    }

    override fun filterNotesByContains(text: String): Flow<List<Note>> {
        return realm.query<Note>(query = "header CONTAINS[c] $0 OR body CONTAINS[c] $0", text).asFlow().map { it.list }
    }

    override suspend fun insertNote(note: Note) {
        realm.write { copyToRealm(note) }
    }

    override suspend fun updateNote(id: ObjectId, updateParams: (Note) -> Unit) {
        getNote(id)?.also { currentNote ->
            realm.writeBlocking {
                val queriedNote = findLatest(currentNote)
                queriedNote?.apply {
                    updateParams(this)
                }
            }
        }
    }

    override suspend fun updateNote(
        ids: List<ObjectId>,
        updateParams: (Note) -> Unit
    ) {
        realm.writeBlocking {
            for (id in ids) {
                val note = getNote(id) ?: continue
                try {
                    findLatest(note)?.let(updateParams)
                } catch (e: Exception) {
                    Log.d("NoteMongoRepositoryImpl", "${e.message}")
                }
            }
        }
    }

    override suspend fun deleteNote(id: ObjectId) {
        realm.write {
            val note = getNote(id) ?: return@write
            try {
                findLatest(note)
                    ?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("NoteMongoRepositoryImpl", "${e.message}")
            }
        }
    }

    override suspend fun deleteNote(ids: List<ObjectId>) {
        realm.writeBlocking {
            for (id in ids) {
                val note = getNote(id) ?: continue
                try {
                    findLatest(note)
                        ?.let { delete(it) }
                } catch (e: Exception) {
                    Log.d("NoteMongoRepositoryImpl", "${e.message}")
                }
            }
        }
    }
}
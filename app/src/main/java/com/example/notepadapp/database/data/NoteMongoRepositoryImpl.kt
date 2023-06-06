package com.example.notepadapp.database.data

import android.util.Log
import com.example.notepadapp.database.models.Note
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class NoteMongoRepositoryImpl(val realm: Realm) : NoteMongoRepository {
    override fun getNotes(): Flow<List<Note>> {
        return realm.query<Note>()
            .sort("_id", Sort.DESCENDING)
            .asFlow()
            .map { it.list }
    }

    override fun getNote(id: ObjectId): Note? {
        return realm.query<Note>(query = "_id == $0", id).first().find()
    }

    override fun filterNotesByContains(name: String): Flow<List<Note>> {
        return realm.query<Note>(query = "header CONTAINS[c] $0 OR body CONTAINS[c] $0", name).asFlow().map { it.list }
    }

    override suspend fun insertNote(note: Note) {
        realm.write { copyToRealm(note) }
    }

    override suspend fun updateNote(note: Note) {
        getNote(note._id)?.also { currentNote ->
            realm.writeBlocking {
                val queriedNote = findLatest(currentNote)
                queriedNote?.apply {
                    header = note.header
                    body = note.body
                    updateDate = note.updateDate
                    creationDate = note.creationDate
                    deletionDate = note.deletionDate
                    hashtags = note.hashtags
                    isPinned = note.isPinned
                    position = note.position
                }
            }
        }
    }

    override suspend fun deleteNote(id: ObjectId) {
        realm.write {
            val note = getNote(id)
            try {
                note?.let { delete(it) }
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
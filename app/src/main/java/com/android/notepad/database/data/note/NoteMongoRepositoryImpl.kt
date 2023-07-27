package com.android.notepad.database.data.note

import android.content.Context
import android.util.Log
import com.android.notepad.database.data.reminder.ReminderMongoRepositoryImpl
import com.android.notepad.database.models.Note
import com.android.notepad.database.models.NotePosition
import com.android.notepad.services.notification_service.NotificationScheduler
import com.android.notepad.services.statistics_service.StatisticsService
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

class NoteMongoRepositoryImpl(val realm: Realm, @ApplicationContext val context: Context) : NoteMongoRepository {
    override fun getNotesFlow(): Flow<List<Note>> {
        return realm.query<Note>()
            .sort("_id", Sort.DESCENDING)
            .asFlow()
            .map { it.list }
    }

    override fun getNote(id: ObjectId): Note? {
        return realm.query<Note>(query = "_id == $0", id).first().find()
    }

    override fun getNotesFlow(position: NotePosition): Flow<List<Note>> {
        return realm.query<Note>(query = "positionString == $0", position.name)
            .sort("_id", Sort.DESCENDING)
            .asFlow()
            .map { it.list }
    }

    override fun getNotes(position: NotePosition): List<Note> {
        return realm.query<Note>(query = "positionString == $0", position.name)
            .sort("_id", Sort.DESCENDING).find()
    }

    override fun getNotesByHashtag(position: NotePosition, hashtag: String): Flow<List<Note>> {
        return realm.query<Note>(query = "positionString == $0 AND $1 IN hashtags", position.name, hashtag)
            .sort("_id", Sort.DESCENDING)
            .asFlow()
            .map { it.list }
    }

    override fun getHashtags(position: NotePosition): Flow<Set<String>> {
        return realm.query<Note>(query = "positionString == $0", position.name)
            .sort("_id", Sort.DESCENDING)
            .asFlow().map { notes ->
                notes.list.flatMap { it.hashtags }.toSet()
            }
    }

    override fun getNotesFlow(ids: List<ObjectId>): List<Note> {
        return realm.query<Note>().find().filter { ids.any { id -> id == it._id } }
    }

    override fun getAllNotes(): List<Note> {
        return realm.query<Note>().find()
    }

    override fun filterNotesByContains(text: String, position: NotePosition): Flow<List<Note>> {
        return realm.query<Note>(
            query = "(header CONTAINS[c] $0 OR body CONTAINS[c] $0) AND positionString == $1",
            text,
            position.name
        ).sort("_id", Sort.DESCENDING).asFlow().map { it.list }
    }

    override suspend fun insertNote(note: Note) {
        realm.write { copyToRealm(note) }

        // Statistics update
        val statisticsService = StatisticsService(context)
        statisticsService.appStatistics.apply {
            createdNotesCount.increment()
        }
        statisticsService.saveStatistics()
    }

    override suspend fun insertOrUpdateNotes(notes: List<Note>, updateStatistics: Boolean) {
        realm.writeBlocking {
            for (note in notes) {
                val existedNote = getNote(note._id)
                if (existedNote == null) {
                    copyToRealm(note)
                    continue
                }
                try {
                    copyToRealm(note, UpdatePolicy.ALL)
                    updateNotification(note)
                } catch (e: Exception) {
                    Log.d("NoteMongoRepositoryImpl", "${e.message}")
                }
            }
        }
        if (!updateStatistics) return
        // Statistics update
        val statisticsService = StatisticsService(context)
        statisticsService.appStatistics.apply {
            repeat(notes.size) {
                createdNotesCount.increment()
            }
        }
        statisticsService.saveStatistics()
    }

    override suspend fun updateNote(id: ObjectId, updateParams: (Note) -> Unit) {
        getNote(id)?.also { currentNote ->
            realm.writeBlocking {
                val queriedNote = findLatest(currentNote) ?: return@writeBlocking
                queriedNote.apply {
                    updateParams(this)
                }
                updateNotification(queriedNote)
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
                    val latest = findLatest(note) ?: continue
                    latest.let(updateParams)
                    updateNotification(latest)
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
                val latest = findLatest(note) ?: return@write
                delete(latest)
                // update statistics
                statisticsNoteDeletedIncrement()
            } catch (e: Exception) {
                Log.d("NoteMongoRepositoryImpl", "${e.message}")
            }
        }
        ReminderMongoRepositoryImpl(realm, context).deleteReminderForNote(id)
    }

    private fun statisticsNoteDeletedIncrement() {
        val statisticsService = StatisticsService(context)
        statisticsService.appStatistics.apply {
            noteDeletedCount.increment()
        }
        statisticsService.saveStatistics()
    }

    override suspend fun deleteNote(ids: List<ObjectId>) {
        realm.writeBlocking {
            for (id in ids) {
                val note = getNote(id) ?: continue
                try {
                    findLatest(note)
                        ?.let { delete(it) }
                    // update statistics
                    statisticsNoteDeletedIncrement()
                } catch (e: Exception) {
                    Log.d("NoteMongoRepositoryImpl", "${e.message}")
                }
            }
        }
        for (id in ids)
            ReminderMongoRepositoryImpl(realm, context).deleteReminderForNote(id)
    }

    private fun updateNotification(
        newNote: Note
    ) {
        val notificationScheduler = NotificationScheduler(context)
        if (newNote.position == NotePosition.DELETE) {
            notificationScheduler.cancelNotification(newNote._id.toHexString())
        } else {
            notificationScheduler.updateNotificationData(
                newNote._id.toHexString(),
                newNote.header,
                newNote.body,
                true
            )
        }
    }
}
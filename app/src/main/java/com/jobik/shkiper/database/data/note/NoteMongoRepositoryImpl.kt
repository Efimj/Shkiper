package com.jobik.shkiper.database.data.note

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.jobik.shkiper.database.data.reminder.ReminderMongoRepositoryImpl
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.services.notification_service.NotificationScheduler
import com.jobik.shkiper.services.statistics_service.StatisticsService
import com.jobik.shkiper.widgets.handlers.mapNoteToWidget
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

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

    override fun getHashtags(): Set<String> {
        return realm.query<Note>()
            .sort("_id", Sort.DESCENDING)
            .find()
            .flatMap { it.hashtags }.toSet()
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
        return realm.query<Note>().sort("_id", Sort.DESCENDING).find()
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
        for (note in notes) {
            realm.writeBlocking {
                val existedNote = getNote(note._id)
                try {
                    if (existedNote == null)
                        copyToRealm(note)
                    else
                        copyToRealm(note, UpdatePolicy.ALL)
                } catch (e: Exception) {
                    Log.d("NoteMongoRepositoryImpl", "${e.message}")
                }
            }
            getNote(note._id)?.let { note -> updateNotification(note) }
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
            var queriedNote: Note? = null
            realm.writeBlocking {
                queriedNote = findLatest(currentNote) ?: return@writeBlocking
                queriedNote?.apply {
                    updateParams(this)
                }
            }
            getNote(id)?.let { note -> updateNotification(note) }
        }
    }

    override suspend fun updateNote(
        ids: List<ObjectId>,
        updateParams: (Note) -> Unit
    ) {
        for (id in ids) {
            var latest: Note? = null
            realm.writeBlocking {
                try {
                    val note = getNote(id)
                    if (note != null)
                        latest = findLatest(note)
                    latest?.let(updateParams)
                } catch (e: Exception) {
                    Log.d("NoteMongoRepositoryImpl", "${e.message}")
                }
            }
            getNote(id)?.let { note -> updateNotification(note) }
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
        for (id in ids) {
            try {
                ReminderMongoRepositoryImpl(realm, context).deleteReminderForNote(id)
            } catch (e: Exception) {
                Log.d("NoteMongoRepositoryImpl - deleteNote", "${e.message}")
            }
        }
    }

    private fun statisticsNoteDeletedIncrement() {
        val statisticsService = StatisticsService(context)
        statisticsService.appStatistics.apply {
            noteDeletedCount.increment()
        }
        statisticsService.saveStatistics()
    }

    private suspend fun updateNotification(
        newNote: Note
    ) {
        GlanceAppWidgetManager(context).mapNoteToWidget(context, newNote)

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
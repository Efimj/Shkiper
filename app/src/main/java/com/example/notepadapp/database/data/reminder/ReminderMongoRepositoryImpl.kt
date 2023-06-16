package com.example.notepadapp.database.data.reminder

import android.util.Log
import com.example.notepadapp.database.models.Reminder
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class ReminderMongoRepositoryImpl(val realm: Realm) : ReminderMongoRepository {
    override fun getAllReminders(): Flow<List<Reminder>> {
        return realm.query<Reminder>()
            .sort("_id", Sort.DESCENDING)
            .asFlow()
            .map { it.list }
    }

    override fun getReminder(id: ObjectId): Reminder? {
        return realm.query<Reminder>(query = "_id == $0", id).first().find()
    }

    override fun getReminderForNote(noteId: ObjectId): Reminder? {
        return realm.query<Reminder>(query = "noteId == $0", noteId).first().find()
    }

    override fun getRemindersForNotes(noteIds: List<ObjectId>): List<Reminder> {
        return realm.query<Reminder>(query = "noteId IN {${noteIds.joinToString(", ")}}").find()
    }

    override suspend fun insertReminder(reminder: Reminder) {
        realm.write { copyToRealm(reminder) }
    }

    override suspend fun updateReminder(id: ObjectId, updateParams: (Reminder) -> Unit) {
        getReminder(id)?.also { currentReminder ->
            realm.writeBlocking {
                val queriedReminder = findLatest(currentReminder)
                queriedReminder?.apply {
                    updateParams(this)
                }
            }
        }
    }

    override suspend fun updateReminder(
        ids: List<ObjectId>,
        updateParams: (Reminder) -> Unit
    ) {
        realm.writeBlocking {
            for (id in ids) {
                val reminder = getReminder(id) ?: continue
                try {
                    findLatest(reminder)?.let(updateParams)
                } catch (e: Exception) {
                    Log.d("ReminderMongoRepositoryImpl", "${e.message}")
                }
            }
        }
    }

    override suspend fun updateOrCreateReminderForNotes(
        noteIds: List<ObjectId>,
        updateParams: (Reminder) -> Unit
    ) {
        realm.writeBlocking {
            for (id in noteIds) {
                val reminder = getReminderForNote(id)
                if (reminder != null) {
                    try {
                        findLatest(reminder)?.let(updateParams)
                    } catch (e: Exception) {
                        Log.d("ReminderMongoRepositoryImpl", "${e.message}")
                    }
                } else {
                    val newReminder = Reminder()
                    newReminder.noteId = id
                    newReminder.let(updateParams)
                    copyToRealm(newReminder)
                }
            }
        }
    }

    override suspend fun deleteReminder(id: ObjectId) {
        realm.write {
            val reminder = getReminder(id) ?: return@write
            try {
                findLatest(reminder)
                    ?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("ReminderMongoRepositoryImpl", "${e.message}")
            }
        }
    }

    override suspend fun deleteReminder(ids: List<ObjectId>) {
        realm.writeBlocking {
            for (id in ids) {
                val reminder = getReminder(id) ?: continue
                try {
                    findLatest(reminder)
                        ?.let { delete(it) }
                } catch (e: Exception) {
                    Log.d("ReminderMongoRepositoryImpl", "${e.message}")
                }
            }
        }
    }
}
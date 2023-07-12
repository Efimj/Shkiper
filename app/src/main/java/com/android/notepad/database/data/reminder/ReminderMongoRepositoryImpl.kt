package com.android.notepad.database.data.reminder

import android.content.Context
import android.util.Log
import com.android.notepad.R
import com.android.notepad.database.models.Note
import com.android.notepad.database.models.Reminder
import com.android.notepad.notification_service.NotificationData
import com.android.notepad.notification_service.NotificationScheduler
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.OffsetDateTime

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

    override suspend fun insertReminder(reminder: Reminder, note: Note, context: Context) {
        realm.write { copyToRealm(reminder) }
        scheduleNotification(reminder, note, context)
    }

    override suspend fun updateReminder(id: ObjectId, context: Context, updateParams: (Reminder) -> Unit) {
        getReminder(id)?.also { currentReminder ->
            realm.writeBlocking {
                val queriedReminder = findLatest(currentReminder) ?: return@writeBlocking
                queriedReminder.apply {
                    updateParams(this)
                }
                updateNotification(context, id, queriedReminder)
            }
        }
    }

    override suspend fun updateOrCreateReminderForNotes(
        notes: List<Note>,
        context: Context,
        updateParams: (Reminder) -> Unit
    ) {
        realm.writeBlocking {
            for (note in notes) {
                try {
                    var reminder = getReminderForNote(note._id)
                    if (reminder != null) {
                        val latest = findLatest(reminder) ?: continue
                        latest.let(updateParams)
                        reminder = latest
                    } else {
                        reminder = Reminder()
                        reminder.noteId = note._id
                        reminder.let(updateParams)
                        copyToRealm(reminder)
                    }
                    scheduleNotification(reminder, note, context)
                } catch (e: Exception) {
                    Log.d("ReminderMongoRepositoryImpl", "${e.message}")
                }
            }
        }
    }

    override suspend fun deleteReminder(id: ObjectId, context: Context) {
        realm.write {
            val reminder = getReminder(id) ?: return@write
            try {
                findLatest(reminder)?.let { delete(it) }
                deleteNotification(context, id)
            } catch (e: Exception) {
                Log.d("ReminderMongoRepositoryImpl", "${e.message}")
            }
        }
    }

    override suspend fun deleteReminder(ids: List<ObjectId>, context: Context) {
        realm.writeBlocking {
            for (id in ids) {
                val reminder = getReminder(id) ?: continue
                try {
                    findLatest(reminder)
                        ?.let { delete(it) }
                    deleteNotification(context, id)
                } catch (e: Exception) {
                    Log.d("ReminderMongoRepositoryImpl", "${e.message}")
                }
            }
        }
    }

    override suspend fun deleteReminderForNote(noteId: ObjectId, context: Context){
        realm.write {
            val reminder = getReminderForNote(noteId) ?: return@write
            try {
                findLatest(reminder)?.let { delete(it) }
                deleteNotification(context, reminder._id)
            } catch (e: Exception) {
                Log.d("ReminderMongoRepositoryImpl", "${e.message}")
            }
        }
    }

    private fun deleteNotification(context: Context, reminderId: ObjectId) {
        val notificationScheduler = NotificationScheduler(context)
        notificationScheduler.deleteNotification(reminderId.timestamp)
    }

    private fun updateNotification(
        context: Context,
        reminderId: ObjectId,
        queriedReminder: Reminder
    ) {
        // Update notification
        val notificationScheduler = NotificationScheduler(context)
        notificationScheduler.updateNotificationTime(
            reminderId.timestamp,
            queriedReminder.date,
            queriedReminder.time,
            queriedReminder.repeat
        )
    }

    private fun scheduleNotification(
        reminder: Reminder,
        note: Note,
        context: Context
    ) {
        val notificationScheduler = NotificationScheduler(context)
        notificationScheduler.createNotificationChannel(NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL)
        val localDateTime = LocalDateTime.of(reminder.date, reminder.time)
        val notificationData = NotificationData(
            note._id.toHexString(),
            reminder._id.timestamp,
            note.header,
            note.body,
            R.drawable.ic_notification,
            reminder.repeat,
            reminder._id.timestamp,
            localDateTime.toInstant(OffsetDateTime.now().offset).toEpochMilli()
        )
        notificationScheduler.scheduleNotification(notificationData)
    }
}
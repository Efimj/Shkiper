package com.jobik.shkiper.database.data.reminder

import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.Reminder
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface ReminderMongoRepository {
    fun getReminders(): Flow<List<Reminder>>
    fun getReminder(id: ObjectId): Reminder?
    fun getAllReminders(): List<Reminder>
    fun getRemindersForNote(noteId: ObjectId): Flow<List<Reminder>>
    suspend fun insertReminder(reminder: Reminder)
    suspend fun insertOrUpdateReminders(reminders: List<Reminder>, updateStatistics: Boolean = true)
    suspend fun updateReminder(reminderId: ObjectId, note: Note, updateParams: (Reminder) -> Unit)
    suspend fun createReminderForNotes(notes: List<Note>, updateParams: (Reminder) -> Unit)
    suspend fun deleteReminder(id: ObjectId)
    suspend fun deleteReminder(ids: List<ObjectId>)
    suspend fun deleteAllRemindersForNote(noteId: ObjectId)
}
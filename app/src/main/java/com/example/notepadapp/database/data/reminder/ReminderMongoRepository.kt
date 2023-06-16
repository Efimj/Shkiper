package com.example.notepadapp.database.data.reminder

import com.example.notepadapp.database.models.Reminder
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface ReminderMongoRepository {
    fun getAllReminders(): Flow<List<Reminder>>
    fun getReminder(id: ObjectId): Reminder?
    fun getReminderForNote(noteId: ObjectId): Reminder?
    fun getRemindersForNotes(noteIds: List<ObjectId>): List<Reminder>
    suspend fun insertReminder(reminder: Reminder)
    suspend fun updateReminder(id: ObjectId, updateParams: (Reminder) -> Unit)
    suspend fun updateReminder(ids: List<ObjectId>, updateParams: (Reminder) -> Unit)
    suspend fun updateOrCreateReminderForNotes(noteIds: List<ObjectId>, updateParams: (Reminder) -> Unit)
    suspend fun deleteReminder(id: ObjectId)
    suspend fun deleteReminder(ids: List<ObjectId>)
}
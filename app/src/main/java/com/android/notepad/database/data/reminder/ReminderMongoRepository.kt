package com.android.notepad.database.data.reminder

import android.content.Context
import com.android.notepad.database.models.Note
import com.android.notepad.database.models.Reminder
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface ReminderMongoRepository {
    fun getAllReminders(): Flow<List<Reminder>>
    fun getReminder(id: ObjectId): Reminder?
    fun getReminderForNote(noteId: ObjectId): Reminder?
    suspend fun insertReminder(reminder: Reminder, note: Note)
    suspend fun updateReminder(id: ObjectId, updateParams: (Reminder) -> Unit)
    suspend fun updateOrCreateReminderForNotes(notes: List<Note>, updateParams: (Reminder) -> Unit)
    suspend fun deleteReminder(id: ObjectId)
    suspend fun deleteReminder(ids: List<ObjectId>)
    suspend fun deleteReminderForNote(noteId: ObjectId)
}
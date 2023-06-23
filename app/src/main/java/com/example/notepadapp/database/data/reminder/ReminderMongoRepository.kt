package com.example.notepadapp.database.data.reminder

import android.content.Context
import com.example.notepadapp.database.models.Note
import com.example.notepadapp.database.models.Reminder
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface ReminderMongoRepository {
    fun getAllReminders(): Flow<List<Reminder>>
    fun getReminder(id: ObjectId): Reminder?
    fun getReminderForNote(noteId: ObjectId): Reminder?
    suspend fun insertReminder(reminder: Reminder, note: Note, context: Context)
    suspend fun updateReminder(id: ObjectId, context: Context, updateParams: (Reminder) -> Unit)
    suspend fun updateOrCreateReminderForNotes(
        notes: List<Note>,
        context: Context,
        updateParams: (Reminder) -> Unit
    )
    suspend fun deleteReminder(id: ObjectId, context: Context)
    suspend fun deleteReminder(ids: List<ObjectId>, context: Context)
    suspend fun deleteReminderForNote(noteId: ObjectId, context: Context)
}
package com.jobik.shkiper.services.backup

import androidx.annotation.Keep
import com.jobik.shkiper.database.models.Note
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.database.models.NotificationColor
import com.jobik.shkiper.database.models.NotificationIcon
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.services.statistics.StatisticsData
import io.realm.kotlin.ext.toRealmSet
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

@Keep
data class BackupData(
    var noteList: List<NoteBackup> = emptyList(),
    var reminderList: List<ReminderBackup> = emptyList(),
    var userStatistics: StatisticsData = StatisticsData()
) {
    var realmNoteList: List<Note>
        get() {
            val realmNoteList = mutableListOf<Note>()
            for (note in noteList) {
                val realmNote = Note().apply {
                    _id = ObjectId(note.id)
                    header = note.header
                    body = note.body
                    hashtags = note.hashtags.toRealmSet()
                    creationDateString = note.creationDateString
                    updateDateString = note.updateDateString
                    deletionDateString = note.deletionDateString
                    isPinned = note.isPinned
                    linkPreviewEnabled = note.linkPreviewEnabled
                    positionString = note.positionString
                }
                realmNoteList.add(realmNote)
            }
            return realmNoteList
        }
        set(value) {
            noteList = emptyList()
            val newNoteList = mutableListOf<NoteBackup>()
            for (note in value) {
                val noteBackup = NoteBackup(
                    id = note._id.toHexString(),
                    header = note.header,
                    body = note.body,
                    hashtags = note.hashtags.toSet(),
                    creationDateString = note.creationDateString,
                    updateDateString = note.updateDateString,
                    deletionDateString = note.deletionDateString,
                    isPinned = note.isPinned,
                    linkPreviewEnabled = note.linkPreviewEnabled,
                    positionString = note.positionString
                )
                newNoteList.add(noteBackup)
            }
            noteList = newNoteList
        }

    var realmReminderList: List<Reminder>
        get() {
            val realmReminderList = mutableListOf<Reminder>()
            for (reminder in reminderList) {
                val realmReminder = Reminder().apply {
                    _id = ObjectId(reminder.id)
                    noteId = BsonObjectId(reminder.noteId)
                    repeatString = reminder.repeatString
                    dateString = reminder.dateString
                    timeString = reminder.timeString
                    iconString = reminder.iconString
                    colorString = reminder.colorString
                }
                realmReminderList.add(realmReminder)
            }
            return realmReminderList
        }
        set(value) {
            reminderList = emptyList()
            val newReminderList = mutableListOf<ReminderBackup>()
            for (reminder in value) {
                val reminderBackup = ReminderBackup(
                    id = reminder._id.toHexString(),
                    noteId = reminder.noteId.toHexString(),
                    repeatString = reminder.repeatString,
                    dateString = reminder.dateString,
                    timeString = reminder.timeString,
                    iconString = reminder.iconString,
                    colorString = reminder.colorString
                )
                newReminderList.add(reminderBackup)
            }
            reminderList = newReminderList
        }
}

@Keep
data class NoteBackup(
    val id: String = ObjectId.invoke().toHexString(),
    val header: String = "",
    val body: String = "",
    val hashtags: Set<String> = emptySet(),
    val creationDateString: String = "",
    val updateDateString: String = "",
    val deletionDateString: String? = null,
    val isPinned: Boolean = false,
    val linkPreviewEnabled: Boolean = true,
    val positionString: String = NotePosition.MAIN.name,
)

@Keep
data class ReminderBackup(
    var id: String = ObjectId.invoke().toHexString(),
    var noteId: String = ObjectId.invoke().toHexString(),
    var repeatString: String = RepeatMode.NONE.name,
    var dateString: String = "",
    var timeString: String = "",
    var iconString: String = NotificationIcon.EVENT.name,
    var colorString: String = NotificationColor.MATERIAL.name
)
package com.jobik.shkiper.services.notification_service

import android.content.Context
import androidx.annotation.Keep
import com.jobik.shkiper.SharedPreferencesKeys
import com.jobik.shkiper.database.models.RepeatMode
import com.google.gson.Gson

@Keep
class NotificationStorage(context: Context) {
    private val gson = Gson()
    private val sharedPreferences =
        context.getSharedPreferences(SharedPreferencesKeys.NotificationStorageName, Context.MODE_PRIVATE)

    fun getAll(): MutableList<NotificationData> {
        val json = sharedPreferences.getString(SharedPreferencesKeys.NotificationListKey, "[]")
        return gson.fromJson(json, Array<NotificationData>::class.java).toMutableList()
    }

    fun getNotification(requestId: Int): NotificationData? {
        val notifications = getAll()
        return findNotification(notifications, requestId)
    }

    fun getNotificationsForNote(noteId: String): List<NotificationData> {
        val notifications = getAll()
        return findNotificationsForNote(notifications, noteId)
    }

    fun save(notifications: List<NotificationData>) {
        val json = gson.toJson(notifications)
        val editor = sharedPreferences.edit()
        editor.putString(SharedPreferencesKeys.NotificationListKey, json)
        editor.apply()
    }

    fun addOrUpdate(notificationsData: List<NotificationData>) {
        val notifications = getAll()
        addOrUpdateElement(notifications, notificationsData)
        save(notifications)
    }

    fun updateNotificationTime(requestId: Int, trigger: Long, repeatMode: RepeatMode) {
        val notifications = getAll()
        val currentNotifications = notifications.filter { notification -> notification.requestCode == requestId }
        val newNotification = currentNotifications.map {
            it.copy(
                trigger = trigger,
                repeatMode = repeatMode
            )
        }
        addOrUpdate(newNotification)
    }

    fun updateNotificationData(noteId: String, title: String, message: String) {
        val notifications = getAll()
        val currentNotifications = notifications.filter { notification -> notification.noteId == noteId }
        val newNotification = currentNotifications.map {
            it.copy(
                title = title,
                message = message
            )
        }
        addOrUpdate(newNotification)
    }

    fun remove(notificationData: NotificationData) {
        val notifications = getAll()
        removeElement(notifications, notificationData)
        save(notifications)
    }

    fun remove(requestId: Int) {
        val notifications = getAll()
        removeElement(notifications, requestId)
        save(notifications)
    }

    fun remove(noteId: String): Int? {
        val notifications = getAll()
        val requestId = removeElement(notifications, noteId)
        save(notifications)
        return requestId
    }

    private fun findNotification(
        notifications: MutableList<NotificationData>,
        requestId: Int
    ): NotificationData? {
        val index = notifications.indexOfFirst { it.requestCode == requestId } // if exists
        var notification: NotificationData? = null
        if (index != -1) {
            notification = notifications[index]
        }
        return notification
    }

    private fun findNotificationsForNote(
        notifications: MutableList<NotificationData>,
        noteId: String
    ): List<NotificationData> {
        return notifications.filter { it.noteId == noteId }
    }

    private fun removeElement(list: MutableList<NotificationData>, element: NotificationData) {
        val index = list.indexOfFirst { it.requestCode == element.requestCode } // if exists

        if (index != -1) {
            list.removeAt(index)
        }
    }

    private fun removeElement(list: MutableList<NotificationData>, requestId: Int) {
        val index = list.indexOfFirst { it.requestCode == requestId } // if exists

        if (index != -1) {
            list.removeAt(index)
        }
    }

    private fun removeElement(list: MutableList<NotificationData>, noteId: String): Int? {
        val index = list.indexOfFirst { it.noteId == noteId } // if exists
        var requestId: Int? = null
        if (index != -1) {
            requestId = list[index].requestCode
            list.removeAt(index)
        }
        return requestId
    }

    private fun addOrUpdateElement(
        oldNotificationsList: MutableList<NotificationData>,
        newNotificationsList: List<NotificationData>
    ) {
        val newNotificationsMap = newNotificationsList.associateBy { it.requestCode }

        oldNotificationsList.replaceAll { oldNotification ->
            newNotificationsMap[oldNotification.requestCode] ?: oldNotification
        }

        val missingNotifications = newNotificationsList.filterNot { newNotification ->
            oldNotificationsList.any { it.requestCode == newNotification.requestCode }
        }

        oldNotificationsList.addAll(missingNotifications)
    }
}
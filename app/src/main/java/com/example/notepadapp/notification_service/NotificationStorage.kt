package com.example.notepadapp.notification_service

import android.content.Context
import com.google.gson.Gson

class NotificationStorage(private val context: Context) {
    companion object {
        const val reminderStorageName = "ReminderStorage"
        const val reminderListKey = "Reminders"
    }

    private val gson = Gson()
    private val sharedPreferences = context.getSharedPreferences(reminderStorageName, Context.MODE_PRIVATE)

    fun getAll(): MutableList<NotificationData> {
        val json = sharedPreferences.getString(reminderListKey, "[]")
        return gson.fromJson(json, Array<NotificationData>::class.java).toMutableList()
    }

    fun getNotification(requestId: Int): NotificationData? {
        val notifications = getAll()
        return findNotification(notifications, requestId)
    }

    fun save(notifications: List<NotificationData>) {
        val json = gson.toJson(notifications)
        val editor = sharedPreferences.edit()
        editor.putString(reminderListKey, json)
        editor.apply()
    }

    fun addOrUpdate(notificationData: NotificationData) {
        val notifications = getAll()
        addOrUpdateElement(notifications, notificationData)
        save(notifications)
    }

    fun remove(notificationData: NotificationData) {
        val notifications = getAll()
        removeElement(notifications, notificationData)
        save(notifications)
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

    private fun removeElement(list: MutableList<NotificationData>, element: NotificationData) {
        val index = list.indexOfFirst { it.requestCode == element.requestCode } // if exists

        if (index != -1) {
            list.removeAt(index)
        }
    }

    private fun addOrUpdateElement(list: MutableList<NotificationData>, element: NotificationData) {
        val index = list.indexOfFirst { it.requestCode == element.requestCode } // if exists

        if (index != -1) {
            list[index] = element
        } else {
            list.add(element)
        }
    }
}
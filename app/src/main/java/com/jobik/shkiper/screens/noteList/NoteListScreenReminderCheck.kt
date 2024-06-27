package com.jobik.shkiper.screens.noteList

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.NotificationImportant
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.helpers.areChanelNotificationsEnabled
import com.jobik.shkiper.helpers.areEXACTNotificationsEnabled
import com.jobik.shkiper.helpers.areNotificationsEnabled
import com.jobik.shkiper.services.notification.NotificationScheduler
import com.jobik.shkiper.ui.components.modals.ActionDialog

@Composable
fun NoteListScreenReminderCheck(notesViewModel: NotesViewModel) {
    val context = LocalContext.current

    var reminderNeededDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var clickedOnEnabled by rememberSaveable {
        mutableStateOf(false)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        reminderNeededDialog = !checkIsNotificationEnabled(context) && notesViewModel.screenState.value.reminders.isNotEmpty()
    }

    if (reminderNeededDialog)
        ActionDialog(
            dialogProperties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            icon = if (clickedOnEnabled) Icons.Outlined.CheckCircleOutline else Icons.Outlined.NotificationImportant,
            title = if (clickedOnEnabled) stringResource(id = R.string.ImportantForRemindersWork) else stringResource(
                id = R.string.NeededPermissionForNotifications
            ),
            confirmText = if (clickedOnEnabled) stringResource(id = R.string.Confirm) else stringResource(
                id = R.string.Enable
            ),
            onConfirm = {
                clickedOnEnabled = true
                if (!areNotificationsEnabled(context = context) || !areChanelNotificationsEnabled(
                        context = context,
                        channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
                    )
                ) {
                    IntentHelper().startIntentAppNotificationSettings(
                        context = context,
                        channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
                    )
                }
                if (!areEXACTNotificationsEnabled(context = context)) {
                    IntentHelper().startIntentAppEXACTNotificationSettings(context)
                }
                if (checkIsNotificationEnabled(context = context)) {
                    reminderNeededDialog = false
                }
            },
            goBackText = stringResource(id = R.string.Cancel),
            onGoBack = {
                reminderNeededDialog = false
            }
        )

    LaunchedEffect(notesViewModel.screenState.value.reminders.size) {
        if (notesViewModel.screenState.value.reminders.isNotEmpty() && !checkIsNotificationEnabled(
                context = context
            )
        ) {
            reminderNeededDialog = true
        }
    }
}

private fun checkIsNotificationEnabled(context: Context) =
    areNotificationsEnabled(context = context) &&
            areEXACTNotificationsEnabled(context = context) &&
            areChanelNotificationsEnabled(
                context = context,
                channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
            )
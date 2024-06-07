package com.jobik.shkiper.ui.components.modals

import android.content.Context
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.helpers.areChanelNotificationsEnabled
import com.jobik.shkiper.helpers.areEXACTNotificationsEnabled
import com.jobik.shkiper.helpers.areNotificationsEnabled
import com.jobik.shkiper.services.notification.NotificationScheduler
import com.jobik.shkiper.ui.components.buttons.DropDownButton
import com.jobik.shkiper.ui.components.buttons.DropDownButtonSizeMode
import com.jobik.shkiper.ui.components.buttons.DropDownItem
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.cards.SettingsItemColors
import com.jobik.shkiper.ui.components.fields.CustomDatePicker
import com.jobik.shkiper.ui.components.fields.CustomTimePicker
import com.jobik.shkiper.ui.theme.AppTheme
import com.kizitonwose.calendar.compose.ContentHeightMode
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Keep
private enum class ReminderDialogPages(val value: Int) {
    DATEPICK(0), TIMEPICK(1), FINISH(2),
}

@Parcelize
data class ReminderDialogProperties(
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val repeatMode: RepeatMode = RepeatMode.NONE
) : Parcelable

@Parcelize
private data class ReminderDialogState(
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val isNotificationEnabled: Boolean = false,
    val canSave: Boolean = false,
) : Parcelable

@Composable
fun CreateReminderDialog(
    reminderDialogProperties: ReminderDialogProperties = ReminderDialogProperties(),
    onGoBack: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onSave: (date: LocalDate, time: LocalTime, repeatMode: RepeatMode) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        ReminderDialogPages.entries.size
    }

    val reminderDialogState =
        rememberSaveable {
            mutableStateOf(
                ReminderDialogState(
                    date = reminderDialogProperties.date,
                    time = reminderDialogProperties.time,
                    repeatMode = reminderDialogProperties.repeatMode,
                    isNotificationEnabled = checkIsNotificationEnabled(context)
                )
            )
        }

    val goToPage: (Int) -> Unit = {
        scope.launch {
            pagerState.animateScrollToPage(it)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (checkIsNotificationEnabled(context = context)) {
            reminderDialogState.value = reminderDialogState.value.copy(isNotificationEnabled = true)
        } else {
            reminderDialogState.value =
                reminderDialogState.value.copy(isNotificationEnabled = false)
        }
    }

    Dialog(
        onDismissRequest = onGoBack,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(AppTheme.colors.background)
                .padding(vertical = 10.dp)
        ) {
            DialogHeader(
                page = pagerState.currentPage,
                reminderDialogState = reminderDialogState
            )
            HorizontalPager(
                state = pagerState,
                pageSpacing = 0.dp,
                userScrollEnabled = false,
                reverseLayout = false,
                contentPadding = PaddingValues(0.dp),
                pageSize = PageSize.Fill,
            ) {
                DialogContent(
                    page = it,
                    goToPage = goToPage,
                    reminderDialogState = reminderDialogState
                )
            }
            DialogFooter(
                pagerState = pagerState,
                reminderDialogState = reminderDialogState,
                onGoBack = onGoBack,
                onDelete = onDelete,
            ) {
                onSave(
                    reminderDialogState.value.date,
                    reminderDialogState.value.time,
                    reminderDialogState.value.repeatMode
                )
            }
        }
    }
}

@Composable
private fun DialogHeader(
    page: Int,
    reminderDialogState: MutableState<ReminderDialogState>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(targetState = page == 2, label = "AnimatedContent - DialogHeader") {
            if (it) {
                Text(
                    text = stringResource(R.string.Reminder),
                    style = MaterialTheme.typography.headlineMedium,
                    color = AppTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                )
            } else {
                val dateTextColor =
                    animateColorAsState(
                        targetValue = if (page == 0) AppTheme.colors.onSecondaryContainer else AppTheme.colors.textSecondary,
                        label = "dateTextColor"
                    )
                val timeTextColor =
                    animateColorAsState(
                        targetValue = if (page == 1) AppTheme.colors.onSecondaryContainer else AppTheme.colors.textSecondary,
                        label = "timeTextColor"
                    )

                val dateFontWeight =
                    animateIntAsState(
                        targetValue = if (page == 0) FontWeight.SemiBold.weight else FontWeight.Normal.weight,
                        label = "dateTextColor"
                    )

                val timeFontWeight =
                    animateIntAsState(
                        targetValue = if (page == 1) FontWeight.SemiBold.weight else FontWeight.Normal.weight,
                        label = "timeFontWeight"
                    )

                Row(modifier = Modifier.basicMarquee()) {
                    Text(
                        text = DateHelper.getLocalizedDate(reminderDialogState.value.date),
                        style = MaterialTheme.typography.headlineMedium,
                        color = dateTextColor.value,
                        fontWeight = FontWeight(dateFontWeight.value),
                        maxLines = 1,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = reminderDialogState.value.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = MaterialTheme.typography.headlineMedium,
                        color = timeTextColor.value,
                        fontWeight = FontWeight(timeFontWeight.value),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogContent(
    page: Int,
    goToPage: (Int) -> Unit,
    reminderDialogState: MutableState<ReminderDialogState>,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .height(340.dp)
    ) {
        when (page) {
            ReminderDialogPages.DATEPICK.value -> DatePickPage(reminderDialogState = reminderDialogState)
            ReminderDialogPages.TIMEPICK.value -> TimePickPage(reminderDialogState = reminderDialogState)
            ReminderDialogPages.FINISH.value -> FinishPage(
                reminderDialogState = reminderDialogState, goToPage = goToPage
            )
        }
    }
}

@Composable
private fun FinishPage(
    goToPage: (Int) -> Unit,
    reminderDialogState: MutableState<ReminderDialogState>
) {
    val context = LocalContext.current
    val repeatModeList =
        RepeatMode.entries.map { DropDownItem(text = it.getLocalizedValue(context)) }
    val isExpanded = remember { mutableStateOf(false) }
    val isDatePast = DateHelper.isFutureDateTime(
        date = reminderDialogState.value.date,
        time = reminderDialogState.value.time
    ).not()

    Column {
        SettingsItem(
            title = DateHelper.getLocalizedDate(reminderDialogState.value.date),
            icon = Icons.Outlined.Event,
            onClick = { goToPage(0) }
        )
        SettingsItem(
            title = reminderDialogState.value.time.toString(),
            icon = Icons.Outlined.Schedule,
            onClick = { goToPage(1) }
        )
        DropDownButton(
            items = repeatModeList,
            expanded = isExpanded,
            stretchMode = DropDownButtonSizeMode.STRERCHBYBUTTONWIDTH,
            selectedIndex = reminderDialogState.value.repeatMode.ordinal,
            onChangedSelection = {
                reminderDialogState.value =
                    reminderDialogState.value.copy(repeatMode = RepeatMode.entries.toTypedArray()[it])
            }) {
            SettingsItem(
                title = reminderDialogState.value.repeatMode.getLocalizedValue(context = context),
                description = stringResource(R.string.Repeat),
                icon = Icons.Outlined.Repeat,
                onClick = { it() }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedVisibility(
                visible = isDatePast,
                enter = slideInVertically { it / 2 } + expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = slideOutVertically { it / 2 } + shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                SettingsItem(
                    title = stringResource(R.string.ErrorDateMastBeFuture),
                    icon = Icons.Outlined.CalendarMonth,
                    onClick = { goToPage(0) },
                    colors = SettingsItemColors(
                        contentColor = AppTheme.colors.onSecondaryContainer,
                        containerColor = AppTheme.colors.secondaryContainer,
                        leadingIconColor = AppTheme.colors.onSecondaryContainer
                    )
                )
            }
            AnimatedVisibility(
                visible = reminderDialogState.value.isNotificationEnabled.not(),
                enter = slideInVertically { it / 2 } + expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = slideOutVertically { it / 2 } + shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                SettingsItem(
                    title = stringResource(R.string.TurnOnNotifications),
                    icon = Icons.Outlined.NotificationsOff,
                    onClick = {
                        if (!checkIsNotificationEnabled(context = context)) {
                            enableNotificationIfDisabled(context = context)
                        } else {
                            reminderDialogState.value =
                                reminderDialogState.value.copy(isNotificationEnabled = true)
                        }
                    },
                    colors = SettingsItemColors(
                        contentColor = AppTheme.colors.onSecondaryContainer,
                        containerColor = AppTheme.colors.secondaryContainer,
                        leadingIconColor = AppTheme.colors.onSecondaryContainer
                    )
                )
            }
        }
    }
}

@Composable
private fun TimePickPage(
    reminderDialogState: MutableState<ReminderDialogState>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomTimePicker(startTime = reminderDialogState.value.time) { newTime ->
            reminderDialogState.value = reminderDialogState.value.copy(time = newTime)
        }
    }
}

@Composable
private fun DatePickPage(reminderDialogState: MutableState<ReminderDialogState>) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomDatePicker(
            currentDate = reminderDialogState.value.date,
            contentHeightMode = ContentHeightMode.Wrap
        ) { day ->
            reminderDialogState.value = reminderDialogState.value.copy(date = day.date)
        }
    }
}

@Composable
private fun DialogFooter(
    pagerState: PagerState,
    reminderDialogState: MutableState<ReminderDialogState>,
    onGoBack: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onSave: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val isEnd = pagerState.currentPage == ReminderDialogPages.entries.size - 1

    val isFutureDate =
        remember(reminderDialogState.value.date, reminderDialogState.value.time) {
            mutableStateOf(
                DateHelper.isFutureDateTime(
                    date = reminderDialogState.value.date,
                    time = reminderDialogState.value.time
                )
            )
        }

    val buttonNextContentColor: Color by animateColorAsState(
        targetValue = if (isEnd && reminderDialogState.value.isNotificationEnabled && isFutureDate.value) AppTheme.colors.onPrimary else AppTheme.colors.text,
        label = "buttonNextContentColor"
    )

    val buttonNextBackgroundColor: Color by animateColorAsState(
        targetValue = if (isEnd && reminderDialogState.value.isNotificationEnabled && isFutureDate.value) AppTheme.colors.primary else AppTheme.colors.container,
        label = "buttonNextBackgroundColor"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(
            20.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = onDelete != null,
            enter = slideInHorizontally() + expandHorizontally(clip = false) + fadeIn(),
            exit = slideOutHorizontally() + shrinkHorizontally(clip = false) + fadeOut(),
        ) {
            Button(
                modifier = Modifier.fillMaxHeight(),
                shape = AppTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = AppTheme.colors.text,
                    containerColor = AppTheme.colors.container
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = {
                    if (onDelete != null)
                        coroutineScope.launch {
                            onDelete()
                        }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.NotificationsOff,
                    contentDescription = stringResource(R.string.Delete),
                    tint = AppTheme.colors.text
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(.5f),
                shape = AppTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = AppTheme.colors.text,
                    containerColor = AppTheme.colors.container
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage > 0) pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        else {
                            onGoBack()
                        }
                    }
                },
            ) {
                AnimatedContent(
                    targetState = pagerState.currentPage > 0,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { height -> height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> -height } + fadeOut())
                        } else {
                            (slideInHorizontally { height -> -height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> height } + fadeOut())
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }, label = ""
                ) { value ->
                    if (value) {
                        Text(
                            text = stringResource(R.string.Back),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colors.text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.Cancel),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.colors.text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(.5f),
                shape = AppTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    contentColor = buttonNextContentColor,
                    containerColor = buttonNextBackgroundColor
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = {
                    if (isEnd && isFutureDate.value) {
                        if (!checkIsNotificationEnabled(context = context) && !reminderDialogState.value.isNotificationEnabled) {
                            enableNotificationIfDisabled(context = context)
                        } else {
                            reminderDialogState.value =
                                reminderDialogState.value.copy(isNotificationEnabled = true)
                            onSave()
                        }
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
            ) {
                AnimatedContent(
                    targetState = isEnd,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { height -> height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> -height } + fadeOut())
                        } else {
                            (slideInHorizontally { height -> -height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> height } + fadeOut())
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }, label = ""
                ) { value ->
                    if (value) {
                        Text(
                            text = stringResource(R.string.Save),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = buttonNextContentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.Next),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = buttonNextContentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
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

fun enableNotificationIfDisabled(context: Context) {
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
}
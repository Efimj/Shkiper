package com.jobik.shkiper.ui.components.modals

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.*
import com.jobik.shkiper.screens.OnboardingScreen.OnBoardingPage
import com.jobik.shkiper.services.notification_service.NotificationScheduler
import com.jobik.shkiper.ui.components.buttons.*
import com.jobik.shkiper.ui.components.fields.CustomDatePicker
import com.jobik.shkiper.ui.components.fields.CustomTimePicker
import com.jobik.shkiper.ui.theme.CustomTheme
import com.kizitonwose.calendar.compose.ContentHeightMode
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime


private enum class ReminderDialogPages(val value: Int) {
    DATEPICK(0), TIMEPICK(1), REPEATMODE(2),
}

data class ReminderDialogProperties(
    var date: LocalDate = LocalDate.now(),
    var time: LocalTime = LocalTime.now(),
    var repeatMode: RepeatMode = RepeatMode.NONE
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateReminderDialog(
    reminderDialogProperties: ReminderDialogProperties = ReminderDialogProperties(),
    onGoBack: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onSave: (date: LocalDate, time: LocalTime, repeatMode: RepeatMode) -> Unit,
) {
    val context = LocalContext.current
    val date = rememberSaveable { mutableStateOf(reminderDialogProperties.date) }
    val time = rememberSaveable { mutableStateOf(reminderDialogProperties.time) }
    val repeatMode = rememberSaveable { mutableStateOf(reminderDialogProperties.repeatMode) }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        ReminderDialogPages.entries.size
    }
    val isNotificationEnabled = remember {
        mutableStateOf(checkIsNotificationEnabled(context))
    }

    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(CustomTheme.colors.mainBackground)
                .padding(vertical = 20.dp)
        ) {
            HorizontalPager(
                modifier = Modifier,
                state = pagerState,
                pageSpacing = 0.dp,
                userScrollEnabled = true,
                reverseLayout = false,
                contentPadding = PaddingValues(0.dp),
                beyondBoundsPageCount = 0,
                pageSize = PageSize.Fill,
            ) {
                DialogContent(
                    it = it,
                    date = date,
                    time = time,
                    repeatMode = repeatMode,
                    isNotificationEnabled = isNotificationEnabled
                )
            }
            DialogFooter(
                pagerState = pagerState,
                onGoBack = onGoBack,
                onDelete = onDelete,
                isNotificationEnabled = isNotificationEnabled
            ) {
                onSave(date.value, time.value, repeatMode.value)
            }
        }
    }
}

private fun checkIsNotificationEnabled(context: Context) = areNotificationsEnabled(context = context) &&
        areEXACTNotificationsEnabled(context = context) &&
        areChanelNotificationsEnabled(
            context = context,
            channelId = NotificationScheduler.Companion.NotificationChannels.NOTECHANNEL.channelId
        )

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DialogFooter(
    pagerState: PagerState,
    onGoBack: () -> Unit,
    onDelete: (() -> Unit)? = null,
    isNotificationEnabled: MutableState<Boolean>,
    onSave: () -> Unit,
) {
    val isEnd = pagerState.currentPage == ReminderDialogPages.entries.size - 1
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val buttonNextContentColor: Color by animateColorAsState(
        targetValue = if (isEnd) CustomTheme.colors.textOnActive else CustomTheme.colors.text,
        label = "buttonNextContentColor"
    )

    val buttonNextBackgroundColor: Color by animateColorAsState(
        targetValue = if (isEnd) CustomTheme.colors.active else CustomTheme.colors.secondaryBackground,
        label = "buttonNextBackgroundColor"
    )

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = onDelete != null,
            enter = slideInHorizontally() + expandHorizontally(clip = false) + fadeIn(),
            exit = slideOutHorizontally() + shrinkHorizontally(clip = false) + fadeOut(),
        )
        {
            Button(
                modifier = Modifier.fillMaxHeight(),
                shape = CustomTheme.shapes.small,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    contentColor = CustomTheme.colors.text,
                    containerColor = CustomTheme.colors.secondaryBackground
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
                    tint = CustomTheme.colors.text
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterHorizontally)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(.5f),
                shape = CustomTheme.shapes.small,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    contentColor = CustomTheme.colors.text,
                    containerColor = CustomTheme.colors.secondaryBackground
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
                            (slideInHorizontally { height -> height } + fadeIn()).togetherWith(slideOutHorizontally { height -> -height } + fadeOut())
                        } else {
                            (slideInHorizontally { height -> -height } + fadeIn()).togetherWith(slideOutHorizontally { height -> height } + fadeOut())
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }, label = ""
                ) { value ->
                    if (value) {
                        Text(
                            text = stringResource(R.string.Back),
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold,
                            color = CustomTheme.colors.text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.Cancel),
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold,
                            color = CustomTheme.colors.text,
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
                shape = CustomTheme.shapes.small,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    contentColor = buttonNextContentColor,
                    containerColor = buttonNextBackgroundColor
                ),
                border = null,
                elevation = null,
                contentPadding = PaddingValues(horizontal = 15.dp),
                onClick = {
                    if (isEnd) {
                        if (!isNotificationEnabled.value && !checkIsNotificationEnabled(context = context)) {
                            enableNotificationIfDisabled(context = context)
                        } else {
                            isNotificationEnabled.value = true
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
                            (slideInHorizontally { height -> height } + fadeIn()).togetherWith(slideOutHorizontally { height -> -height } + fadeOut())
                        } else {
                            (slideInHorizontally { height -> -height } + fadeIn()).togetherWith(slideOutHorizontally { height -> height } + fadeOut())
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }, label = ""
                ) { value ->
                    if (value) {
                        Text(
                            text = stringResource(R.string.Save),
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold,
                            color = buttonNextContentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.Next),
                            style = MaterialTheme.typography.body1,
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

@Composable
private fun DialogContent(
    it: Int,
    date: MutableState<LocalDate>,
    time: MutableState<LocalTime>,
    repeatMode: MutableState<RepeatMode>,
    isNotificationEnabled: State<Boolean>
) {
    Column(Modifier.height(340.dp)) {
        when (it) {
            ReminderDialogPages.DATEPICK.value -> DatePickPage(date)

            ReminderDialogPages.TIMEPICK.value -> TimePickPage(date, time)

            ReminderDialogPages.REPEATMODE.value -> RepeatModePage(date, time, repeatMode, isNotificationEnabled)
        }
    }
}

@Composable
private fun RepeatModePage(
    date: MutableState<LocalDate>,
    time: MutableState<LocalTime>,
    repeatMode: MutableState<RepeatMode>,
    isNotificationEnabled: State<Boolean>
) {
    val repeatModeList = RepeatMode.entries.map { DropDownItem(text = it.getLocalizedValue(LocalContext.current)) }
    val isExpanded = remember { mutableStateOf(false) }
    val isDatePast = !DateHelper.isFutureDateTime(date.value, time.value)

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            stringResource(R.string.Reminder),
            style = MaterialTheme.typography.h5,
            color = CustomTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .fillMaxWidth()
        )
        Column(horizontalAlignment = Alignment.Start) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                Icon(
                    tint = CustomTheme.colors.textSecondary,
                    imageVector = Icons.Default.Event,
                    contentDescription = stringResource(R.string.Event)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    DateHelper.getLocalizedDate(date.value),
                    style = MaterialTheme.typography.body1,
                    color = CustomTheme.colors.text,
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                Icon(
                    tint = CustomTheme.colors.textSecondary,
                    imageVector = Icons.Default.Schedule,
                    contentDescription = stringResource(R.string.Schedule)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = time.value.toString(),
                    style = MaterialTheme.typography.body1,
                    color = CustomTheme.colors.text,
                )
            }
            if (isDatePast)
                Text(
                    text = stringResource(R.string.ErrorDateMastBeFuture),
                    style = MaterialTheme.typography.body1,
                    color = CustomTheme.colors.active,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(38.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    tint = CustomTheme.colors.textSecondary,
                    imageVector = Icons.Default.Repeat,
                    contentDescription = stringResource(R.string.Repeat)
                )
                Spacer(Modifier.width(8.dp))
                DropDownButton(
                    items = repeatModeList,
                    expanded = isExpanded,
                    selectedIndex = repeatMode.value.ordinal,
                    onChangedSelection = { repeatMode.value = RepeatMode.values()[it] }) {
                    CustomButton(
                        text = repeatMode.value.getLocalizedValue(LocalContext.current),
                        onClick = { it() },
                        style = ButtonStyle.Text
                    )
                }
            }
            if (!isNotificationEnabled.value)
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                    Row(
                        modifier = Modifier.padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            tint = CustomTheme.colors.active,
                            imageVector = Icons.Default.Warning,
                            contentDescription = stringResource(R.string.TurnOnNotifications)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.TurnOnNotifications),
                            style = MaterialTheme.typography.body1,
                            color = CustomTheme.colors.text,
                        )
                    }
                }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimePickPage(
    date: MutableState<LocalDate>,
    time: MutableState<LocalTime>,
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .basicMarquee()
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                DateHelper.getLocalizedDate(date.value),
                style = MaterialTheme.typography.h5,
                color = CustomTheme.colors.textSecondary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                time.value.toString(),
                style = MaterialTheme.typography.h5,
                color = CustomTheme.colors.text,
            )
        }
        Spacer(Modifier.height(20.dp))
        CustomTimePicker(time.value) { newTime -> time.value = newTime }
    }
}

@Composable
private fun DatePickPage(date: MutableState<LocalDate>) {
    Column(Modifier.padding(horizontal = 20.dp)) {
        Text(
            DateHelper.getLocalizedDate(date.value),
            style = MaterialTheme.typography.h5,
            color = CustomTheme.colors.text
        )
        Spacer(Modifier.height(10.dp))
        CustomDatePicker(
            date.value, ContentHeightMode.Wrap
        ) { day -> date.value = day.date }
    }
}
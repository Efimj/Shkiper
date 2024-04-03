package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.CustomTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReminderCard(reminder: Reminder, isSelected: Boolean, onClick: () -> Unit, onLongClick: () -> Unit) {
    val context = LocalContext.current
    val nextReminderDate =
        DateHelper.nextDateWithRepeating(LocalDateTime.of(reminder.date, reminder.time), reminder.repeat)
    val isDateFuture = DateHelper.isFutureDateTime(nextReminderDate)
    val isRepeatable = reminder.repeat != RepeatMode.NONE

    val backgroundColor by animateColorAsState(
        if (isDateFuture) CustomTheme.colors.container else CustomTheme.colors.background,
        label = "ReminderCardBackgroundColor",
    )

    val strokeColorValue = when {
        isSelected -> CustomTheme.colors.primary
        isDateFuture -> CustomTheme.colors.container
        else -> CustomTheme.colors.border
    }
    val strokeColor by animateColorAsState(
        strokeColorValue,
        label = "ReminderCardStrokeColor",
    )

    val contentColor by animateColorAsState(
        if (isDateFuture && isDateFuture) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
        label = "ReminderCardStrokeColor",
    )

    Card(
        shape = CustomTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = CustomTheme.colors.text,
            disabledContainerColor = backgroundColor,
            disabledContentColor = CustomTheme.colors.text
        ),
        border = BorderStroke(width = 2.dp, strokeColor),
        elevation = CardDefaults.outlinedCardElevation(),
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(0.95f)
            .clip(CustomTheme.shapes.medium)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isRepeatable) Icons.Outlined.Repeat else Icons.Outlined.Event,
                    contentDescription = stringResource(id = R.string.Repeat),
                    modifier = Modifier.size(35.dp),
                    tint = CustomTheme.colors.textSecondary
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp, alignment = Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = nextReminderDate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.h6,
                        color = contentColor,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .basicMarquee(),
                        text = DateHelper.getLocalizedDate(nextReminderDate.toLocalDate()),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.h6,
                        color = contentColor,
                    )
                }
                AnimatedVisibility(
                    visible = isRepeatable,
                    enter = slideInVertically() + expandVertically() + fadeIn(),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut(),
                ) {
                    Text(
                        text = reminder.repeat.getLocalizedValue(context = context),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body2,
                        color = CustomTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}
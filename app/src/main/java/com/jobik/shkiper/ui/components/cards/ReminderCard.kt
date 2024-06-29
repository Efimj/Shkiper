package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.database.models.Reminder
import com.jobik.shkiper.database.models.RepeatMode
import com.jobik.shkiper.helpers.DateHelper
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReminderCard(
    reminder: Reminder,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val context = LocalContext.current
    val nextReminderDate =
        DateHelper.nextDateWithRepeating(
            LocalDateTime.of(reminder.date, reminder.time),
            reminder.repeat
        )
    val isDateFuture = DateHelper.isFutureDateTime(nextReminderDate)
    val isRepeatable = reminder.repeat != RepeatMode.NONE

    val backgroundColor by animateColorAsState(
        if (isDateFuture) AppTheme.colors.container else AppTheme.colors.background,
        label = "ReminderCardBackgroundColor",
    )

    val strokeColorValue = when {
        isSelected -> AppTheme.colors.primary
        isDateFuture -> AppTheme.colors.container
        else -> AppTheme.colors.border
    }
    val strokeColor by animateColorAsState(
        strokeColorValue,
        label = "ReminderCardStrokeColor",
    )

    val contentColor by animateColorAsState(
        if (isDateFuture && isDateFuture) AppTheme.colors.text else AppTheme.colors.textSecondary,
        label = "ReminderCardStrokeColor",
    )

    Card(
        shape = AppTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = AppTheme.colors.text,
            disabledContainerColor = backgroundColor,
            disabledContentColor = AppTheme.colors.text
        ),
        border = BorderStroke(width = 2.dp, strokeColor),
        elevation = CardDefaults.outlinedCardElevation(),
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(0.95f)
            .clip(AppTheme.shapes.medium)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp)
                .heightIn(min = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(reminder.icon.getDrawable()),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(AppTheme.colors.textSecondary)
                )
            }
            Column(
                modifier = Modifier.padding(end = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        20.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = nextReminderDate.toLocalTime()
                            .format(DateTimeFormatter.ofPattern("HH:mm")),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .basicMarquee(),
                        text = DateHelper.getLocalizedDate(nextReminderDate.toLocalDate()),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
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
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}
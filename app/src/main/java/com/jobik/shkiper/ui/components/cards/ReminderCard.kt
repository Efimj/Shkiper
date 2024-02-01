package com.jobik.shkiper.ui.components.cards

import androidx.annotation.Keep
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.layout.ContentScale
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
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReminderCard(reminder: Reminder, onClick: () -> Unit) {
    val context = LocalContext.current
    val nextReminderDate = DateHelper.nextDateWithRepeating(reminder.date, reminder.time, reminder.repeat)
    val isDateFuture = DateHelper.isFutureDateTime(nextReminderDate)
    val isRepeatable = reminder.repeat != RepeatMode.NONE

    val backgroundColor by animateColorAsState(
        if (isDateFuture) CustomTheme.colors.secondaryBackground else CustomTheme.colors.mainBackground,
        label = "ReminderCardBackgroundColor",
    )

    val strokeColor by animateColorAsState(
        if (isDateFuture) CustomTheme.colors.secondaryBackground else CustomTheme.colors.stroke,
        label = "ReminderCardStrokeColor",
    )

    val contentColor by animateColorAsState(
        if (isDateFuture && isDateFuture) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
        label = "ReminderCardStrokeColor",
    )

    val iconSize by animateDpAsState(
        if (isRepeatable) 25.dp else 35.dp,
        label = "iconSize",
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
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.heightIn(min = 40.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isRepeatable) Icons.Outlined.Repeat else Icons.Outlined.Event,
                    contentDescription = stringResource(id = R.string.Repeat),
                    modifier = Modifier.size(iconSize),
                    tint = contentColor
                )
                if (isRepeatable)
                    Text(
                        text = reminder.repeat.getLocalizedValue(context = context),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body1,
                        color = CustomTheme.colors.textSecondary,
                    )
            }
            Row(
                modifier = Modifier.basicMarquee()
            ) {
                Text(
                    text = DateHelper.getLocalizedDate(nextReminderDate.toLocalDate()),
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6,
                    color = contentColor,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = nextReminderDate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6,
                    color = contentColor,
                )
            }
        }
    }
}
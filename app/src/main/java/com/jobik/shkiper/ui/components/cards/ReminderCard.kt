package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReminderCard(reminder: Reminder, onClick: () -> Unit) {
    val context = LocalContext.current
    val nextReminderDate = DateHelper.nextDateWithRepeating(reminder.date, reminder.time, reminder.repeat)
    val isDateFuture = DateHelper.isFutureDateTime(nextReminderDate)

    val backgroundColor by animateColorAsState(
        if (isDateFuture) CustomTheme.colors.secondaryBackground else CustomTheme.colors.mainBackground,
        label = "ReminderCardBackgroundColor",
    )

    Card(
        shape = CustomTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = CustomTheme.colors.text,
            disabledContainerColor = backgroundColor,
            disabledContentColor = CustomTheme.colors.text
        ),
        border = BorderStroke(width = 1.dp, CustomTheme.colors.secondaryBackground),
        elevation = CardDefaults.outlinedCardElevation(),
        modifier = Modifier
            .bounceClick(0.95f)
            .clip(CustomTheme.shapes.medium)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(modifier = Modifier.heightIn(min = 40.dp)) {
                Image(
                    imageVector = if (reminder.repeat == RepeatMode.NONE) Icons.Outlined.Event else Icons.Outlined.Repeat,
                    contentDescription = stringResource(
                        id = R.string.Repeat
                    ),
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()
                )
                if (reminder.repeat != RepeatMode.NONE)
                    Text(
                        text = reminder.repeat.getLocalizedValue(context = context),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body1,
                        color = CustomTheme.colors.textSecondary,
                    )
            }
            Text(
                text = DateHelper.getLocalizedDate(nextReminderDate.toLocalDate()),
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6,
                color = CustomTheme.colors.textSecondary,
                modifier = Modifier.basicMarquee()
            )

        }
    }
}
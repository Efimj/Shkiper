package com.jobik.shkiper.ui.components.layouts

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.CustomTheme
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import java.time.LocalDate

@Composable
fun CalendarDayView(day: CalendarDay, currentDate: LocalDate, onClick: (CalendarDay) -> Unit) {
    val dateNow = LocalDate.now()
    val isDateCurrentOrFuture = !day.date.isBefore(dateNow)
    val borderCornerShape = RoundedCornerShape(12.dp)

    val targetBorderColorValue = when {
        currentDate == day.date -> CustomTheme.colors.active
        day.date == dateNow -> CustomTheme.colors.stroke
        else -> Color.Transparent
    }
    val borderColor by animateColorAsState(targetValue = targetBorderColorValue, label = "borderColor")

    if (day.position == DayPosition.MonthDate)
        Box(
            modifier = Modifier
                .clip(borderCornerShape)
                .border(
                    BorderStroke(
                        2.dp,
                        borderColor
                    ),
                    borderCornerShape
                )
                .aspectRatio(1f)
                .clickable(
                    enabled = isDateCurrentOrFuture,
                    onClick = { onClick(day) }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = MaterialTheme.typography.body1,
                color =
                if (isDateCurrentOrFuture) CustomTheme.colors.text
                else CustomTheme.colors.textSecondary,
            )
        }
}

@Composable
fun CalendarDayView(
    modifier: Modifier = Modifier,
    day: CalendarDay,
    isSelected: Boolean,
    showIndicator: Boolean = false,
    onClick: (CalendarDay) -> Unit
) {
    val dateNow = LocalDate.now()
    val isDateCurrentOrFuture = !day.date.isBefore(dateNow)

    if (day.position == DayPosition.MonthDate)
        CalendarDayContent(
            modifier = modifier,
            isToday = dateNow.isEqual(day.date),
            isSelected = isSelected,
            enabled = isDateCurrentOrFuture,
            onClick = { onClick(day) },
            day = day.date,
            showIndicator = showIndicator
        )
}

@Composable
fun CalendarDayView(
    modifier: Modifier = Modifier,
    day: WeekDay,
    isSelected: Boolean,
    showIndicator: Boolean = false,
    onClick: (WeekDay) -> Unit
) {
    val dateNow = LocalDate.now()
    val isDateCurrentOrFuture = !day.date.isBefore(dateNow)

    if (day.position == WeekDayPosition.RangeDate)
        CalendarDayContent(
            modifier = modifier,
            isToday = dateNow.isEqual(day.date),
            isSelected = isSelected,
            enabled = isDateCurrentOrFuture,
            onClick = { onClick(day) },
            day = day.date,
            showIndicator = showIndicator
        )

}

@Composable
private fun CalendarDayContent(
    modifier: Modifier = Modifier,
    isToday: Boolean,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    day: LocalDate,
    showIndicator: Boolean = false
) {
    val targetBackgroundColorValue = when {
        isSelected -> CustomTheme.colors.active
        isToday -> CustomTheme.colors.stroke
        else -> Color.Transparent
    }
    val borderCornerShape = RoundedCornerShape(12.dp)
    val backgroundColor by animateColorAsState(targetValue = targetBackgroundColorValue, label = "backgroundColor")

    Column(
        modifier = modifier
            .clip(borderCornerShape)
            .background(backgroundColor)
            .aspectRatio(1f)
            .clickable(
                enabled = enabled,
                onClick = { onClick() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(Color.Green),
        ) {

        }
        Text(
            text = day.dayOfMonth.toString(),
            style = MaterialTheme.typography.body1,
            color = if (enabled) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showIndicator) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(CustomTheme.colors.active, CircleShape)
                )
            }
        }
    }
}
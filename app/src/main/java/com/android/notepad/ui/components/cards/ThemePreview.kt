package com.android.notepad.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.notepad.ui.modifiers.bounceClick
import com.android.notepad.ui.theme.ExtendedColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemePreview(
    colors: ExtendedColors,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .bounceClick()
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(onClick = onClick),
        elevation = 0.dp,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(if (selected) 2.dp else 0.dp, if (selected) colors.active else Color.Transparent),
        backgroundColor = colors.mainBackground,
        contentColor = colors.text,
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.stroke)) {}
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.textSecondary)) {}
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.active)) {}
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.secondaryBackground)) {}
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.mainBackground)) {}
        }
    }
}
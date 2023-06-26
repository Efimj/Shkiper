package com.example.notepadapp.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.theme.CustomAppTheme

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HashtagButton(chip: String, selected: Boolean, onChipClicked: (String) -> Unit) {
    Chip(
        modifier = Modifier
            .padding(end = 8.dp),
        onClick = { onChipClicked(chip) },
        shape = RoundedCornerShape(10.dp),
        colors = ChipDefaults.chipColors(
            backgroundColor = if (selected) CustomAppTheme.colors.active else CustomAppTheme.colors.secondaryBackground,
            contentColor = if (selected) Color.White else CustomAppTheme.colors.text
        ),
        border = BorderStroke(1.dp, CustomAppTheme.colors.stroke)
    ) {
        Text(
            chip,
            modifier = Modifier.basicMarquee().padding(8.dp),
            maxLines = 1,
            style = MaterialTheme.typography.body1,
            color = if (selected) Color.White else CustomAppTheme.colors.text
        )
    }
}
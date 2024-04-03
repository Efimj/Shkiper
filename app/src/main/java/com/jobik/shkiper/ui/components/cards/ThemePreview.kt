package com.jobik.shkiper.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.CustomThemeColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemePreview(
    colors: CustomThemeColors,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Card(
        modifier = Modifier
            .bounceClick()
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(onClick = { multipleEventsCutter.processEvent { onClick() } }),
        elevation = 0.dp,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(if (selected) 2.dp else 0.dp, if (selected) colors.primary else Color.Transparent),
        backgroundColor = colors.background,
        contentColor = colors.text,
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.border)) {}
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.textSecondary)) {}
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.primary)) {}
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.container)) {}
            Row(Modifier.fillMaxWidth().weight(1f).background(colors.background)) {}
        }
    }
}
package com.jobik.shkiper.ui.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.CustomThemeColors

@Composable
fun ThemePreview(
    colors: CustomThemeColors,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .weight(1f)
                    .background(colors.primary)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {}
            }
            Row(Modifier.weight(1f)) {
                Box(
                    Modifier
                        .weight(1f)
                        .background(colors.container)
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {}
                }
                Box(
                    Modifier
                        .weight(1f)
                        .background(colors.background)
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {}
                }
            }
        }
        AnimatedVisibility(visible = selected, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(colors.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = colors.onSecondaryContainer
                )
            }
        }
    }

//    Card(
//        modifier = Modifier
//            .bounceClick()
//            .fillMaxSize()
//            .clip(RoundedCornerShape(10.dp))
//            .combinedClickable(onClick = { multipleEventsCutter.processEvent { onClick() } }),
//        elevation = 0.dp,
//        shape = RoundedCornerShape(10.dp),
//        border = BorderStroke(if (selected) 2.dp else 0.dp, if (selected) colors.primary else Color.Transparent),
//        backgroundColor = colors.background,
//        contentColor = colors.text,
//    ) {
//        Column(Modifier.fillMaxSize()) {
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .background(colors.border)) {}
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .background(colors.textSecondary)) {}
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .background(colors.primary)) {}
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .background(colors.container)) {}
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .background(colors.background)) {}
//        }
//    }
}
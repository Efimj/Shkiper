package com.example.notepadapp.ui.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadapp.R
import com.example.notepadapp.helpers.LinkHelper
import com.example.notepadapp.ui.components.buttons.RoundedButton
import com.example.notepadapp.ui.theme.CustomAppTheme

@Composable
fun LinkPreviewList(linkPreviewList: Set<LinkHelper.LinkPreview>, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier) {
        Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)){
            Text(
                stringResource(R.string.Links),
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
                color = CustomAppTheme.colors.textSecondary,
            )
        }
        linkPreviewList.take(3).forEach { linkPreview ->
            LinkPreviewCard(linkPreview)
            Spacer(Modifier.height(8.dp))
        }
        AnimatedVisibility(visible = linkPreviewList.size > 3 && expanded) {
            Column {
                linkPreviewList.drop(3).forEach { linkPreview ->
                    LinkPreviewCard(linkPreview)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        if (linkPreviewList.size > 3) {
            RoundedButton(
                text = if (expanded) stringResource(R.string.Hide) else stringResource(R.string.ShowAll),
                onClick = { expanded = !expanded },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    disabledBackgroundColor = Color.Transparent
                ),
                textColor = CustomAppTheme.colors.textSecondary
            )
        }
    }
}
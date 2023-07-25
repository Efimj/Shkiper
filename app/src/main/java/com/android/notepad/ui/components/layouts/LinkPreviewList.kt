package com.android.notepad.ui.components.layouts

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Loop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.notepad.R
import com.android.notepad.helpers.LinkHelper
import com.android.notepad.ui.components.buttons.RoundedButton
import com.android.notepad.ui.components.cards.LinkPreviewCard
import com.android.notepad.ui.modifiers.circularRotation
import com.android.notepad.ui.theme.CustomAppTheme

fun LazyListScope.LinkPreviewList(
    linkPreviewList: Set<LinkHelper.LinkPreview>,
    expanded: MutableState<Boolean>,
    isLoading: Boolean,
    contentPadding: Modifier
) {
    if (isLoading) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = contentPadding) {
                Text(
                    text = stringResource(R.string.Loading),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
                    color = CustomAppTheme.colors.textSecondary,
                )
                Spacer(Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Outlined.Loop,
                    contentDescription = stringResource(R.string.Loading),
                    tint = CustomAppTheme.colors.textSecondary,
                    modifier = Modifier.circularRotation()
                )
            }
        }
        return
    }
    if (linkPreviewList.isEmpty()) return
    item {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = contentPadding.padding(bottom = 10.dp)) {
            Text(
                stringResource(R.string.Links),
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
                color = CustomAppTheme.colors.textSecondary,
            )
        }
    }
    items(if (linkPreviewList.size > 3 && expanded.value) linkPreviewList.toList() else linkPreviewList.take(3)) { linkPreview ->
        Box(contentPadding.padding(bottom = 8.dp)) {
            LinkPreviewCard(linkPreview)
        }
    }
    if (linkPreviewList.size > 3) {
        item {
            RoundedButton(
                text = if (expanded.value) stringResource(R.string.Hide) else stringResource(R.string.ShowAll),
                onClick = { expanded.value = !expanded.value },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    disabledBackgroundColor = Color.Transparent
                ),
                textColor = CustomAppTheme.colors.textSecondary,
                modifier = contentPadding
            )
        }
    }
}
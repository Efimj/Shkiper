package com.jobik.shkiper.ui.components.layouts

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.LinkHelper
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.components.cards.LinkPreviewCard
import com.jobik.shkiper.ui.modifiers.circularRotation
import com.jobik.shkiper.ui.theme.CustomTheme

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
                    color = CustomTheme.colors.textSecondary,
                )
                Spacer(Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Outlined.Loop,
                    contentDescription = stringResource(R.string.Loading),
                    tint = CustomTheme.colors.textSecondary,
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
                color = CustomTheme.colors.textSecondary,
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
            CustomButton(
                text = if (expanded.value) stringResource(R.string.Hide) else stringResource(R.string.ShowAll),
                onClick = { expanded.value = !expanded.value },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    disabledBackgroundColor = Color.Transparent
                ),
                textColor = CustomTheme.colors.textSecondary,
                modifier = contentPadding
            )
        }
    }
}
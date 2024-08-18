package com.jobik.shkiper.ui.components.layouts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.LinkPreview
import com.jobik.shkiper.ui.components.cards.LinkPreviewCard
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun LinkPreviewList(
    linkPreviewList: Set<LinkPreview>,
    expanded: MutableState<Boolean>,
    isLoading: Boolean,
    contentPadding: PaddingValues
) {
    val links = remember(expanded.value, linkPreviewList) {
        if (linkPreviewList.size > 3 && expanded.value) linkPreviewList.toList() else linkPreviewList.take(
            3
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .animateContentSize()
    ) {
        AnimatedVisibility(visible = isLoading) {
            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(modifier = Modifier.size(30.dp), color = AppTheme.colors.primary)
//                Spacer(Modifier.width(10.dp))
//                Text(
//                    text = stringResource(R.string.Loading),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    style = MaterialTheme.typography.titleMedium,
//                    color = AppTheme.colors.textSecondary,
//                )
            }
        }
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = isLoading.not() && linkPreviewList.isNotEmpty()
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.Links),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.textSecondary,
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    links.forEach { link ->
                        LinkPreviewCard(link)
                    }
                }
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(),
                    visible = linkPreviewList.size > 3
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = AppTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = AppTheme.colors.textSecondary,
                            containerColor = Color.Transparent
                        ),
                        border = null,
                        elevation = null,
                        contentPadding = PaddingValues(horizontal = 15.dp),
                        onClick = { expanded.value = !expanded.value }
                    ) {
                        val buttonIcon =
                            if (expanded.value) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown

                        val buttonText =
                            if (expanded.value) stringResource(R.string.Hide) else stringResource(
                                R.string.ShowAll
                            )

                        AnimatedContent(targetState = buttonIcon, label = "animate icon") { icon ->
                            Icon(
                                imageVector = icon,
                                contentDescription = buttonText,
                                tint = AppTheme.colors.textSecondary
                            )
                        }
                        Spacer(modifier = Modifier.padding(end = 10.dp))
                        AnimatedContent(targetState = buttonText, label = "animate text") { text ->
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppTheme.colors.textSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
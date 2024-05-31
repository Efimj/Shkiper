package com.jobik.shkiper.ui.components.cards

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.efim.shkiper.core.resources.R
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import com.jobik.shkiper.util.ThemeUtil
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun LinkCard(
    link: String,
    linkTextLabel: String,
    onLinkCopiedText: String
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Card(
        shape = AppTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.container,
            contentColor = AppTheme.colors.text,
            disabledContainerColor = AppTheme.colors.container,
            disabledContentColor = AppTheme.colors.text
        ),
        elevation = CardDefaults.outlinedCardElevation(),
        border = null,
        modifier = Modifier
            .bounceClick(0.95f)
            .clip(AppTheme.shapes.medium)
            .combinedClickable(
                onClick = {
                    try {
                        IntentHelper().openBrowserIntent(context = context, link = link)
                    } catch (
                        e: Exception
                    ) {
                        Log.e("LinkCard", "OnClick", e)
                    }
                },
                onLongClick = {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(linkTextLabel, link))
                    coroutineScope.launch {
                        SnackbarHostUtil.snackbarHostState.showSnackbar(
                            SnackbarVisualsCustom(
                                message = onLinkCopiedText,
                                icon = Icons.Default.Link
                            )
                        )
                    }
                },
            ),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                modifier = Modifier.height(60.dp),
                painter = painterResource(if (ThemeUtil.isDarkMode.value == true) R.drawable.github_dark else R.drawable.github_light),
                contentDescription = stringResource(id = R.string.Image),
                contentScale = ContentScale.Fit,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp, alignment = Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(id = R.string.OpenSource),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.text,
                    modifier = Modifier.basicMarquee()
                )
                Text(
                    text = stringResource(id = R.string.OpenSourceDescription),
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colors.textSecondary,
                )
            }
        }
    }
}
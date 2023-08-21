package com.jobik.shkiper.ui.components.cards

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jobik.shkiper.helpers.LinkHelper
import com.jobik.shkiper.ui.helpers.MultipleEventsCutter
import com.jobik.shkiper.ui.helpers.get
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.CustomAppTheme
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import kotlinx.coroutines.launch
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.IntentHelper

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinkPreviewCard(openGraphData: LinkHelper.LinkPreview) {
    val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val isImageError = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val onLinkCopiedText = stringResource(R.string.LinkCopied)
    val linkTextLabel = stringResource(R.string.Link)
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .bounceClick(0.95f)
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = {
                    multipleEventsCutter.processEvent {
                        try {
                            openGraphData.link?.let { IntentHelper().openBrowserIntent(context, it) }
                        } catch (
                            e: Exception
                        ) {
                            Log.e("LinkPreviewCard", "OnClick", e)
                        }
                    }
                },
                onLongClick = {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(linkTextLabel, openGraphData.link))
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
        elevation = 0.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = CustomAppTheme.colors.secondaryBackground,
        contentColor = CustomAppTheme.colors.text,
    ) {
        Row {
            AsyncImage(
                model = openGraphData.img,
                contentDescription = stringResource(R.string.LinkImage),
                modifier = Modifier.width(60.dp),
                contentScale = ContentScale.Crop,
                error = rememberVectorPainter(Icons.Default.Language),
                onError = { isImageError.value = true },
                colorFilter = if (isImageError.value) ColorFilter.tint(CustomAppTheme.colors.textSecondary) else null
            )
            Column(
                modifier = Modifier.padding(horizontal = 10.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                if (openGraphData.title != null && openGraphData.title!!.isNotEmpty())
                    Text(
                        text = openGraphData.title!!,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
                        color = CustomAppTheme.colors.textSecondary,
                    )
                if (openGraphData.description != null && openGraphData.description!!.isNotEmpty())
                    Text(
                        text = openGraphData.description!!,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body1,
                        color = CustomAppTheme.colors.textSecondary,
                    )
                if ((openGraphData.description == null || openGraphData.title == null) && openGraphData.url != null)
                    Text(
                        text = openGraphData.url!!,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body1,
                        color = CustomAppTheme.colors.textSecondary,
                    )
            }
        }
    }
}
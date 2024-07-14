package com.jobik.shkiper.ui.components.modals

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.ui.helpers.SetRichTextDefaultStyles
import com.jobik.shkiper.ui.theme.AppTheme
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun ShareNoteDialog(
    noteHeader: String,
    noteContent: String,
    onConfirm: () -> Unit,
    onGoBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val richTextState = rememberRichTextState()
    val captureController = rememberCaptureController()
    val context = LocalContext.current
    val noteContentScrollState = rememberScrollState()
    val actionsBackgroundColor: Color by animateColorAsState(
        targetValue = if (!noteContentScrollState.canScrollForward) AppTheme.colors.background else AppTheme.colors.container,
        label = "actionsBackgroundColor"
    )

    SetRichTextDefaultStyles(richTextState)

    LaunchedEffect(Unit) {
        if (richTextState.annotatedString.text !== noteContent)
            richTextState.setHtml(noteContent)
        else
            richTextState.setText("")
    }

    Dialog(
        onDismissRequest = onGoBack,
        properties = DialogProperties(
            true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
                .clip(AppTheme.shapes.medium)
                .background(AppTheme.colors.background),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(noteContentScrollState)
            ) {
                Column(
                    modifier = Modifier
                        .capturable(captureController)
                        .fillMaxWidth()
                        .background(AppTheme.colors.background)
                        .padding(20.dp)
                ) {
                    if (noteHeader.isNotEmpty()) {
                        Text(
                            text = noteHeader,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = AppTheme.colors.text
                        )
                    }
                    if (richTextState.annotatedString.text.isNotEmpty() && noteHeader.isNotEmpty())
                        Spacer(modifier = Modifier.height(4.dp))
                    if (richTextState.annotatedString.text.isNotEmpty()) {
                        RichText(
                            state = richTextState,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.colors.text,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(actionsBackgroundColor)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier.height(50.dp),
                    shape = AppTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = AppTheme.colors.text,
                        containerColor = Color.Transparent
                    ),
                    border = null,
                    elevation = null,
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    onClick = onGoBack
                ) {
                    Text(
                        text = stringResource(R.string.Cancel),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.width(12.dp))
                Button(
                    modifier = Modifier.height(50.dp),
                    shape = AppTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = AppTheme.colors.text,
                        containerColor = AppTheme.colors.primary
                    ),
                    border = null,
                    elevation = null,
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    onClick = {
                        scope.launch {
                            val capture = captureController.captureAsync()
                            try {
                                val bitmap = capture.await()
                                IntentHelper().shareImageToOthers(
                                    context = context,
                                    text = "",
                                    bitmap = bitmap,
                                    format = Bitmap.CompressFormat.PNG
                                )
                                onConfirm()
                            } catch (error: Throwable) {
                                Log.d("Capturable error", error.toString())
                                // Error occurred. Handle it!
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = stringResource(R.string.Share),
                        tint = AppTheme.colors.onPrimary
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.Share),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
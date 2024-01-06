package com.jobik.shkiper.ui.components.modals

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.ui.components.buttons.ButtonStyle
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.theme.CustomTheme
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.IntentHelper
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController


@Composable
fun ShareNoteDialog(
    noteHeader: String,
    noteContent: String,
    onConfirm: () -> Unit,
    onGoBack: () -> Unit,
) {
    val richTextState = rememberRichTextState()
    val captureController = rememberCaptureController()
    val context = LocalContext.current
    val noteContentScrollState = rememberScrollState()
    val actionsBackgroundColor: Color by animateColorAsState(
        targetValue = if (!noteContentScrollState.canScrollForward) CustomTheme.colors.mainBackground else CustomTheme.colors.secondaryBackground,
        label = "actionsBackgroundColor"
    )

    val codeColor = CustomTheme.colors.textOnActive
    val codeBackgroundColor = CustomTheme.colors.active.copy(alpha = .2f)
    val codeStrokeColor = CustomTheme.colors.active
    val linkColor = CustomTheme.colors.text

    LaunchedEffect(Unit) {
        richTextState.setConfig(
            linkColor = linkColor,
            linkTextDecoration = TextDecoration.Underline,
            codeColor = codeColor,
            codeBackgroundColor = codeBackgroundColor,
            codeStrokeColor = codeStrokeColor
        )

        if (richTextState.annotatedString.text !== noteContent)
            richTextState.setHtml(noteContent)
        else
            richTextState.setText("")
    }

    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true, usePlatformDefaultWidth = false)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CustomTheme.shapes.medium)
                .background(CustomTheme.colors.mainBackground),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(noteContentScrollState)
            ) {
                Capturable(
                    controller = captureController,
                    onCaptured = { bitmap, error ->
                        if (bitmap != null) {
                            IntentHelper().shareImageToOthers(
                                context = context,
                                text = "",
                                bitmap = bitmap,
                                format = Bitmap.CompressFormat.PNG
                            )
                            // handle success action share
                            onConfirm()
                        }

                        if (error != null) {
                            Log.d("Capturable error", error.toString())
                            // Error occurred. Handle it!
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CustomTheme.colors.mainBackground)
                            .padding(20.dp)
                    ) {
                        if (noteHeader.isNotEmpty()) {
                            Text(
                                text = noteHeader,
                                style = MaterialTheme.typography.h6.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 21.sp
                                ),
                                color = CustomTheme.colors.text
                            )
                        }
                        if (richTextState.annotatedString.text.isNotEmpty() && noteHeader.isNotEmpty())
                            Spacer(modifier = Modifier.height(4.dp))
                        if (richTextState.annotatedString.text.isNotEmpty()) {
                            RichText(
                                state = richTextState,
                                style = MaterialTheme.typography.body1,
                                color = CustomTheme.colors.textSecondary,
                            )
                        }
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
                CustomButton(
                    text = stringResource(id = R.string.Cancel),
                    onClick = onGoBack,
                    style = ButtonStyle.Text,
                )
                Spacer(Modifier.width(12.dp))
                CustomButton(
                    text = stringResource(id = R.string.Share),
                    onClick = { captureController.capture(); },
                    style = ButtonStyle.Filled,
                )
            }
        }
    }
}
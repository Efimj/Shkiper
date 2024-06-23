package com.jobik.shkiper.screens.note

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LinkOff
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.TextFormat
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.R
import com.jobik.shkiper.database.models.NotePosition
import com.jobik.shkiper.ui.animation.AnimateVerticalSwitch
import com.jobik.shkiper.ui.components.layouts.RichTextBottomToolBar
import com.jobik.shkiper.ui.components.modals.BottomSheetAction
import com.jobik.shkiper.ui.components.modals.BottomSheetActions
import com.jobik.shkiper.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NoteScreenFooter(onBack: () -> Unit, noteViewModel: NoteViewModel, richTextState: RichTextState) {
    val backgroundColor by animateColorAsState(
        if (noteViewModel.screenState.value.isBottomAppBarHover || noteViewModel.screenState.value.isStyling) AppTheme.colors.container else AppTheme.colors.background,
        label = "backgroundColor",
    )

    val contentColorValue =
        if (noteViewModel.screenState.value.isBottomAppBarHover || noteViewModel.screenState.value.isStyling) AppTheme.colors.onSecondaryContainer else AppTheme.colors.textSecondary

    val contentColor by animateColorAsState(
        contentColorValue, label = "contentColor",
    )

    val shadowElevation = animateDpAsState(
        targetValue = if (noteViewModel.screenState.value.isBottomAppBarHover) 8.dp else 0.dp,
        label = "shadowElevation"
    )

    val barHeight = 54.dp

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shadowElevation = shadowElevation.value
    ) {
        AnimateVerticalSwitch(
            modifier = Modifier,
            directionUp = true,
            state = noteViewModel.screenState.value.isStyling,
            topComponent = {
                Row(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
                        .horizontalWindowInsetsPadding()
                        .height(barHeight)
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row {
                        IconButton(
                            onClick = { noteViewModel.switchStyling() },
                            modifier = Modifier
                                .alpha(if (noteViewModel.screenState.value.isStylingEnabled) 1f else .5f),
                            enabled = noteViewModel.screenState.value.isStylingEnabled
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.TextFormat,
                                contentDescription = "",
                                tint = if (noteViewModel.screenState.value.isStyling && noteViewModel.screenState.value.isStylingEnabled) AppTheme.colors.text else contentColor,
                            )
                        }
                    }
                    if (noteViewModel.screenState.value.intermediateStates.size < 2) {
                        Text(
                            text = "${stringResource(R.string.ChangedAt)} ${getLastUpdatedNoteTime(noteViewModel)}",
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp)
                                .basicMarquee(),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            color = contentColor
                        )
                    } else {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
                        ) {
                            IconButton(
                                onClick = {
                                    noteViewModel.noteStateGoBack()
                                    richTextState.setHtml(noteViewModel.screenState.value.intermediateStates[noteViewModel.screenState.value.currentIntermediateIndex].body)
                                },
                                enabled = noteViewModel.screenState.value.currentIntermediateIndex > 0
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.undo_fill0_wght400_grad0_opsz24),
                                    contentDescription = stringResource(R.string.GoBack),
                                    tint = if (noteViewModel.screenState.value.currentIntermediateIndex > 0) AppTheme.colors.primary else AppTheme.colors.textSecondary,
                                )
                            }
                            IconButton(
                                onClick = {
                                    noteViewModel.noteStateGoNext()
                                    richTextState.setHtml(noteViewModel.screenState.value.intermediateStates[noteViewModel.screenState.value.currentIntermediateIndex].body)
                                },
                                enabled = noteViewModel.screenState.value.currentIntermediateIndex < noteViewModel.screenState.value.intermediateStates.size - 1
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.redo_fill0_wght400_grad0_opsz24),
                                    contentDescription = stringResource(R.string.GoForward),
                                    tint = if (noteViewModel.screenState.value.currentIntermediateIndex < noteViewModel.screenState.value.intermediateStates.size - 1) AppTheme.colors.primary else AppTheme.colors.textSecondary,
                                )
                            }
                        }
                    }
                    Row {
                        val isBottomActionsDialogOpened = remember { mutableStateOf(false) }

                        IconButton(
                            onClick = { isBottomActionsDialogOpened.value = true },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                contentDescription = stringResource(R.string.Open),
                                tint = contentColor
                            )
                        }

                        BottomActions(isOpened = isBottomActionsDialogOpened, noteViewModel = noteViewModel)
                    }
                }
            },
            bottomComponent = {
                Row(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
                        .height(barHeight),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RichTextBottomToolBar(state = richTextState, onClose = { noteViewModel.switchStyling(false) })
                }
            }
        )
    }
}

private fun getLastUpdatedNoteTime(noteViewModel: NoteViewModel): String {
    val duration = Duration.between(noteViewModel.screenState.value.updatedDate, LocalDateTime.now())
    return if (duration.toDays() > 0)
        noteViewModel.screenState.value.updatedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm"))
    else
        noteViewModel.screenState.value.updatedDate.format(DateTimeFormatter.ofPattern("HH:mm"))
}

@Composable
private fun BottomActions(
    isOpened: MutableState<Boolean>,
    noteViewModel: NoteViewModel,
) {
    if (noteViewModel.screenState.value.notePosition == NotePosition.DELETE) {
        BottomSheetActions(
            isOpen = isOpened.value,
            actions = listOf(
                BottomSheetAction(
                    icon = Icons.Outlined.DeleteForever,
                    title = stringResource(R.string.Delete),
                    action = noteViewModel::switchDeleteDialogShow,
                ),
            )
        ) {
            isOpened.value = false
        }
    } else {
        BottomSheetActions(
            isOpen = isOpened.value,
            actions = listOf(
                BottomSheetAction(
                    icon = Icons.Outlined.Share,
                    title = stringResource(R.string.ShareNote),
                    action = noteViewModel::switchShowShareDialog,
                ),
                BottomSheetAction(
                    icon = Icons.Outlined.Widgets,
                    title = stringResource(R.string.CreateWidget),
                    action = noteViewModel::createWidget,
                ),
                BottomSheetAction(
                    icon = if (noteViewModel.screenState.value.linkPreviewEnabled) Icons.Outlined.LinkOff else Icons.Outlined.Link,
                    title = stringResource(if (noteViewModel.screenState.value.linkPreviewEnabled) R.string.DisableLinkPreviews else R.string.EnableLinkPreviews),
                    action = noteViewModel::switchLinkPreviewEnabled,
                ),
                BottomSheetAction(
                    icon = Icons.Outlined.DeleteForever,
                    title = stringResource(R.string.Delete),
                    action = noteViewModel::moveToBasket,
                ),
            )
        ) {
            isOpened.value = false
        }
    }
}
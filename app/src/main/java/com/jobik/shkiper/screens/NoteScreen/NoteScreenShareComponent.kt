package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Html
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.TextHelper
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.modals.ShareNoteDialog
import com.jobik.shkiper.ui.theme.CustomTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenShareComponent(
    noteViewModel: NoteViewModel,
    richTextState: RichTextState
) {
    val context = LocalContext.current
    val shareSheetState = androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(noteViewModel.screenState.value.showShareDialog) {
        if (!noteViewModel.screenState.value.showShareDialog) {
            shareSheetState.hide()
        }
    }

    if (noteViewModel.screenState.value.showShareDialog) {
        val topInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top)
        val bottomInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)

        ModalBottomSheet(
            sheetState = shareSheetState,
            dragHandle = null,
            containerColor = Color.Transparent,
            contentColor = CustomTheme.colors.text,
            windowInsets = WindowInsets.ime,
            onDismissRequest = { noteViewModel.switchShowShareDialog(mode = false) }) {
            Spacer(modifier = Modifier.windowInsetsPadding(topInsets))
            Surface(
                shape = BottomSheetDefaults.ExpandedShape,
                contentColor = CustomTheme.colors.text,
                color = CustomTheme.colors.mainBackground,
                tonalElevation = BottomSheetDefaults.Elevation,
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .windowInsetsPadding(bottomInsets)
                        .padding(bottom = 10.dp)
                ) {
                    SettingsItem(
                        icon = Icons.Outlined.ContentCopy,
                        title = stringResource(R.string.ShareText),
                        onClick = {
                            scope.launch { shareSheetState.hide() }.invokeOnCompletion {
                                noteViewModel.shareNoteText(
                                    context = context,
                                    text = TextHelper.removeMarkdownStyles(richTextState.toMarkdown())
                                )
                                noteViewModel.switchShowShareDialog()
                            }
                        })
                    SettingsItem(
                        icon = Icons.Outlined.Html,
                        title = stringResource(R.string.ShareHTMLText),
                        onClick = {
                            scope.launch { shareSheetState.hide() }.invokeOnCompletion {
                                noteViewModel.shareNoteText(
                                    context = context,
                                    text = richTextState.toHtml()
                                )
                                noteViewModel.switchShowShareDialog()
                            }
                        })
                    SettingsItem(
                        icon = Icons.Outlined.Code,
                        title = stringResource(R.string.ShareMarkdownText),
                        onClick = {
                            scope.launch { shareSheetState.hide() }.invokeOnCompletion {
                                noteViewModel.shareNoteText(
                                    context = context,
                                    text = richTextState.toMarkdown()
                                )
                                noteViewModel.switchShowShareDialog()
                            }
                        })
                    SettingsItem(
                        icon = Icons.Outlined.Screenshot,
                        title = stringResource(R.string.ShareImage),
                        onClick = {
                            scope.launch { shareSheetState.hide() }.invokeOnCompletion {
                                noteViewModel.switchShowShareNoteDialog()
                                noteViewModel.switchShowShareDialog()
                            }
                        })
                }
            }
        }

        if (noteViewModel.screenState.value.showShareNoteDialog) {
            ShareNoteDialog(
                noteHeader = noteViewModel.screenState.value.noteHeader,
                noteContent = noteViewModel.screenState.value.noteBody,
                onConfirm = noteViewModel::switchShowShareNoteDialog,
                onGoBack = noteViewModel::switchShowShareNoteDialog,
            )
        }
    }
}
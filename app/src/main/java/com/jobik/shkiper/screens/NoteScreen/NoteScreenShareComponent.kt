package com.jobik.shkiper.screens.NoteScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Html
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.TextHelper
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.modals.CustomModalBottomSheet
import com.jobik.shkiper.ui.components.modals.ShareNoteDialog
import com.mohamedrejeb.richeditor.model.RichTextState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenShareComponent(
    noteViewModel: NoteViewModel,
    richTextState: RichTextState
) {
    val context = LocalContext.current
    val shareSheetState = androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(noteViewModel.screenState.value.showShareDialog) {
        if (!noteViewModel.screenState.value.showShareDialog) {
            shareSheetState.hide()
        }
    }

    if (noteViewModel.screenState.value.showShareDialog) {
        CustomModalBottomSheet(
            state = shareSheetState,
            onCancel = {
                noteViewModel.switchShowShareDialog(mode = false)
            },
            dragHandle = null,
        ) {
            SettingsItem(
                icon = Icons.Outlined.ContentCopy,
                title = stringResource(R.string.ShareText),
                onClick = {
                    noteViewModel.shareNoteText(
                        context,
                        TextHelper.removeMarkdownStyles(richTextState.toMarkdown())
                    )
                    noteViewModel.switchShowShareDialog()
                })
            SettingsItem(
                icon = Icons.Outlined.Html,
                title = stringResource(R.string.ShareHTMLText),
                onClick = {
                    noteViewModel.shareNoteText(
                        context,
                        richTextState.toHtml()
                    )
                    noteViewModel.switchShowShareDialog()
                })
            SettingsItem(
                icon = Icons.Outlined.Code,
                title = stringResource(R.string.ShareMarkdownText),
                onClick = {
                    noteViewModel.shareNoteText(
                        context,
                        richTextState.toMarkdown()
                    )
                    noteViewModel.switchShowShareDialog()
                })
            SettingsItem(
                icon = Icons.Outlined.Screenshot,
                title = stringResource(R.string.ShareImage),
                onClick = {
                    noteViewModel.switchShowShareNoteDialog()
                    noteViewModel.switchShowShareDialog()
                })
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
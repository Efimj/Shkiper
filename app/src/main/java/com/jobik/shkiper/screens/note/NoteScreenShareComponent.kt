package com.jobik.shkiper.screens.note

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Html
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.TextHelper
import com.jobik.shkiper.ui.components.modals.BottomSheetAction
import com.jobik.shkiper.ui.components.modals.BottomSheetActions
import com.jobik.shkiper.ui.components.modals.ShareNoteDialog
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun NoteScreenShareComponent(
    noteViewModel: NoteViewModel,
    richTextState: RichTextState
) {
    val context = LocalContext.current

    BottomSheetActions(
        isOpen = noteViewModel.screenState.value.showShareDialog, actions = listOf(
            BottomSheetAction(
                icon = Icons.Outlined.ContentCopy,
                title = stringResource(R.string.ShareText),
                action = {
                    noteViewModel.shareNoteText(
                        context = context,
                        text = TextHelper.removeMarkdownStyles(richTextState.toMarkdown())
                    )
                }),
            BottomSheetAction(
                icon = Icons.Outlined.Html,
                title = stringResource(R.string.ShareHTMLText),
                action = {
                    noteViewModel.shareNoteText(
                        context = context,
                        text = richTextState.toHtml()
                    )
                }),
            BottomSheetAction(
                icon = Icons.Outlined.Code,
                title = stringResource(R.string.ShareMarkdownText),
                action = {
                    noteViewModel.shareNoteText(
                        context = context,
                        text = richTextState.toMarkdown()
                    )
                }),
            BottomSheetAction(
                icon = Icons.Outlined.Screenshot,
                title = stringResource(R.string.ShareImage),
                action = {
                    noteViewModel.switchShowShareNoteDialog()
                })
        )
    ) {
        noteViewModel.switchShowShareDialog(mode = false)
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
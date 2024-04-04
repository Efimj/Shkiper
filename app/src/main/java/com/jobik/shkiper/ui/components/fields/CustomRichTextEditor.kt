package com.jobik.shkiper.ui.components.fields

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.BasicRichTextEditor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomRichTextEditor(
    state: RichTextState,
    placeholder: String = "",
    textColor: Color = AppTheme.colors.text,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Default,
        capitalization = KeyboardCapitalization.Sentences,
        autoCorrect = true
    ),
    keyboardActions: KeyboardActions? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = AppTheme.colors.container,
        placeholderColor = AppTheme.colors.textSecondary,
        leadingIconColor = AppTheme.colors.textSecondary,
        trailingIconColor = AppTheme.colors.textSecondary,
        textColor = AppTheme.colors.text,
        cursorColor = AppTheme.colors.primary,
        focusedLabelColor = AppTheme.colors.textSecondary,
        unfocusedLabelColor = AppTheme.colors.textSecondary,
    )

    val customTextSelectionColors = TextSelectionColors(
        handleColor = AppTheme.colors.primary,
        backgroundColor = AppTheme.colors.primary.copy(alpha = 0.4f),
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
//        LocalTextToolbar provides CustomTextToolbar(LocalView.current), doesn't work in bottomSheetModal
    ) {
        BasicRichTextEditor(
            modifier = modifier,
            state = state,
            enabled = enabled,
            minLines = minLines,
            singleLine = singleLine,
            interactionSource = interactionSource,
            textStyle = textStyle.copy(color = textColor),
            cursorBrush = SolidColor(AppTheme.colors.primary),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions ?: KeyboardActions(
                onAny = {
                    state.setText("\n")
                }
            ),
        ) {
            TextFieldDefaults.TextFieldDecorationBox(
                value = state.toMarkdown(),
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                singleLine = singleLine,
                enabled = enabled,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                placeholder = { Text(text = placeholder, style = textStyle) },
                colors = textFieldColors,
            )
        }
    }
}

class CustomTextToolbar(private val view: View) : TextToolbar {
    override fun hide() {
        println("hide")
    }

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?
    ) {
        view.startActionMode(TextActionModeCallback())
    }

    override val status: TextToolbarStatus = TextToolbarStatus.Shown

    class TextActionModeCallback(
    ) : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            println("onActionItemClicked $mode $item")
            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            println("onActionItemClicked $mode $menu")
            return false
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            println("onActionItemClicked $mode $menu")
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            println("onActionItemClicked $mode")
        }
    }
}

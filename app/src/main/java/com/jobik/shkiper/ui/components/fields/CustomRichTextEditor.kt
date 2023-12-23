package com.jobik.shkiper.ui.components.fields

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.CustomTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.BasicRichTextEditor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomRichTextEditor(
    state: RichTextState,
    placeholder: String = "",
    textColor: Color = CustomTheme.colors.text,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    modifier: Modifier = Modifier,
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
        backgroundColor = CustomTheme.colors.secondaryBackground,
        placeholderColor = CustomTheme.colors.textSecondary,
        leadingIconColor = CustomTheme.colors.textSecondary,
        trailingIconColor = CustomTheme.colors.textSecondary,
        textColor = CustomTheme.colors.text,
        cursorColor = CustomTheme.colors.active,
        focusedLabelColor = CustomTheme.colors.textSecondary,
        unfocusedLabelColor = CustomTheme.colors.textSecondary,
    )

    val customTextSelectionColors = TextSelectionColors(
        handleColor = CustomTheme.colors.active,
        backgroundColor = CustomTheme.colors.active.copy(alpha = 0.4f),
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
    ) {
        BasicRichTextEditor(
            modifier = modifier,
            state = state,
            enabled = enabled,
            interactionSource = interactionSource,
            singleLine = singleLine,
            textStyle = textStyle.copy(color = textColor),
            cursorBrush = SolidColor(CustomTheme.colors.active),
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onAny = {
                    Log.i("d","sdsax")
                    state.setText("\n\n")
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
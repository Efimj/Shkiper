package com.jobik.shkiper.ui.components.fields

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomDefaultTextField(
    text: String,
    placeholder: String = "",
    textColor: Color = AppTheme.colors.text,
    onTextChange: (String) -> Unit = {},
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
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            textStyle = textStyle.copy(color = textColor),
            cursorBrush = SolidColor(AppTheme.colors.primary),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions ?: KeyboardActions(
                onAny = {
                    onTextChange("\n")
                }
            ),
            modifier = modifier,
        ) {
            TextFieldDefaults.TextFieldDecorationBox(
                value = text,
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
package com.example.notepadapp.ui.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadapp.ui.theme.CustomAppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTextField(
    textFieldValue: TextFieldValue,
    placeholder: String = "",
    textColor: Color = CustomAppTheme.colors.text,
    onValueChange: (TextFieldValue) -> Unit = {},
    textStyle: TextStyle = MaterialTheme.typography.body1,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Default,
    ),
    keyboardActions: KeyboardActions? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = CustomAppTheme.colors.secondaryBackground,
        placeholderColor = CustomAppTheme.colors.textSecondary,
        leadingIconColor = CustomAppTheme.colors.textSecondary,
        trailingIconColor = CustomAppTheme.colors.textSecondary,
        textColor = CustomAppTheme.colors.text,
        cursorColor = CustomAppTheme.colors.textSecondary,
        focusedLabelColor = CustomAppTheme.colors.textSecondary,
        unfocusedLabelColor = CustomAppTheme.colors.textSecondary,
    )

    val customTextSelectionColors = TextSelectionColors(
        handleColor = CustomAppTheme.colors.stroke,
        backgroundColor = CustomAppTheme.colors.stroke,
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            textStyle = textStyle.copy(color = textColor),
            cursorBrush = SolidColor(CustomAppTheme.colors.textSecondary),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions ?: KeyboardActions(
                onAny = {
                    onValueChange(textFieldValue.copy("\n"))
                }
            ),
            modifier = modifier,
        ) {
            TextFieldDefaults.TextFieldDecorationBox(
                value = textFieldValue.text,
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
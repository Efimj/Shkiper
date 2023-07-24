package com.android.notepad.ui.components.fields

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.notepad.R
import com.android.notepad.ui.theme.CustomAppTheme

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f
private val CONTAINERSHAPE = RoundedCornerShape(15.dp)

@Composable
fun SearchBar(
    search: String,
    placeholder: String = stringResource(R.string.Search),
    onValueChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val enabled = true
    val textStyle = MaterialTheme.typography.body1.copy(color = CustomAppTheme.colors.text)
    val visualTransformation = VisualTransformation.None
    val isTrailingIconVisible= search.isNotBlank()

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
    val maxFontSize = 18.sp
    val minFontSize = 16.sp

    val customTextSelectionColors = TextSelectionColors(
        handleColor = CustomAppTheme.colors.stroke,
        backgroundColor = CustomAppTheme.colors.stroke,
    )
    val focusManager = LocalFocusManager.current

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val shrunkFontSize = resizeFontSize(maxFontSize, search, textStyle, minFontSize, TEXT_SCALE_REDUCTION_INTERVAL)
        CompositionLocalProvider(
            LocalTextSelectionColors provides customTextSelectionColors,
        ) {
            BasicTextField(
                value = search,
                onValueChange = onValueChange,
                interactionSource = interactionSource,
                enabled = enabled,
                singleLine = true,
                textStyle = textStyle.copy(fontSize = shrunkFontSize),
                cursorBrush = SolidColor(CustomAppTheme.colors.textSecondary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = CustomAppTheme.colors.secondaryBackground,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clip(CONTAINERSHAPE)
                    .height(50.dp)
                    .clip(CONTAINERSHAPE)
                    .border(1.dp, CustomAppTheme.colors.stroke, CONTAINERSHAPE)
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp, 0.dp, 0.dp),
            ) {
                TextFieldDecorationBox(
                    search,
                    visualTransformation,
                    it,
                    enabled,
                    interactionSource,
                    isTrailingIconVisible,
                    onTrailingIconClick,
                    placeholder,
                    textFieldColors
                )
            }
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.resizeFontSize(
    maxFontSize: TextUnit,
    search: String,
    textStyle: TextStyle,
    minFontSize: TextUnit,
    TEXT_SCALE_REDUCTION_INTERVAL: Float
): TextUnit {
    var shrunkFontSize = maxFontSize
    val calculateIntrinsics = @Composable {
        ParagraphIntrinsics(
            text = search,
            style = textStyle.copy(fontSize = shrunkFontSize),
            density = LocalDensity.current,
            fontFamilyResolver = createFontFamilyResolver(LocalContext.current)
        )
    }

    var intrinsics = calculateIntrinsics()
    with(LocalDensity.current) {
        val textFieldDefaultHorizontalPadding = 53.dp.toPx()
        val maxInputWidth = maxWidth.toPx() - 2 * textFieldDefaultHorizontalPadding

        while (minFontSize < shrunkFontSize && intrinsics.maxIntrinsicWidth > maxInputWidth) {
            shrunkFontSize *= TEXT_SCALE_REDUCTION_INTERVAL
            intrinsics = calculateIntrinsics()
        }
    }
    return shrunkFontSize
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TextFieldDecorationBox(
    search: String,
    visualTransformation: VisualTransformation,
    it: @Composable () -> Unit,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    isTrailingIconVisible: Boolean,
    onTrailingIconClick: () -> Unit,
    placeholder: String,
    textFieldColors: TextFieldColors
) {
    TextFieldDefaults.TextFieldDecorationBox(
        value = search,
        visualTransformation = visualTransformation,
        innerTextField = it,
        singleLine = true,
        enabled = enabled,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp),
        leadingIcon = { leadingIcon() },
        trailingIcon = {
            trailingIcon(isTrailingIconVisible, onTrailingIconClick)
        },
        placeholder = { Text(text = placeholder) },
        colors = textFieldColors,
    )
}

@Composable
private fun trailingIcon(isTrailingIconVisible: Boolean, onTrailingIconClick: () -> Unit) {
    if (isTrailingIconVisible) {
        IconButton(
            onClick = onTrailingIconClick,
        ) {
            Icon(
                tint = CustomAppTheme.colors.textSecondary,
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.Clear)
            )
        }
    }
}

@Composable
private fun leadingIcon() {
    Icon(
        imageVector = Icons.Default.Search,
        contentDescription = stringResource(R.string.Search),
        tint = CustomAppTheme.colors.textSecondary
    )
}


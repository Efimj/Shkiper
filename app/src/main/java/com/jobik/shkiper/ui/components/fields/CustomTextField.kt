package com.jobik.shkiper.ui.components.fields

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobik.shkiper.ui.theme.CustomTheme

@Composable
fun CustomTextField(
    columnModifier: Modifier = Modifier.fillMaxWidth(),
    fieldModifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    onChange: (text: String) -> Unit,
    label: String? = "",
    placeholder: String? = "",
    singleLine: Boolean = true,
    enabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Default,
        capitalization = KeyboardCapitalization.Sentences,
        autoCorrect = true
    ),
    keyboardActions: KeyboardActions? = KeyboardActions.Default,
) {
    var isFocused by remember { mutableStateOf(false) }

    val labelColor: Color by animateColorAsState(
        targetValue = if (isFocused) CustomTheme.colors.active else CustomTheme.colors.textSecondary,
        label = "labelColor"
    )

    val bottomDividerColor: Color by animateColorAsState(
        targetValue = if (isFocused) CustomTheme.colors.active else CustomTheme.colors.secondaryStroke,
        label = "bottomDividerColor"
    )

    Column(modifier = columnModifier) {
        if (label != null) {
            Text(
                text = label,
                color = labelColor,
                style = textStyle.copy(fontSize = 14.sp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        CustomDefaultTextField(
            modifier = fieldModifier.onFocusChanged { state -> isFocused = state.isFocused },
            text = text,
            onTextChange = onChange,
            placeholder = placeholder ?: "",
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            enabled = enabled,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = bottomDividerColor
        )
    }
}
package com.jobik.shkiper.ui.components.fields


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun CustomOutlinedTextField(
    rowModifier: Modifier = Modifier.fillMaxWidth(),
    fieldModifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    onChange: (text: String) -> Unit,
    leadingIcon: ImageVector? = null,
    placeholder: String? = "",
    singleLine: Boolean = true,
    enabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
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
        targetValue = if (isFocused) AppTheme.colors.primary else AppTheme.colors.textSecondary,
        label = "labelColor"
    )

    val borderColor: Color by animateColorAsState(
        targetValue = if (isFocused) AppTheme.colors.primary else AppTheme.colors.border,
        label = "bottomDividerColor"
    )

    Row(
        modifier = rowModifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 14.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                tint = labelColor,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        CustomDefaultTextField(
            modifier = fieldModifier.onFocusChanged { state -> isFocused = state.isFocused },
            text = text,
            onTextChange = onChange,
            placeholder = placeholder ?: "",
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            enabled = enabled,
            textStyle = textStyle,
        )
    }
}
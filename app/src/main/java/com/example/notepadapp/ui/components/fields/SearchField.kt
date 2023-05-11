package com.example.notepadapp.ui.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notepadapp.ui.theme.CustomAppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchField(
    search: String,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val containerShape = RoundedCornerShape(15.dp)
    val singleLine = true
    val enabled = true
    val visualTransformation = VisualTransformation.None

    BasicTextField(
        value = search,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        enabled = enabled,
        singleLine = singleLine,
        textStyle = MaterialTheme.typography.body1.copy(color = CustomAppTheme.colors.text),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CustomAppTheme.colors.secondaryBackground,
                shape = RoundedCornerShape(8.dp))
            .clip(containerShape)
            .height(45.dp)
            .clip(containerShape)
            .border(1.dp, CustomAppTheme.colors.stroke, containerShape)
            .fillMaxWidth()
            .padding(15.dp, 0.dp, 0.dp, 0.dp),
    ) {
        TextFieldDefaults.TextFieldDecorationBox(
            value = search,
            visualTransformation = visualTransformation,
            innerTextField = it,
            singleLine = singleLine,
            enabled = enabled,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(0.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "", tint = CustomAppTheme.colors.textSecondary) },
            trailingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "", tint = CustomAppTheme.colors.textSecondary) },
            placeholder = { Text(text = "Search") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = CustomAppTheme.colors.secondaryBackground,
                placeholderColor = CustomAppTheme.colors.textSecondary,
                leadingIconColor = CustomAppTheme.colors.textSecondary,
                trailingIconColor = CustomAppTheme.colors.textSecondary,
                textColor = CustomAppTheme.colors.text,
                cursorColor = CustomAppTheme.colors.textSecondary,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedLabelColor = CustomAppTheme.colors.textSecondary,
                unfocusedLabelColor = CustomAppTheme.colors.textSecondary,
            ),
        )
    }
}

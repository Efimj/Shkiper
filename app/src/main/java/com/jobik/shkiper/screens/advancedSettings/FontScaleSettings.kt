package com.jobik.shkiper.screens.advancedSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.fields.CustomSlider
import com.jobik.shkiper.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun FontScaleSettings() {
    val fontScale = remember {
        mutableFloatStateOf(1f)
    }

    /**
     * Rounds this [Float] to another with 2 significant numbers
     * 0.1234 is rounded to 0.12
     * 0.127 is rounded to 0.13
     */
    fun Float.roundToTwoDigits() = (this * 100.0f).roundToInt() / 100.0f
    Column {
        SettingsItem(
            icon = Icons.Outlined.TextFields,
            title = stringResource(R.string.font_scale),
            action = {
                Text(
                    text = String.format("%.2f", fontScale.floatValue.roundToTwoDigits()),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.primary
                )
            }
        )
        CustomSlider(
            modifier = Modifier.padding(horizontal = 20.dp),
            value = fontScale.floatValue,
            valueRange = 0.5f..1.5f,
            onValueChange = { fontScale.floatValue = it },
            steps = 19
        )
    }
}
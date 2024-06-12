package com.jobik.shkiper.screens.advancedSettings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.fields.CustomSlider
import com.jobik.shkiper.ui.components.layouts.Counter
import com.jobik.shkiper.ui.components.layouts.SettingsGroup
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.theme.AppTheme
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt

@SuppressLint("DefaultLocale")
@Composable
fun AdvancedSettings() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .allWindowInsetsPadding()
            .padding(top = 85.dp, bottom = 30.dp)
            .padding(horizontal = 10.dp)
    ) {
        SettingsGroup(header = stringResource(R.string.Application)) {
            Column {
                SettingsItem(
                    icon = Icons.Outlined.TouchApp,
                    title = stringResource(R.string.application_icon)
                )
                AppIconSelector(context)
            }
            Column {
                val fontScale = remember {
                    mutableFloatStateOf(0.5f)
                }

                /**
                 * Rounds this [Float] to another with 2 significant numbers
                 * 0.1234 is rounded to 0.12
                 * 0.127 is rounded to 0.13
                 */
                fun Float.roundToTwoDigits() = (this * 100.0f).roundToInt() / 100.0f

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
    }
}
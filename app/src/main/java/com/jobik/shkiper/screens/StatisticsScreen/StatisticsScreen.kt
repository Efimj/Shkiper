package com.jobik.shkiper.screens.StatisticsScreen

import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.services.statistics_service.StatisticsItem
import com.jobik.shkiper.services.statistics_service.StatisticsService
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.components.cards.StatisticsCard
import com.jobik.shkiper.ui.components.layouts.ScreenWrapper
import com.jobik.shkiper.ui.components.modals.StatisticsInformationDialog
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    val orientation = LocalConfiguration.current.orientation
    val statistics = remember { StatisticsService(context).appStatistics.getStatisticsPreviews() }
    val openedStatistics = remember { mutableStateOf<StatisticsItem?>(null) }
    var showShareDialog by remember { mutableStateOf(false) }
    val shareSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val captureController = rememberCaptureController()

    ScreenWrapper {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AppTheme.shapes.medium)
                .background(AppTheme.colors.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .allWindowInsetsPadding()
                    .padding(vertical = 40.dp)
            ) {
                Capturable(
                    controller = captureController,
                    onCaptured = { bitmap, error ->
                        if (bitmap != null) {
                            IntentHelper().shareImageToOthers(
                                context = context,
                                text = "",
                                bitmap = bitmap,
                                format = Bitmap.CompressFormat.PNG
                            )
                        }

                        if (error != null) {
                            Log.d("Capturable error", error.toString())
                            // Error occurred. Handle it!
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.colors.background)
                            .padding(horizontal = 10.dp, vertical = 40.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                stringResource(R.string.Statistics),
                                color = AppTheme.colors.text,
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }
                        for (i in 0 until 3) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                for (j in 0 until 3) {
                                    val index = i * 3 + j
                                    if (index < statistics.size) {
                                        Box(
                                            Modifier
                                                .padding(5.dp)
                                                .weight(1f)
                                        ) {
                                            StatisticsCard(statistics[index]) {
                                                openedStatistics.value = statistics[index]
                                            }
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .bottomWindowInsetsPadding()
                    .endWindowInsetsPadding()
                    .padding(35.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .padding(bottom = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.dp else 40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Card(
                        modifier = Modifier
                            .testTag("create_note_button")
                            .bounceClick()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable() {
                                showShareDialog = !showShareDialog
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = AppTheme.colors.primary,
                            contentColor = AppTheme.colors.onPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Row(
                            modifier = Modifier.height(50.dp).padding(horizontal = 14.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(R.string.Share),
                                tint = AppTheme.colors.onPrimary,
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(4.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.Share),
                                color = AppTheme.colors.onPrimary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }

    if (openedStatistics.value != null) {
        StatisticsInformationDialog(openedStatistics.value!!) { openedStatistics.value = null }
    }
    val scope = rememberCoroutineScope()

    if (showShareDialog) {
        val statisticsText = StatisticsService(context).appStatistics.getStatisticsText()

        val topInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top)
        val bottomInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)

        ModalBottomSheet(
            sheetState = shareSheetState,
            dragHandle = null,
            containerColor = Color.Transparent,
            contentColor = AppTheme.colors.text,
            windowInsets = WindowInsets.ime,
            onDismissRequest = { showShareDialog = !showShareDialog }) {
            Spacer(modifier = Modifier.windowInsetsPadding(topInsets))
            Surface(
                shape = BottomSheetDefaults.ExpandedShape,
                contentColor = AppTheme.colors.text,
                color = AppTheme.colors.background,
                tonalElevation = BottomSheetDefaults.Elevation,
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .windowInsetsPadding(bottomInsets)
                        .padding(bottom = 10.dp)
                ) {
                    SettingsItem(
                        icon = Icons.Outlined.ContentCopy,
                        title = stringResource(R.string.ShareText),
                        onClick = {
                            scope.launch { shareSheetState.hide() }.invokeOnCompletion {
                                showShareDialog = !showShareDialog
                                IntentHelper().shareTextIntent(context, statisticsText)
                            }
                        })
                    SettingsItem(
                        icon = Icons.Outlined.Screenshot,
                        title = stringResource(R.string.ShareImage),
                        onClick = {
                            scope.launch { shareSheetState.hide() }.invokeOnCompletion {
                                showShareDialog = !showShareDialog
                                captureController.capture()
                            }
                        })
                }
            }
        }
    }
}
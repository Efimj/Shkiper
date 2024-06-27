package com.jobik.shkiper.screens.statistics

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.R
import com.jobik.shkiper.helpers.IntentHelper
import com.jobik.shkiper.services.statistics.StatisticsService
import com.jobik.shkiper.ui.components.cards.StatisticsCard
import com.jobik.shkiper.ui.components.modals.BottomSheetAction
import com.jobik.shkiper.ui.components.modals.BottomSheetActions
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.endWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    val statistics = remember { StatisticsService(context).appStatistics.getStatisticsPreviews() }
    var showShareDialog by remember { mutableStateOf(false) }
    val captureController = rememberCaptureController()

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
            Column(
                modifier = Modifier
                    .capturable(captureController)
                    .fillMaxWidth()
                    .background(AppTheme.colors.background)
                    .padding(top = 40.dp, bottom = 70.dp)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = stringResource(R.string.Statistics),
                    color = AppTheme.colors.text,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    statistics.map { StatisticsCard(it) }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .bottomWindowInsetsPadding()
                .endWindowInsetsPadding()
                .padding(20.dp)
                .padding(bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .bounceClick()
                    .widthIn(140.dp)
                    .heightIn(54.dp)
                    .clip(AppTheme.shapes.small)
                    .background(AppTheme.colors.secondaryContainer)
                    .clickable {
                        showShareDialog = !showShareDialog
                    }
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = stringResource(R.string.Share),
                    tint = AppTheme.colors.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = stringResource(R.string.Share),
                    color = AppTheme.colors.onSecondaryContainer,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    ActionDialog(showShareDialog = showShareDialog, captureController = captureController) {
        showShareDialog = false
    }
}

@OptIn(ExperimentalComposeApi::class)
@Composable
private fun ActionDialog(
    showShareDialog: Boolean,
    captureController: CaptureController,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val statisticsText = StatisticsService(context).appStatistics.getStatisticsText()

    BottomSheetActions(
        isOpen = showShareDialog,
        actions = listOf(
            BottomSheetAction(
                icon = Icons.Outlined.ContentCopy,
                title = stringResource(R.string.ShareText),
                action = {
                    IntentHelper().shareTextIntent(context, statisticsText)
                },
            ),
            BottomSheetAction(
                icon = Icons.Outlined.Screenshot,
                title = stringResource(R.string.ShareImage),
                action = {
                    scope.launch {
                        val capture = captureController.captureAsync()
                        try {
                            val bitmap = capture.await()
                            IntentHelper().shareImageToOthers(
                                context = context,
                                text = "",
                                bitmap = bitmap,
                                format = Bitmap.CompressFormat.PNG
                            )
                        } catch (error: Throwable) {
                            Log.d("Capturable error", error.toString())
                            // Error occurred. Handle it!
                        }
                    }
                },
            ),
        )
    ) {
        onDismiss()
    }
}
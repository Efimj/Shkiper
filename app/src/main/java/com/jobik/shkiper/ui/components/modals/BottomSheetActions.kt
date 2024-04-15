package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.components.cards.SettingsItem
import com.jobik.shkiper.ui.theme.AppTheme
import kotlinx.coroutines.launch

data class BottomSheetAction(
    val icon: ImageVector,
    val title: String,
    val action: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetActions(
    isOpen: Boolean,
    actions: List<BottomSheetAction>,
    onDismiss: () -> Unit,
) {
    val shareSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(isOpen) {
        if (!isOpen) {
            shareSheetState.hide()
        }
    }

    if (isOpen) {
        val topInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top)
        val bottomInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)

        ModalBottomSheet(
            sheetState = shareSheetState,
            dragHandle = null,
            containerColor = Color.Transparent,
            contentColor = AppTheme.colors.text,
            windowInsets = WindowInsets.ime,
            onDismissRequest = onDismiss
        ) {
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
                        .padding(vertical = 10.dp)
                ) {
                    actions.forEach { props ->
                        SettingsItem(
                            icon = props.icon,
                            title = props.title,
                            onClick = {
                                scope.launch { shareSheetState.hide() }.invokeOnCompletion {
                                    onDismiss()
                                    props.action()
                                }
                            })
                    }
                }
            }
        }
    }
}
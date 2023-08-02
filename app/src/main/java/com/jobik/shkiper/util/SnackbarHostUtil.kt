package com.jobik.shkiper.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.ui.graphics.vector.ImageVector

data class SnackbarVisualsCustom(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    val icon: ImageVector? = null
) : SnackbarVisuals

object SnackbarHostUtil {
    val snackbarHostState = SnackbarHostState()

//    fun showSnackbar(snackbarData: SnackbarVisualsCustom) {
//        snackbarHostState.showSnackbar(
//            snackbarData
//        )
//    }
}
package com.jobik.shkiper.ui.components.modals.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun OnboardingDialog(onFinish: () -> Unit) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {}
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                Button(onClick = { onFinish() }) {
                Text(text = "Finish")
            }
                OnBoardingScreen()
            }
        }
    }
}
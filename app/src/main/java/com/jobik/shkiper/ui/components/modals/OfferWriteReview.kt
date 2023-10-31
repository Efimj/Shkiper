package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.R
import com.jobik.shkiper.services.review_service.ReviewService
import com.jobik.shkiper.ui.components.buttons.RoundedButton
import com.jobik.shkiper.ui.theme.CustomTheme
import kotlinx.coroutines.delay

@Composable
fun OfferWriteReview(
    onGoBack: () -> Unit,
) {
    val timeHasPassed = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(3000)
        timeHasPassed.value = true
    }

    val goBackFunDelay = { if (timeHasPassed.value) onGoBack() }

    Dialog(goBackFunDelay, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier.clip(RoundedCornerShape(15.dp)).background(CustomTheme.colors.secondaryBackground)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(210.dp).background(CustomTheme.colors.mainBackground)
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.two_phones_preview),
                    contentDescription = stringResource(R.string.Image),
                    contentScale = ContentScale.Fit
                )
            }
            Column(
                modifier = Modifier.padding(top = 10.dp).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.OfferWriteReviewTitle),
                    color = CustomTheme.colors.text,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = stringResource(R.string.OfferWriteReviewDescription),
                    color = CustomTheme.colors.textSecondary,
                    style = MaterialTheme.typography.body1,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RoundedButton(
                            text = stringResource(R.string.Cancel),
                            onClick = goBackFunDelay,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                disabledBackgroundColor = Color.Transparent
                            ),
                            enabled = timeHasPassed.value,
                            border = null,
                            textColor = if (timeHasPassed.value) CustomTheme.colors.text else CustomTheme.colors.textSecondary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(Modifier.width(7.dp))
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val context = LocalContext.current
                        RoundedButton(
                            text = stringResource(R.string.RateTheApp),
                            onClick = { ReviewService(context).openRateScreen(); onGoBack() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = CustomTheme.colors.active,
                                disabledBackgroundColor = Color.Transparent
                            ),
                            border = null,
                            textColor = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
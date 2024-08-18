package com.jobik.shkiper.ui.components.modals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.ButtonStyle
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.components.buttons.DefaultButtonProperties
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun OfferWriteReview(
    onGoBack: () -> Unit,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(AppTheme.colors.container)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.three_stars),
                    contentDescription = stringResource(R.string.Image),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                )
            }
            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.OfferWriteReviewTitle),
                    color = AppTheme.colors.text,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = stringResource(R.string.OfferWriteReviewDescription),
                    color = AppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(15.dp))
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
                        CustomButton(
                            text = stringResource(R.string.Cancel),
                            onClick = onGoBack,
                            properties = DefaultButtonProperties(
                                buttonColors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent
                                ),
                                border = null,
                                textColor = AppTheme.colors.text,
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(Modifier.width(7.dp))
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CustomButton(
                            text = stringResource(R.string.RateTheApp),
                            onClick = {
                                uriHandler.openUri(context.getString(R.string.shkiper_google_play_link))
                                onGoBack()
                            },
                            style = ButtonStyle.Filled,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
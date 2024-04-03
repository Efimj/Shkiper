package com.jobik.shkiper.ui.components.modals

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jobik.shkiper.R
import com.jobik.shkiper.ui.components.buttons.ButtonStyle
import com.jobik.shkiper.ui.components.buttons.CustomButton
import com.jobik.shkiper.ui.theme.CustomTheme

data class ImageActionDialogButton(
    val text: String? = null,
    val icon: ImageVector? = null,
    val isActive: Boolean = false,
    val onClick: () -> Unit,
)

@Composable
fun ImageActionDialog(
    @DrawableRes
    image: Int? = null,
    header: String? = null,
    text: String? = null,
    onGoBack: () -> Unit,
    confirmButton: ImageActionDialogButton,
    backButton: ImageActionDialogButton? = null
) {
    Dialog(onGoBack, DialogProperties(true, dismissOnClickOutside = true)) {
        Column(
            Modifier.clip(RoundedCornerShape(15.dp)).background(CustomTheme.colors.container)
        ) {
            image?.let { image ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(210.dp)
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = image),
                        contentDescription = stringResource(R.string.Image),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Column(
                modifier = Modifier.padding(top = 20.dp).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                header?.let { header ->
                    Text(
                        text = header,
                        color = CustomTheme.colors.text,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                text?.let { text ->
                    Text(
                        text = text,
                        color = CustomTheme.colors.textSecondary,
                        style = MaterialTheme.typography.body1,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    backButton?.let { button ->
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CustomButton(
                                text = button.text,
                                onClick = button.onClick,
                                icon = button.icon,
                                style = if (button.isActive) ButtonStyle.Filled else ButtonStyle.Text,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(Modifier.width(7.dp))
                    }
                    confirmButton.let { button ->
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CustomButton(
                                text = button.text,
                                onClick = button.onClick,
                                icon = button.icon,
                                style = if (button.isActive) ButtonStyle.Filled else ButtonStyle.Text,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
package com.jobik.shkiper.crash

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jobik.shkiper.BuildConfig
import com.jobik.shkiper.R
import com.jobik.shkiper.activity.StartupActivity
import com.jobik.shkiper.screens.layout.navigation.DefaultNavigationValues
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.bottomWindowInsetsPadding
import com.jobik.shkiper.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.ui.theme.ShkiperTheme
import com.jobik.shkiper.util.ContextUtils
import com.jobik.shkiper.util.ContextUtils.adjustFontSize
import com.jobik.shkiper.util.settings.NightMode
import com.jobik.shkiper.util.settings.SettingsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrashActivity : CrashHandler() {
    override fun attachBaseContext(newBase: Context) {
        SettingsManager.init(newBase)
        super.attachBaseContext(
            ContextUtils.setLocale(
                context = newBase,
                language = SettingsManager.settings.localization
            )
        )
    }

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        adjustFontSize(SettingsManager.settings.fontScale)
        actionBar?.hide()

        val crashReason = getCrashReason()
        val exName = crashReason.split("\n\n")[0].trim()
        val ex = crashReason.split("\n\n").drop(1).joinToString("\n\n")

        val title = "[Bug] Application Crash: $exName"
        val deviceInfo =
            "Device: ${Build.MODEL} (${Build.BRAND} - ${Build.DEVICE}) \nSDK: ${Build.VERSION.SDK_INT} (${Build.VERSION.RELEASE}) \nApp: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n\n"
        val body = "$deviceInfo$ex"

        setContent {
            ShkiperTheme(
                darkTheme = when (SettingsManager.settings.nightMode) {
                    NightMode.Light -> false
                    NightMode.Dark -> true
                    else -> isSystemInDarkTheme()
                },
                style = SettingsManager.settings.theme
            ) {
                val clipboardManager = LocalClipboardManager.current
                val context = LocalContext.current

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 100.dp)
                            .allWindowInsetsPadding()
                            .fillMaxSize()
                            .background(AppTheme.colors.background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(40.dp))
                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(60.dp),
                                imageVector = Icons.Outlined.BugReport,
                                contentDescription = null,
                                tint = AppTheme.colors.text
                            )
                            Text(
                                text = stringResource(R.string.crash_detected),
                                color = AppTheme.colors.text,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                            Text(
                                text = stringResource(R.string.crash_detect_description),
                                color = AppTheme.colors.textSecondary,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        FlowRow(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            LinkCard(
                                icon = ImageVector.vectorResource(id = R.drawable.ic_telegram),
                                text = stringResource(R.string.telegram_chat)
                            ) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(context.getString(R.string.telegram_link))
                                    )
                                )
                                clipboardManager.setText(AnnotatedString(("$title + \n\n + $body")))
                            }
                            LinkCard(
                                icon = ImageVector.vectorResource(id = R.drawable.ic_github),
                                text = stringResource(R.string.issue)
                            ) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("${context.getString(R.string.github_issue_tracker_link)}/new?title=$title&body=$body")
                                    )
                                )
                                clipboardManager.setText(AnnotatedString(("$title + \n\n + $body")))
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .clip(AppTheme.shapes.medium)
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(("$title + \n\n + $body")))
                                    val toast =
                                        Toast.makeText(
                                            /* context = */ context,
                                            /* text = */
                                            context.getText(R.string.copied_to_clipboard),
                                            /* duration = */
                                            Toast.LENGTH_SHORT
                                        )
                                    toast.show()
                                }
                                .background(AppTheme.colors.container)
                                .padding(20.dp)
                        ) {
                            Text(
                                text = exName,
                                color = AppTheme.colors.textSecondary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = ex,
                                color = AppTheme.colors.textSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp)
                            .bottomWindowInsetsPadding()
                            .horizontalWindowInsetsPadding()
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(modifier = Modifier.height(60.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = AppTheme.colors.onSecondaryContainer,
                                    containerColor = AppTheme.colors.secondaryContainer
                                ),
                                onClick = {
                                    startActivity(
                                        Intent(
                                            this@CrashActivity,
                                            StartupActivity::class.java
                                        )
                                    )
                                }) {
                                Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                                    Icon(
                                        imageVector = Icons.Outlined.RestartAlt,
                                        contentDescription = null,
                                        tint = AppTheme.colors.onSecondaryContainer
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        modifier = Modifier.basicMarquee(),
                                        text = stringResource(R.string.restart),
                                        maxLines = 1,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            Surface(
                                modifier = Modifier
                                    .bounceClick()
                                    .height(DefaultNavigationValues().containerHeight)
                                    .aspectRatio(1f)
                                    .clickable {
                                        clipboardManager.setText(AnnotatedString(("$title + \n\n + $body")))
                                        val toast =
                                            Toast.makeText(
                                                /* context = */ context,
                                                /* text = */
                                                context.getText(R.string.copied_to_clipboard),
                                                /* duration = */
                                                Toast.LENGTH_SHORT
                                            )
                                        toast.show()
                                    },
                                shape = MaterialTheme.shapes.small,
                                shadowElevation = 1.dp,
                                color = AppTheme.colors.primary,
                                contentColor = AppTheme.colors.onPrimary
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.CopyAll,
                                        contentDescription = null,
                                        tint = AppTheme.colors.onPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LinkCard(
        modifier: Modifier = Modifier,
        icon: ImageVector,
        text: String,
        onClick: () -> Unit,
    ) {
        Row(
            modifier = modifier
                .bounceClick()
                .clip(CircleShape)
                .clickable { onClick() }
                .background(AppTheme.colors.container)
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(28.dp),
                imageVector = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(AppTheme.colors.text)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = text,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.colors.text
            )
        }

    }
}
package com.jobik.shkiper.screens.purchase

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material.icons.outlined.SignalWifiOff
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.billingclient.api.ProductDetails
import com.jobik.shkiper.R
import com.jobik.shkiper.services.billing.AppProducts
import com.jobik.shkiper.ui.components.layouts.ScreenStub
import com.jobik.shkiper.ui.components.modals.FullscreenPopup
import com.jobik.shkiper.ui.helpers.allWindowInsetsPadding
import com.jobik.shkiper.ui.modifiers.bounceClick
import com.jobik.shkiper.ui.theme.AppTheme
import com.jobik.shkiper.util.ContextUtils
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun PurchaseScreen(purchaseViewModel: PurchaseViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val hasInternetConnection by remember {
        mutableStateOf(
            ContextUtils.hasInternetConnection(
                context
            )
        )
    }

    Congratulations(purchaseViewModel)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
    ) {
        if (hasInternetConnection.not()) {
            ScreenStub(
                title = R.string.CheckInternetConnection,
                icon = Icons.Outlined.SignalWifiOff
            )
        } else {
            ScreenContent(purchaseViewModel = purchaseViewModel)
        }
    }
}

@Composable
private fun Congratulations(purchaseViewModel: PurchaseViewModel) {
    val titleRes by rememberSaveable {
        mutableIntStateOf(
            listOf(
                R.string.ThankForPurchase1,
                R.string.ThankForPurchase2,
                R.string.ThankForPurchase3
            ).random()
        )
    }

    fun parade(): List<Party> {
        val party = Party(
            speed = 10f,
            maxSpeed = 30f,
            damping = 0.9f,
            angle = Angle.RIGHT - 45,
            spread = Spread.SMALL,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30),
            position = Position.Relative(0.0, 0.35)
        )

        return listOf(
            party,
            party.copy(
                angle = party.angle - 90, // flip angle from right to left
                position = Position.Relative(1.0, 0.35)
            ),
        )
    }

    val animationState = remember { MutableTransitionState(false) }
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(purchaseViewModel.screenState.value.showGratitude) {
        if (visible.value.not()) {
            visible.value = true
        }
        animationState.targetState = purchaseViewModel.screenState.value.showGratitude
    }

    LaunchedEffect(animationState.isIdle) {
        if (animationState.isIdle) {
            visible.value = purchaseViewModel.screenState.value.showGratitude
        }
    }

    if (visible.value) {
        FullscreenPopup(
            onDismiss = { },
        ) {
            AnimatedVisibility(
                visibleState = animationState,
                enter = fadeIn() + scaleIn(initialScale = .95f),
                exit = fadeOut() + scaleOut(targetScale = .95f)
            ) {
                BackHandler(true) {}
                var buttonHideShow by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(1500)
                    buttonHideShow = true
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {}
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .widthIn(max = 450.dp)
                            .fillMaxWidth()
                            .clip(AppTheme.shapes.large)
                            .background(AppTheme.colors.secondaryContainer)
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Box(Modifier.padding(horizontal = 10.dp)) {
                                Icon(
                                    modifier = Modifier.size(70.dp),
                                    imageVector = Icons.Outlined.VolunteerActivism,
                                    contentDescription = null,
                                    tint = AppTheme.colors.primary
                                )
                            }
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(titleRes),
                                color = AppTheme.colors.onSecondaryContainer,
                                fontWeight = FontWeight.SemiBold,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = buttonHideShow,
                            enter = slideInVertically { it } + expandVertically { -it },
                            exit = slideOutVertically { it } + shrinkVertically { -it })
                        {
                            Button(modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = AppTheme.colors.onPrimary,
                                    containerColor = AppTheme.colors.primary
                                ),
                                onClick = { purchaseViewModel.hideCompletedPurchase() }) {
                                Text(
                                    modifier = Modifier.basicMarquee(),
                                    text = stringResource(R.string.thank_you),
                                    maxLines = 1,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    KonfettiView(
                        modifier = Modifier.fillMaxSize(),
                        parties = parade(),
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenContent(purchaseViewModel: PurchaseViewModel) {
    val context = LocalContext.current

    var highlight by remember { mutableIntStateOf(0) }
    val count = 7

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            highlight = (highlight + 1) % count
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .allWindowInsetsPadding()
            .padding(top = 85.dp, bottom = 40.dp)
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.secondaryContainer)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.support_the_project),
                color = AppTheme.colors.onSecondaryContainer,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.support_project_description),
                color = AppTheme.colors.onSecondaryContainer,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                purchaseViewModel.screenState.value.purchases.forEachIndexed { index, it ->
                    ProductCard(
                        modifier = Modifier.weight(1f),
                        highlight = index == highlight,
                        isBought = purchaseViewModel.checkIsProductPurchased(it.productId),
                        product = it
                    ) {
                        purchaseViewModel.makePurchase(
                            productDetails = it,
                            activity = context as Activity
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val sub = purchaseViewModel.screenState.value.subscriptions.firstOrNull()

                sub?.subscriptionOfferDetails?.forEachIndexed { index, subDetail ->
                    subDetail?.let {
                        SubscriptionCard(
                            modifier = Modifier.weight(1f),
                            highlight = index + 2 == highlight,
                            product = subDetail
                        ) {
                            purchaseViewModel.makePurchaseSubscription(
                                productDetails = sub,
                                subscriptionOfferDetails = subDetail,
                                activity = context as Activity
                            )
                        }
                    }
                }
            }
        }

        val clipboardManager = LocalClipboardManager.current
        val scope = rememberCoroutineScope()

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CryptoWalletCard(
                isHighlight = highlight == 4,
                image = R.drawable.ic_btc,
                walletName = stringResource(id = R.string.btc),
                walletAddress = stringResource(id = R.string.wallet_address_btc)
            ) {
                copyToClipboard(
                    clipboardManager = clipboardManager,
                    context = context,
                    scope = scope,
                    walletAddress = context.getString(R.string.wallet_address_btc),
                    walletName = context.getString(R.string.btc)
                )
            }
            CryptoWalletCard(
                isHighlight = highlight == 5,
                image = R.drawable.ic_eth,
                walletName = stringResource(id = R.string.eth),
                walletAddress = stringResource(id = R.string.wallet_address_eth)
            ) {
                copyToClipboard(
                    clipboardManager = clipboardManager,
                    context = context,
                    scope = scope,
                    walletAddress = context.getString(R.string.wallet_address_eth),
                    walletName = context.getString(R.string.eth)
                )
            }
            CryptoWalletCard(
                isHighlight = highlight == 6,
                image = R.drawable.ic_usdt,
                walletName = stringResource(id = R.string.usdt),
                walletAddress = stringResource(id = R.string.wallet_address_usdt)
            ) {
                copyToClipboard(
                    clipboardManager = clipboardManager,
                    context = context,
                    scope = scope,
                    walletAddress = context.getString(R.string.wallet_address_usdt),
                    walletName = context.getString(R.string.usdt)
                )
            }
        }
    }
}

private fun copyToClipboard(
    clipboardManager: ClipboardManager,
    context: Context,
    scope: CoroutineScope,
    walletName: String,
    walletAddress: String,
    icon: ImageVector = Icons.Outlined.CopyAll
) {
    clipboardManager.setText(AnnotatedString(walletAddress))
    scope.launch {
        SnackbarHostUtil.snackbarHostState.showSnackbar(
            SnackbarVisualsCustom(
                message = context.getString(R.string.copied_to_clipboard) + " " + walletName,
                icon = icon
            )
        )
    }
}

@Composable
private fun CryptoWalletCard(
    isHighlight: Boolean = false,
    @DrawableRes image: Int,
    walletName: String,
    walletAddress: String,
    onClick: () -> Unit
) {
    val backgroundColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.secondaryContainer else AppTheme.colors.container,
        tween(500)
    )

    val titleColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.onSecondaryContainer else AppTheme.colors.text,
        tween(500)
    )

    val descriptionColor by
    animateColorAsState(
        targetValue = if (isHighlight) AppTheme.colors.textSecondary else AppTheme.colors.textSecondary,
        tween(500)
    )

    val scale by animateFloatAsState(if (isHighlight) 1.05f else 1f, tween(500))

    Row(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .bounceClick()
            .clip(AppTheme.shapes.large)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Image(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = walletName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = walletAddress,
                style = MaterialTheme.typography.bodyMedium,
                color = descriptionColor,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Icon(
            imageVector = Icons.Outlined.CopyAll,
            contentDescription = null,
            tint = AppTheme.colors.textSecondary
        )
    }
}

@Composable
private fun ProductCard(
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    isBought: Boolean = false,
    product: ProductDetails,
    onClick: (ProductDetails) -> Unit
) {
    val type = stringResource(R.string.purchase)
    val description = stringResource(R.string.support)
    val price = product.oneTimePurchaseOfferDetails?.formattedPrice ?: ""

    val backgroundColor by
    animateColorAsState(
        targetValue = if (highlight) AppTheme.colors.secondaryContainer else AppTheme.colors.container,
        tween(500)
    )

    val titleColor by
    animateColorAsState(
        targetValue = if (highlight) AppTheme.colors.onSecondaryContainer else AppTheme.colors.text,
        tween(500)
    )

    val scale by animateFloatAsState(if (highlight) 1.05f else 1f, tween(500))

    Column(
        modifier = modifier
            .bounceClick()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(AppTheme.shapes.medium)
            .background(backgroundColor)
            .clickable { onClick(product) }
            .padding(15.dp),
    ) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier
                        .basicMarquee(),
                    text = type,
                    color = AppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier
                        .height(50.dp)
                        .basicMarquee(),
                    text = description,
                    color = titleColor,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }
            if (isBought) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AppTheme.colors.primary)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Verified,
                        contentDescription = null,
                        tint = AppTheme.colors.onPrimary
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(AppTheme.colors.primary)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.basicMarquee(),
                text = price,
                color = AppTheme.colors.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SubscriptionCard(
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    isBought: Boolean = false,
    product: ProductDetails.SubscriptionOfferDetails,
    onClick: (ProductDetails.SubscriptionOfferDetails) -> Unit
) {
    val type = stringResource(R.string.subscription)
    val description = stringResource(R.string.ongoing_support)
    val price = product.pricingPhases.pricingPhaseList.first().formattedPrice
    val subPeriod = when (product.basePlanId) {
        AppProducts.Yearly -> stringResource(R.string.yearly)
        AppProducts.Monthly -> stringResource(R.string.monthly)
        else -> ""
    }

    val backgroundColor by
    animateColorAsState(
        targetValue = if (highlight) AppTheme.colors.secondaryContainer else AppTheme.colors.container,
        tween(500)
    )

    val titleColor by
    animateColorAsState(
        targetValue = if (highlight) AppTheme.colors.onSecondaryContainer else AppTheme.colors.text,
        tween(500)
    )

    val scale by animateFloatAsState(if (highlight) 1.05f else 1f, tween(500))

    Column(
        modifier = modifier
            .bounceClick()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(AppTheme.shapes.medium)
            .background(backgroundColor)
            .clickable { onClick(product) }
            .padding(15.dp),
    ) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    text = type,
                    color = AppTheme.colors.textSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    text = description,
                    color = titleColor,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .basicMarquee(),
                    text = subPeriod,
                    color = titleColor,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(AppTheme.colors.primary)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.basicMarquee(),
                text = price,
                color = AppTheme.colors.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                maxLines = 1
            )
        }
    }
}
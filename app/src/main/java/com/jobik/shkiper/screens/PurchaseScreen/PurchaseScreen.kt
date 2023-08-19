package com.jobik.shkiper.screens.PurchaseScreen

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.outlined.SignalWifiOff
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jobik.shkiper.R
import com.jobik.shkiper.services.billing_service.AppProducts
import com.jobik.shkiper.ui.components.cards.ProductPurchaseCardContent
import com.jobik.shkiper.ui.components.cards.PurchaseCard
import com.jobik.shkiper.ui.components.cards.PurchaseCardContent
import com.jobik.shkiper.ui.components.cards.TitlePurchaseCardContent
import com.jobik.shkiper.ui.components.layouts.ScreenContentIfNoData
import com.jobik.shkiper.ui.components.layouts.ScreenWrapper
import com.jobik.shkiper.ui.components.modals.ImageActionDialog
import com.jobik.shkiper.ui.components.modals.ImageActionDialogButton
import com.jobik.shkiper.ui.theme.CustomAppTheme
import kotlin.random.Random

@Composable
fun PurchaseScreen(purchaseViewModel: PurchaseViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val connectivityManager = remember { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    val isNetworkActive =
        remember { connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected }

    if (purchaseViewModel.screenState.value.showGratitude) {
        val stringResources = listOf(R.string.ThankForPurchase1, R.string.ThankForPurchase2, R.string.ThankForPurchase3)
        ImageActionDialog(
            header = stringResource(stringResources[Random.nextInt(stringResources.size)]),
            onGoBack = {},
            confirmButton = ImageActionDialogButton(
                icon = Icons.Default.Favorite,
                isActive = true,
                onClick = purchaseViewModel::hideCompletedPurchase
            )
        )
    }

    if (!isNetworkActive)
        ScreenContentIfNoData(R.string.CheckInternetConnection, Icons.Outlined.SignalWifiOff)
    else if (purchaseViewModel.screenState.value.purchases.isEmpty() && purchaseViewModel.screenState.value.subscription != null)
        ScreenContentIfNoData(R.string.CheckUpdatesGooglePlay, Icons.Default.Shop)
    else
        ScreenWrapper(modifier = Modifier.verticalScroll(rememberScrollState()).padding(top = 85.dp, bottom = 30.dp)) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                Text(
                    stringResource(R.string.PurchaseScreenTitle),
                    color = CustomAppTheme.colors.text,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 15.dp).padding(bottom = 15.dp).fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.PurchaseScreenDescription),
                    color = CustomAppTheme.colors.text,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                )
            }
            Text(
                stringResource(R.string.BuyMe),
                color = CustomAppTheme.colors.text,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 4.dp).fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 7.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val productList = listOf(
                    purchaseViewModel.screenState.value.purchases.find { it.productId == AppProducts.CupOfTea }
                        ?.let { productDetails ->
                            ProductPurchaseCardContent(
                                product = productDetails,
                                imageRes = R.drawable.ic_notification,
                                isPurchased = purchaseViewModel.checkIsProductPurchased(productDetails.productId)
                            )
                        },
                    purchaseViewModel.screenState.value.purchases.find { it.productId == AppProducts.SweetsForMyCat }
                        ?.let { productDetails ->
                            ProductPurchaseCardContent(
                                product = productDetails,
                                imageRes = R.drawable.ic_notification,
                                isHighlighted = true,
                                isPurchased = purchaseViewModel.checkIsProductPurchased(productDetails.productId)
                            )
                        },
                    purchaseViewModel.screenState.value.purchases.find { it.productId == AppProducts.GymMembership }
                        ?.let { productDetails ->
                            ProductPurchaseCardContent(
                                product = productDetails,
                                imageRes = R.drawable.ic_notification,
                                isPurchased = purchaseViewModel.checkIsProductPurchased(productDetails.productId)
                            )
                        }
                )
                for (product in productList)
                    if (product != null)
                        Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                            PurchaseCard(product) {
                                purchaseViewModel.makePurchase(product.product, context as Activity)
                            }
                        }
            }
            Spacer(Modifier.height(4.dp))
            purchaseViewModel.screenState.value.subscription?.let { productDetails ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.BuySubscription),
                        color = CustomAppTheme.colors.text,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(bottom = 4.dp)
                            .padding(top = 12.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    if (purchaseViewModel.checkIsProductPurchased(productDetails.productId))
                        Icon(
                            imageVector = Icons.Outlined.TaskAlt,
                            contentDescription = null,
                            tint = CustomAppTheme.colors.active,
                        )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    productDetails.subscriptionOfferDetails?.find { it.basePlanId == AppProducts.Monthly }
                        ?.let { subscriptionOffer ->
                            Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                                PurchaseCard(
                                    TitlePurchaseCardContent(
                                        titleRes = R.string.Monthly,
                                        imageRes = R.drawable.ic_notification,
                                        isPurchased = false
                                    )
                                ) {
                                    purchaseViewModel.makePurchaseSubscription(
                                        productDetails,
                                        subscriptionOffer,
                                        context as Activity
                                    )
                                }
                            }
                        }
                    productDetails.subscriptionOfferDetails?.find { it.basePlanId == AppProducts.Yearly }
                        ?.let { subscriptionOffer ->
                            Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                                PurchaseCard(
                                    TitlePurchaseCardContent(
                                        titleRes = R.string.Annually,
                                        imageRes = R.drawable.ic_notification,
                                        isPurchased = false
                                    )
                                ) {
                                    purchaseViewModel.makePurchaseSubscription(
                                        productDetails,
                                        subscriptionOffer,
                                        context as Activity
                                    )
                                }
                            }
                        }
                }
            }
        }
}
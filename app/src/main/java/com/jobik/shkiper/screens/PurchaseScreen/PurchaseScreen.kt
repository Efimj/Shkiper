package com.jobik.shkiper.screens.PurchaseScreen

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.SignalWifiOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jobik.shkiper.R
import com.jobik.shkiper.services.billing_service.AppProducts
import com.jobik.shkiper.services.billing_service.PurchaseEvent
import com.jobik.shkiper.ui.components.cards.PurchaseCard
import com.jobik.shkiper.ui.components.cards.PurchaseCardContent
import com.jobik.shkiper.ui.components.layouts.ScreenContentIfNoData
import com.jobik.shkiper.ui.components.modals.ImageActionDialog
import com.jobik.shkiper.ui.components.modals.ImageActionDialogButton
import com.jobik.shkiper.ui.theme.CustomAppTheme

@Composable
fun PurchaseScreen(purchaseViewModel: PurchaseViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val connectivityManager = remember { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    if (purchaseViewModel.screenState.value.showGratitude)
        ImageActionDialog(
            image = R.drawable.two_phones_preview,
            header = "Thenks",
            onGoBack = {},
            confirmButton = ImageActionDialogButton(
                text = "sd",
                icon = Icons.Default.Favorite,
                isActive = true,
                onClick = purchaseViewModel::hideCompletedPurchase
            )
        )

    if (!connectivityManager.isDefaultNetworkActive)
        ScreenContentIfNoData(R.string.CheckInternetConnection, Icons.Outlined.SignalWifiOff)
    else if (purchaseViewModel.screenState.value.purchases.isEmpty() && purchaseViewModel.screenState.value.subscriptions.isEmpty())
        ScreenContentIfNoData(R.string.CheckUpdatesGooglePlay, Icons.Default.Shop)
    else
        ColumnForItems {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 85.dp, bottom = 10.dp)) {
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
                            PurchaseCardContent(
                                product = productDetails,
                                image = R.drawable.ic_notification,
                                isPurchased = purchaseViewModel.checkIsProductPurchased(productDetails.productId)
                            )
                        },
                    purchaseViewModel.screenState.value.purchases.find { it.productId == AppProducts.PepperoniPizza }
                        ?.let { productDetails ->
                            PurchaseCardContent(
                                product = productDetails,
                                image = R.drawable.ic_notification,
                                isHighlighted = true,
                                isPurchased = purchaseViewModel.checkIsProductPurchased(productDetails.productId)
                            )
                        },
                    purchaseViewModel.screenState.value.purchases.find { it.productId == AppProducts.GymPass }
                        ?.let { productDetails ->
                            PurchaseCardContent(
                                product = productDetails,
                                image = R.drawable.ic_notification,
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
            Text(
                stringResource(R.string.BuySubscription),
                color = CustomAppTheme.colors.text,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 4.dp).padding(top = 12.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 7.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val productList = listOf(
                    purchaseViewModel.screenState.value.subscriptions.find { it.productId == AppProducts.Monthly }
                        ?.let { productDetails ->
                            PurchaseCardContent(
                                product = productDetails,
                                image = R.drawable.ic_notification,
                                isPurchased = purchaseViewModel.checkIsProductPurchased(productDetails.productId)
                            )
                        },
                )
                for (product in productList)
                    if (product != null)
                        Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                            PurchaseCard(product) {
                                purchaseViewModel.makePurchase(product.product, context as Activity)
                            }
                        }
            }
        }
}

@Composable
private fun ColumnForItems(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 550.dp).verticalScroll(rememberScrollState()).padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
        }
    }
}
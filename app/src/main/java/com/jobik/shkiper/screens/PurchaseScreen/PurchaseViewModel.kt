package com.jobik.shkiper.screens.PurchaseScreen

import android.app.Activity
import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.jobik.shkiper.NotepadApplication
import com.jobik.shkiper.R
import com.jobik.shkiper.services.billing_service.PurchaseCallback
import com.jobik.shkiper.util.SnackbarHostUtil
import com.jobik.shkiper.util.SnackbarVisualsCustom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PurchaseScreenState(
    val purchases: List<ProductDetails> = emptyList(),
    val subscriptions: List<ProductDetails> = emptyList(),
    val productsPurchasesHistory: List<PurchaseHistoryRecord> = emptyList(),
    val subscriptionsPurchasesHistory: List<PurchaseHistoryRecord> = emptyList(),
    val showGratitude: Boolean = false,
)

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val application: Application,
) : ViewModel(), PurchaseCallback {
    private val _screenState = mutableStateOf(PurchaseScreenState())
    val screenState: State<PurchaseScreenState> = _screenState
    private val billingClient = (application as NotepadApplication).billingClientLifecycle

    init {
        _screenState.value = _screenState.value.copy(
            purchases = billingClient.productDetails.value,
            subscriptions = billingClient.subscriptionsDetails.value,
            productsPurchasesHistory = billingClient.productsPurchasesHistory.value,
            subscriptionsPurchasesHistory = billingClient.subscriptionsPurchasesHistory.value
        )
        billingClient.registerPurchaseCallback(this)
    }

    override fun onPurchaseResult(resultCode: Int, purchases: List<Purchase>?) {
        when (resultCode) {
            BillingResponseCode.OK -> {
                showCompletedPurchase()
                updatePurchasesHistory()
            }

            BillingResponseCode.USER_CANCELED -> {

            }

            BillingResponseCode.ITEM_ALREADY_OWNED -> {
                viewModelScope.launch {
                    showSnackbar(
                        message = application.applicationContext.getString(R.string.ItemAlreadyOwned),
                        icon = Icons.Default.Info
                    )
                }
            }

            BillingResponseCode.DEVELOPER_ERROR -> {
                viewModelScope.launch {
                    showSnackbar(
                        message = application.applicationContext.getString(R.string.UnspecifiedErrorOccurred),
                        icon = Icons.Default.Info
                    )
                }
            }
        }
    }

    private fun updatePurchasesHistory() {
        _screenState.value = _screenState.value.copy(
            productsPurchasesHistory = billingClient.productsPurchasesHistory.value,
            subscriptionsPurchasesHistory = billingClient.subscriptionsPurchasesHistory.value
        )
    }

    fun makePurchase(productDetails: ProductDetails, activity: Activity) {
        val billingResult = billingClient.makePurchase(activity, productDetails)
    }

    fun checkIsProductPurchased(productId: String): Boolean {
        return screenState.value.productsPurchasesHistory.any { historyRecord ->
            historyRecord.products.any {
                productId == it
            }
        } || screenState.value.subscriptionsPurchasesHistory.any { historyRecord ->
            historyRecord.products.any {
                productId == it
            }
        }
    }

    private fun showCompletedPurchase() {
        _screenState.value = _screenState.value.copy(
            showGratitude = true,
        )
    }

    fun hideCompletedPurchase() {
        _screenState.value = _screenState.value.copy(
            showGratitude = false,
        )
    }

    private suspend fun showSnackbar(message: String, icon: ImageVector?) {
        SnackbarHostUtil.snackbarHostState.showSnackbar(
            SnackbarVisualsCustom(
                message = message,
                icon = icon
            )
        )
    }
}
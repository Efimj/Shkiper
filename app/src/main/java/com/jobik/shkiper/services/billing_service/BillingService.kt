package com.jobik.shkiper.services.billing_service

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.*
import kotlinx.coroutines.*
import java.lang.Runnable
import kotlin.math.pow

class BillingService private constructor(
    private val applicationContext: Context,
    private val externalScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : PurchasesUpdatedListener, BillingClientStateListener, DefaultLifecycleObserver {

    var productDetails = mutableStateOf(emptyList<ProductDetails>())

    /**
     * Instantiate a new BillingClient instance.
     */
    private lateinit var billingClient: BillingClient
    override fun onCreate(owner: LifecycleOwner) {
        Log.d(TAG, "ON_CREATE")
        // Create a new BillingClient in onCreate().
        // Since the BillingClient can only be used once, we need to create a new instance
        // after ending the previous connection to the Google Play Store in onDestroy().
        billingClient = BillingClient.newBuilder(applicationContext)
            .setListener(this)
            .enablePendingPurchases() // Not used for subscriptions.
            .build()
        if (!billingClient.isReady) {
            Log.d(TAG, "BillingClient: Start connection...")
            billingClient.startConnection(this)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, "ON_DESTROY")
        if (billingClient.isReady) {
            Log.d(TAG, "BillingClient can only be used once -- closing connection")
            // BillingClient can only be used once.
            // After calling endConnection(), we must create a new BillingClient.
            billingClient.endConnection()
        }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        Log.d(TAG, "onBillingSetupFinished: $responseCode $debugMessage")
        if (responseCode == BillingClient.BillingResponseCode.OK) {
            // The billing client is ready.
            // You can query product details and purchases here.
            queryProductDetails()
//            querySubscriptionProductDetails()
//            queryOneTimeProductDetails()
//            querySubscriptionPurchases()
//            queryOneTimeProductPurchases()
        }
    }

    override fun onBillingServiceDisconnected() {
        Log.d(TAG, "onBillingServiceDisconnected")
        val initialDelayMillis = 1000L // Initial delay in milliseconds
        var currentRetry = 0

        val handler = Handler(Looper.getMainLooper())

        val retryRunnable = object : Runnable {
            override fun run() {
                if (currentRetry < MAX_RETRY_ATTEMPT) {
                    currentRetry++
                    Log.d(TAG, "Retrying connection, attempt $currentRetry")
                    billingClient.startConnection(billingClientStateListener)
                    handler.postDelayed(this, initialDelayMillis * (2.0.pow(currentRetry.toDouble())).toLong())
                } else {
                    Log.d(TAG, "Exceeded maximum retries")
                }
            }
        }

        handler.postDelayed(retryRunnable, initialDelayMillis)
    }

    private object billingClientStateListener : BillingClientStateListener {
        override fun onBillingSetupFinished(billingResult: BillingResult) {
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // The BillingClient is ready. You can query purchases here.
            }
        }

        override fun onBillingServiceDisconnected() {
            // Try to restart the connection on the next request to
            // Google Play by calling the startConnection() method.
        }
    }

    private fun startConnection() {
        billingClient.startConnection(billingClientStateListener)
    }

    private fun queryProductDetails() {
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    AppProducts.ListOfProducts.map { prodId ->
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(prodId)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    }
                )
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            // check billingResult
            // process returned productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK)
                productDetails.value = productDetailsList
            else
                Log.d(TAG, "Retrying connection, attempt $billingResult.responseCode")
        }
    }

    fun makePurchase(activity: Activity, productDetails: ProductDetails): BillingResult {
        val productDetailsParamsList =
            if (productDetails.productType == BillingClient.ProductType.INAPP)
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
            else
                listOf(
                    productDetails.subscriptionOfferDetails?.get(0)?.let {
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetails)
                            // to get an offer token, call ProductDetails.subscriptionOfferDetails()
                            // for a list of offers that are available to the user
                            .setOfferToken(it.offerToken)
                            .build()
                    }
                )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        return billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    /**
     * Called by the Billing Library when new purchases are detected.
     */
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        Log.d(TAG, "onPurchasesUpdated: $responseCode $debugMessage")
        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                // ToDo Make response
                if (purchases != null) {
                    externalScope.launch {
                        handlePurchase(purchases)
                    }
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.i(TAG, "onPurchasesUpdated: User canceled the purchase")
            }

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Log.i(TAG, "onPurchasesUpdated: The user already owns this item")
            }

            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
                Log.e(
                    TAG, "onPurchasesUpdated: Developer error means that Google Play does " +
                            "not recognize the configuration. If you are just getting started, " +
                            "make sure you have configured the application correctly in the " +
                            "Google Play Console. The product ID must match and the APK you " +
                            "are using must be signed with release keys."
                )
            }
        }
    }

    private suspend fun handlePurchase(purchases: MutableList<Purchase>) {
        purchases.forEach { purchase ->
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                acknowledgePurchase(purchase)
                consumePurchase(purchase)
            }
        }
    }

    suspend fun acknowledgePurchase(purchase: Purchase): BillingResult? {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return null
        if (purchase.isAcknowledged) return null
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
        return withContext(Dispatchers.IO) {
            billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
        }
    }

    suspend fun consumePurchase(purchase: Purchase): ConsumeResult? {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return null
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build()
        return withContext(Dispatchers.IO) {
            billingClient.consumePurchase(consumeParams)
        }
    }

    companion object {
        private const val TAG = "BillingService"
        private const val MAX_RETRY_ATTEMPT = 3

        @Volatile
        private var INSTANCE: BillingService? = null

        fun getInstance(applicationContext: Context): BillingService =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BillingService(applicationContext).also { INSTANCE = it }
            }
    }
}
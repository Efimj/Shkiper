package com.jobik.shkiper.services.billing_service

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.android.billingclient.api.*
import kotlin.math.pow

class BillingService private constructor(
    private val context: Context
) : PurchasesUpdatedListener, BillingClientStateListener {
    private var billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    var basicProductWithProductDetails = mutableStateOf(emptyList<ProductDetails>())

    init {
        startConnection()
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

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            // Handle successful purchase here.
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle user cancellation here.
        } else {
            // Handle other error cases here.
        }
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

    fun queryProductDetails() {
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
                basicProductWithProductDetails.value = productDetailsList
        }
    }

    companion object {
        private const val TAG = "BillingService"
        private const val MAX_RETRY_ATTEMPT = 3

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: BillingService? = null

        fun getInstance(applicationContext: Context): BillingService =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BillingService(applicationContext).also { INSTANCE = it }
            }
    }
}
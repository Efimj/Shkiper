package com.jobik.shkiper.services.billing

import androidx.annotation.Keep

@Keep
object AppProducts {
    //Product IDs
    const val Product_1 = "product_1"
    const val Product_2 = "product_2"

    //Subscriptions IDs
    const val SimpleSubscription = "simple_subscription"
    const val Monthly = "simple-monthly"
    const val Yearly = "simple-yearly"

    val ListOfProducts = listOf(
        Product_1,
        Product_2,
    )
    val ListOfSubscriptions = listOf(
        SimpleSubscription,
    )

    val ListOfSubscriptionsPlans = listOf(
        Monthly,
        Yearly
    )
}
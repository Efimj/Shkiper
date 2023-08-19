package com.jobik.shkiper.services.billing_service

import androidx.annotation.Keep

@Keep
object AppProducts {
    //Product IDs
    const val CupOfTea = "cup_of_tea"
    const val SweetsForMyCat = "sweets_for_my_cat"
    const val GymMembership = "gym_membership"

    //Subscriptions IDs
    const val SimpleSubscription = "simple_subscription"
    const val Monthly = "simple-monthly"
    const val Yearly = "simple-yearly"

    val ListOfProducts = listOf(
        CupOfTea,
        SweetsForMyCat,
        GymMembership
    )
    val ListOfSubscriptions = listOf(
        SimpleSubscription,
    )

    val ListOfSubscriptionsPlans = listOf(
        Monthly,
        Yearly
    )
}
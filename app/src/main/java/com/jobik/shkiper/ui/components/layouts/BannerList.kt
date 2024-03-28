package com.jobik.shkiper.ui.components.layouts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jobik.shkiper.navigation.Route
import com.jobik.shkiper.ui.components.cards.DonateBanner
import com.jobik.shkiper.util.SupportTheDeveloperBannerUtil

@Composable
fun BannerList(navController: NavController) {
    val context = LocalContext.current
    val isBannerNeeded = rememberSaveable { mutableStateOf(SupportTheDeveloperBannerUtil.isBannerNeeded(context)) }

    if (isBannerNeeded.value)
        LazyRow(
            modifier = Modifier.wrapContentSize(unbounded = true)
                .width(LocalConfiguration.current.screenWidthDp.dp),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 0.dp)
        ) {
            item {
                DonateBanner({
                    SupportTheDeveloperBannerUtil.updateLastShowingDate(context)
                    navController.navigate(Route.Purchases.route)
                    isBannerNeeded.value = false
                }, {
                    SupportTheDeveloperBannerUtil.updateLastShowingDate(context)
                    isBannerNeeded.value = false
                })
            }
        }
}
package com.jobik.shkiper.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController

class NavigationHelpers {
    companion object {
        fun NavHostController.canNavigate(): Boolean {
            return this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
        }
    }
}
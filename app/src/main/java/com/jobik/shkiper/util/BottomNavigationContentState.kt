package com.jobik.shkiper.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object BottomNavigationContentState {
    private var _visible: MutableState<Boolean> = mutableStateOf(false)
    var isVisible: MutableState<Boolean>
        get() = _visible
        private set(value) {
            _visible = value
        }
}
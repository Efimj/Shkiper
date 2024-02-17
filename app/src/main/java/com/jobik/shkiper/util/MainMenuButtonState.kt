package com.jobik.shkiper.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object MainMenuButtonState {
    private var _isButtonOpened: MutableState<Boolean> = mutableStateOf(false)
    var isButtonOpened: MutableState<Boolean>
        get() = _isButtonOpened
        private set(value) {
            _isButtonOpened = value
        }
}
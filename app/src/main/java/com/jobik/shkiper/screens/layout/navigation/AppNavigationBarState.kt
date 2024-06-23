package com.jobik.shkiper.screens.layout.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

object AppNavigationBarState {
    private var _visible: MutableState<Boolean> = mutableStateOf(false)
    private var _locked: MutableState<Boolean> = mutableStateOf(false)

    val isVisible: State<Boolean> = _visible
    val isLocked: State<Boolean> = _locked

    fun show() {
        if (isLocked.value.not())
            _visible.value = true
    }

    fun hide() {
        if (isLocked.value.not())
            _visible.value = false
    }

    fun showWithUnlock() {
        unlock()
        show()
    }

    fun hideWithLock() {
        hide()
        lock()
    }

    fun lock() {
        _locked.value = true
    }

    fun unlock() {
        _locked.value = false
    }
}
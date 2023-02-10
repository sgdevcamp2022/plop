package com.plop.plopmessenger.presentation.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.plop.plopmessenger.presentation.state.ThemeConstants.SYSTEM

object UserState {
    var mode by mutableStateOf(SYSTEM)
}

enum class ThemeConstants {
    SYSTEM,
    DARK,
    LIGHT
}
package com.plop.plopmessenger.presentation.state

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.plop.plopmessenger.presentation.state.ThemeConstants.SYSTEM

object UserState {
    var mode by mutableStateOf(SYSTEM)
    var nickname by mutableStateOf("nickname")
    var profileImg by mutableStateOf<Uri?>(null)
}

enum class ThemeConstants {
    SYSTEM,
    DARK,
    LIGHT
}
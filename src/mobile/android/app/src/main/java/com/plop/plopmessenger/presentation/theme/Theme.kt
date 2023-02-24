package com.plop.plopmessenger.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.plop.plopmessenger.presentation.state.ThemeConstants.DARK
import com.plop.plopmessenger.presentation.state.ThemeConstants.LIGHT
import com.plop.plopmessenger.presentation.state.UserState

private val DarkColorPalette = darkColors(
    primary = Orange300,
    primaryVariant = Purple700,
    secondary = Gray650,
    background = Gray700,
    onBackground = Gray100,
    onPrimary = Gray700,
    onSecondary = Gray100
)

private val LightColorPalette = lightColors(
    primary = Orange200,
    primaryVariant = Purple700,
    secondary = Gray200,
    background = Gray100,
    onBackground = Gray800,
    onPrimary = Gray100,
    onSecondary = Gray800

)

@Composable
fun rememberThemeMode(): Boolean {
    return when (UserState.mode) {
        LIGHT -> false
        DARK -> true
        else -> isSystemInDarkTheme()
    }
}

@Composable
fun PlopMessengerTheme(
    darkTheme: Boolean = rememberThemeMode(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor( color = colors.background )

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
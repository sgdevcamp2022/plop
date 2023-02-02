package com.plop.plopmessenger.presentation.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(
    navigateToSignUp:() -> Unit = {},
    navigateToMain:() -> Unit = {}
) {
    Column() {
        Button(onClick = navigateToSignUp) {
            Text("현재는 Login 누르면 SignUp")
        }
        Button(onClick = navigateToMain) {
            Text("현재는 Login 누르면 Main")
        }
    }
}
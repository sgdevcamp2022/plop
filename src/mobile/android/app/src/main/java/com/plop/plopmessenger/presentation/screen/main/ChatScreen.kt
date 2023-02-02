package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ChatScreen(
    navigateToChatInfo: (Int) -> Unit
) {
    Column() {
        Text("Chat")
        Button(onClick = { navigateToChatInfo(1) }) {
            Text("go to chatInfo ")
        }
    }
}
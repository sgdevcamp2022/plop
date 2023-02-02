package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ChatsScreen(
    navigateToChat: (Int) -> Unit,
    navigateToAddChat: () -> Unit
) {
    Column() {
        Text("Chats")
        Button(onClick = { navigateToChat(1) }) {
            Text("go to chat ")
        }
        Button(onClick = navigateToAddChat) {
            Text("go to add chat ")
        }
    }
}
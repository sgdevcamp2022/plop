package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AddChatScreen(
    navigateToNewChat: () -> Unit,
) {
    Column() {
        Text("Add Chat")
        Button(onClick = navigateToNewChat) {
            Text("To New Chat")
        }
    }
}
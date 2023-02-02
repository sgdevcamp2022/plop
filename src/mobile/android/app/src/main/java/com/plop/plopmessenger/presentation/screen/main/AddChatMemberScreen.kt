package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AddChatMemberScreen(
    navigateToNewChat: () -> Unit,
    navigateToUpdateGroupChat: (Int) -> Unit
) {
    Column() {
        Text("Add Chat Member")
        Button(onClick = navigateToNewChat) {
            Text("to new chat")
        }
        Button(onClick = { navigateToUpdateGroupChat(2) }) {
            Text("to update group chat")
        }
    }

}
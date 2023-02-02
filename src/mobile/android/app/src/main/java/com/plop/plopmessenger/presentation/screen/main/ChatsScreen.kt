package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plop.plopmessenger.presentation.component.ProfileImageWithState

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

@Composable
fun ProfileStateItem(
    imageURL: String,
    isActivate: Boolean = false,
    nickname: String = ""
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileImageWithState(imageURL = imageURL, isActivate = isActivate)
        Text(
            text = nickname,
            fontSize = 14.sp,
            maxLines = 2,
            modifier = Modifier.width(56.dp),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
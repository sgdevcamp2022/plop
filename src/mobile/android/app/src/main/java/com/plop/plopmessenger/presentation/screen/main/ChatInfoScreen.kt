package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ChatInfoValue {
    val ChatInfoItemHeight = 52.dp
}

@Composable
fun ChatInfoScreen(
    navigateToAddMember: (Int) -> Unit
){
    Column() {
        Text("ChatInfo")
        Button(onClick = { navigateToAddMember(1) }) {
            Text("go to AddMember ")
        }
    }
}


@Composable
fun ChatInfoItem(
    onClick: () -> Unit = {},
    icon: ImageVector = Icons.Filled.Done,
    content: String,
    btnColor: Color = MaterialTheme.colors.onBackground,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .height(ChatInfoValue.ChatInfoItemHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = btnColor
        )

        Text(text = content, fontSize = 17.sp)
    }
}

package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object SettingValue{
    val SettingComponentHeight = 60.dp
    val SettingIconSize = 36.dp
}

@Composable
fun SettingScreen() {
    Text("Setting")
}

@Composable
fun SettingComponentToggle(
    content: String = "",
    image: Int,
    onClick:() -> Unit = {},
    modifier: Modifier = Modifier,
    isChecked: Boolean = false
) {

    val checkedState = remember { mutableStateOf(isChecked) }
    Box() {
        SettingComponent(
            content = content,
            image = image,
            onClick = onClick,
            modifier = modifier
        )

        Switch(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                onClick()
            },
            modifier = Modifier.align(Alignment.CenterEnd),
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = MaterialTheme.colors.secondary,
                uncheckedTrackColor = MaterialTheme.colors.secondary,
                checkedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.primary
            )
        )
    }
}

@Composable
fun SettingComponent(
    content: String = "",
    image: Int,
    onClick:() -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingValue.SettingComponentHeight)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            modifier = modifier.size(SettingValue.SettingIconSize)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "setting component image",
                contentScale = ContentScale.Crop)
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = content,
            fontSize = 16.sp
        )
    }
}
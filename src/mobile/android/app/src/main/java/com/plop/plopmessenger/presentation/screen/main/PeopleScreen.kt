package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun PeopleScreen(
    navigateToAddPeople: () -> Unit
) {
    Column() {
        Text("People")
        Button(onClick = navigateToAddPeople) {
            Text("to Add People")
        }
    }
}
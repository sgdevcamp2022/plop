package com.plop.plopmessenger.presentation.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PlopDialog(
    onDismiss:() -> Unit = {},
    onClick:() -> Unit = {},
    title: String = "",
    dismissContent: String = "",
    content: String = "",
    dismissColor: Color = MaterialTheme.colors.error,
    contentColor: Color = MaterialTheme.colors.onBackground
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onClick) {
                Text(text = content, color = contentColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissContent, color = dismissColor)
            }
        },
        title = {
            Text(text = title)
        }
    )
}

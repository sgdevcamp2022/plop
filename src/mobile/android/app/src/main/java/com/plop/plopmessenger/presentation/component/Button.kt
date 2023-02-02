package com.plop.plopmessenger.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object ButtonValue {
    val ButtonShape = RoundedCornerShape(12.dp)
    val LargeButtonHeight = 45.dp

    val MediumButtonHeight = 36.dp
    val MediumButtonWidth = 226.dp

    val SmallButtonHeight = 36.dp
    val SmallButtonWidth = 78.dp
}

@Composable
fun PlopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isClicked: Boolean = false,
    shape: Shape = ButtonValue.ButtonShape,
    border: BorderStroke? = null,
    content: String = "",
    contentColor: Color = MaterialTheme.colors.onPrimary,
    disabledContentColor: Color = MaterialTheme.colors.onSecondary,
    backgroundColor: Color = MaterialTheme.colors.primary,
    disabledColor: Color = MaterialTheme.colors.secondary
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = disabledColor,
            backgroundColor = if(isClicked) disabledColor else backgroundColor,
            disabledContentColor = disabledContentColor,
            contentColor = if(isClicked) disabledContentColor else contentColor
        ),
        border = border,
        shape = shape,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(
            text = content
        )
    }
}
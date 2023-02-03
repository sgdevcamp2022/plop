package com.plop.plopmessenger.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object TopBarValue {
    val HorizontalPadding = 3.dp
    val TopBarHeight = 46.dp
    val IconSize = 26.dp
    val IconBtnSize = 38.dp
    val SmallIconBtnSize = 18.dp
    val TopBarWithProfileHeight = 82.dp
    val SpacerBetweenProfileAndTitle = 14.dp
}

@Composable
fun TopBarWithProfile(
    onClick:() -> Unit,
    profileImage: String,
    content: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(TopBarValue.TopBarWithProfileHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            imageURL = profileImage
        )

        Spacer(modifier = Modifier.size(TopBarValue.SpacerBetweenProfileAndTitle))

        Text(
            text = content,
            modifier = Modifier.weight(1f),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = onClick,
            modifier = modifier
                .background(
                    MaterialTheme.colors.secondary,
                    CircleShape
                )
                .size(TopBarValue.IconBtnSize)
        ) {
            Icon(
                modifier = Modifier.size(TopBarValue.SmallIconBtnSize),
                imageVector = icon,
                contentDescription = null
            )
        }

    }
}


@Composable
fun MainTopBarWithLeftBtn(
    onLeftBtnClick: () -> Unit,
    content: String,
    leftContent: String,
    btnColor: Color = MaterialTheme.colors.primary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                horizontal = TopBarValue.HorizontalPadding
            )
            .height(TopBarValue.TopBarHeight)
    ) {
        Text(
            text = content,
            fontSize = 17.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        Text(
            text = leftContent,
            fontSize = 17.sp,
            color = btnColor,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onLeftBtnClick() }
        )
    }
}

@Composable
fun MainTopBarWithBothBtn(
    onLeftBtnClick: () -> Unit,
    onRightBtnClick: () -> Unit,
    content: String,
    leftContent: String,
    rightContent: String,
    rightVisible: Boolean,
    btnColor: Color = MaterialTheme.colors.primary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                horizontal = TopBarValue.HorizontalPadding
            )
            .height(TopBarValue.TopBarHeight)
    ) {
        Text(
            text = content,
            fontSize = 17.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        Text(
            text = leftContent,
            fontSize = 17.sp,
            color = btnColor,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onLeftBtnClick() }
        )

        if(rightVisible) {
            Text(
                text = rightContent,
                fontSize = 17.sp,
                color = btnColor,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onRightBtnClick() }
            )
        }
    }
}


@Composable
fun ChatTopBar(
    onClick: (String) -> Unit,
    upPress: () -> Unit,
    chatroomTitle: String,
    chatId: String?,
    images: List<String?>,
    btnColor: Color = MaterialTheme.colors.primary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = TopBarValue.HorizontalPadding)
            .height(TopBarValue.TopBarHeight)
            .clickable { if(!chatId.isNullOrBlank()) onClick(chatId) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = upPress,
            modifier = modifier
                .size(TopBarValue.IconSize)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = btnColor
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        ProfileImages(
            images = images
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = chatroomTitle,
            fontSize = 17.sp
        )

    }
}
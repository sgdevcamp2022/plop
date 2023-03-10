package com.plop.plopmessenger.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plop.plopmessenger.util.KeyLine

object PeopleItemValue {
    const val ItemHeight = 56
    const val ProfileSize = 56
    val CheckCircleSize = 14.dp
}

@Composable
fun PeopleWithTwoBtnItem(
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    leftBtnContent: String,
    rightBtnContent: String,
    imageURL: String,
    nickname: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        PeopleItem(imageURL = imageURL, nickname = nickname)
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            PlopButton(
                onClick = onLeftClick,
                content = leftBtnContent,
                modifier = Modifier
                    .size(
                        width = ButtonValue.SmallButtonWidth,
                        height = ButtonValue.SmallButtonHeight
                    )
            )

            PlopButton(
                onClick = onRightClick,
                content = rightBtnContent,
                modifier = Modifier
                    .size(
                        width = ButtonValue.SmallButtonWidth,
                        height = ButtonValue.SmallButtonHeight
                    ),
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary
            )
        }
    }
}


@Composable
fun PeopleWithSingleBtnItem(
    onClick: () -> Unit,
    onClickedClick: () -> Unit,
    btnContent: String,
    imageURL: String,
    nickname: String,
    modifier: Modifier = Modifier,
    clickedContent: String,
    isClicked: Boolean
) {
    Box(modifier = modifier) {
        PeopleItem(imageURL = imageURL, nickname = nickname)
        PlopButton(
            onClick = if(isClicked) onClickedClick else onClick,
            content = btnContent,
            clickedContent = clickedContent,
            isClicked = isClicked,
            modifier = Modifier
                .padding(KeyLine)
                .align(Alignment.CenterEnd)
                .size(
                    width = ButtonValue.SmallButtonWidth,
                    height = ButtonValue.SmallButtonHeight
                )
        )
    }
}


@Composable
fun PeopleWithCheckItem(
    onClick: () -> Unit,
    imageURL: String,
    nickname: String,
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onBtnColor: Color = MaterialTheme.colors.background,
    btnColor: Color = MaterialTheme.colors.primary
) {

    Box(
        modifier = modifier
            .clickable {
                onClick()
            }
    ) {
        PeopleItem(
            imageURL = imageURL,
            nickname = nickname
        )

        if(isChecked){
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .padding(horizontal = KeyLine)
                    .align(Alignment.CenterEnd)
                    .size(PeopleItemValue.CheckCircleSize),
                color = btnColor
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = onBtnColor
                )
            }
        } else {
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .padding(horizontal = KeyLine)
                    .align(Alignment.CenterEnd)
                    .size(PeopleItemValue.CheckCircleSize)
                    .border(2.dp, color = MaterialTheme.colors.secondary, shape = CircleShape),
            ){}
        }

    }
}

@Composable
fun PeopleItem(
    onClick: () -> Unit = {},
    imageURL: String = "",
    nickname: String = "nickname",
    modifier: Modifier = Modifier,
    profileSize: Int = PeopleItemValue.ItemHeight
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(profileSize.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileImage(
            imageURL = imageURL,
            profileSize = profileSize
        )

        Text(
            text = nickname,
            fontSize = 17.sp
        )
    }
}

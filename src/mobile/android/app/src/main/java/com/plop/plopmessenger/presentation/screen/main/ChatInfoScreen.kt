package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plop.plopmessenger.domain.model.Member
import com.plop.plopmessenger.presentation.component.ProfileImages
import com.plop.plopmessenger.util.KeyLine
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.plop.plopmessenger.R
import com.plop.plopmessenger.domain.model.ChatRoomType
import com.plop.plopmessenger.presentation.component.PlopDialog
import com.plop.plopmessenger.presentation.viewmodel.ChatInfoViewModel

object ChatInfoValue {
    val ChatInfoItemHeight = 52.dp
    const val ProfileImageSize = 100
    val SpacerBetweenProfileAndBtnSize = 45.dp
    val SpacerBetweenProfileAndNameSize = 12.dp
    val BackPressBtnSize = 24.dp
}

@Composable
fun ChatInfoScreen(
    navigateToAddMember: (String) -> Unit,
    navigateToChats: () -> Unit,
    upPress: () -> Unit
){
    val viewModel = hiltViewModel<ChatInfoViewModel>()
    val state by viewModel.chatInfoState.collectAsState()
    val members = state.members

    LaunchedEffect(key1 = state.shouldLeave) {
        if(state.shouldLeave) navigateToChats()
    }

    if(state.showDialog) {
        PlopDialog(
            onDismiss = viewModel::closeDialog,
            onClick = { viewModel.leaveChatRoom(); viewModel.closeDialog()},
            title = stringResource(id = R.string.chat_info_dialog),
            dismissContent = stringResource(id = R.string.chat_info_dialog_no),
            content = stringResource(id = R.string.chat_info_dialog_ok),
        )
    }

    var membersName = if(state.roomType == ChatRoomType.DM) (members.firstOrNull()?.nickname?: "")+stringResource(id = R.string.chat_info_add_group_btn)
    else stringResource(id = R.string.chat_info_add_member_btn)

    Column(
        modifier = Modifier
            .padding(KeyLine)
            .fillMaxSize()
    ) {
        IconButton(
            onClick = upPress,
            modifier = Modifier.size(ChatInfoValue.BackPressBtnSize)
        ) {
            Icon(
                modifier = Modifier.size(ChatInfoValue.BackPressBtnSize),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
            )
        }

        ProfileImages(
            images = members.map { it.profileImg },
            profileSize = ChatInfoValue.ProfileImageSize,
            modifier = Modifier
                .padding(bottom = ChatInfoValue.SpacerBetweenProfileAndNameSize)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = state.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier
                .padding(bottom = ChatInfoValue.SpacerBetweenProfileAndBtnSize)
                .align(Alignment.CenterHorizontally)
        )

        ChatInfoItem(
            onClick = { navigateToAddMember(state.chatroomId?: "") },
            content = membersName,
            icon = ImageVector.vectorResource(id = R.drawable.ic_groups)
        )

        ChatInfoItem(
            onClick = { /*TODO*/ },
            content = stringResource(id = R.string.chat_info_alarm_btn),
            icon = Icons.Filled.Notifications
        )

        ChatInfoItem(
            onClick = viewModel::showDialog,
            content = stringResource(id = R.string.chat_info_exit_btn),
            icon = Icons.Filled.ArrowBack
        )
    }
}


@Composable
fun ChatInfoItem(
    onClick: () -> Unit,
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

package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plop.plopmessenger.R
import com.plop.plopmessenger.domain.model.ChatRoom
import com.plop.plopmessenger.domain.model.ChatRoomType
import com.plop.plopmessenger.domain.model.Member
import com.plop.plopmessenger.presentation.component.*
import com.plop.plopmessenger.presentation.state.UserState
import com.plop.plopmessenger.presentation.viewmodel.ChatsViewModel
import com.plop.plopmessenger.util.Constants
import com.plop.plopmessenger.util.KeyLine
import com.plop.plopmessenger.util.dayFormatter
import com.plop.plopmessenger.util.timeFormatter
import java.lang.Math.abs
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ChatsScreenValue {
    val ProfileStateItemWidth = 56.dp
    val SpacerBetweenProfileAndMessage = 7.dp
    val SpacerBetweenMessageAndUnread = 17.dp
    val UnreadSize = 23.dp
    val MessageDateSize = 70.dp

    val SpacerBetweenChatRooms = 12.dp
}


@Composable
fun ChatsScreen(
    navigateToChat: (String) -> Unit = {},
    navigateToAddChat: () -> Unit = {}
) {
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val viewModel = hiltViewModel<ChatsViewModel>()

    var state = viewModel.chatsState.collectAsState()

    var textFieldFocusState by remember { mutableStateOf(false) }
    var focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = KeyLine)
    ) {
        TopBarWithProfile(
            onClick = { navigateToAddChat() },
            content = stringResource(id = R.string.chats_title),
            icon = Icons.Filled.Edit
        )

        LazyColumn {
            item {
                SearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    searchFocused = textFieldFocusState,
                    onSearchFocusChange = { textFieldFocusState = it },
                    onClearQuery = {
                        focusManager.clearFocus()
                        query = TextFieldValue("")
                    })
            }

            item {
                LazyRow(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(state.value.presence) {
                        ProfileStateItem(
                            imageURL = it.profileImg,
                            nickname = it.nickname
                        )
                    }
                }
            }

            if(state.value.chats.isEmpty()) {
                item {
                    EmptyScreen()
                }
            } else {
                items(state.value.chats) {
                    ChatItem(
                        onClick = navigateToChat,
                        chatRoom = it!!,
                        isActivate = null)
                    Spacer(modifier = Modifier.size(ChatsScreenValue.SpacerBetweenChatRooms))
                }
            }
        }
    }
}

@Composable
fun EmptyScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "채팅이 없습니다.",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ProfileStateItem(
    imageURL: String,
    isActivate: Boolean = true,
    nickname: String
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
            modifier = Modifier.width(ChatsScreenValue.ProfileStateItemWidth),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ChatItem(
    onClick: (String) -> Unit,
    chatRoom: ChatRoom,
    isActivate: Boolean?
) {
    var unread = if(chatRoom.unread >= Constants.maxNewMessage) {
        Constants.maxNewMessage.toString() + "+"
    } else {
        chatRoom.unread.toString()
    }



    Row(
        modifier = Modifier
            .clickable { onClick(chatRoom.chatroomId) }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(chatRoom.type == ChatRoomType.DM) {
            ProfileImageWithState(
                imageURL = chatRoom.members.firstOrNull()?.profileImg?: "",
                isActivate = isActivate?: false,
                profileSize = ProfileImageValue.ProfileWithStateSize
            )
        } else {
            ProfileImages(
                images = chatRoom.members.map { it.profileImg },
                profileSize = ProfileImageValue.ProfileWithStateSize
            )
        }

        Spacer(modifier = Modifier.size(ChatsScreenValue.SpacerBetweenProfileAndMessage))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = chatRoom.title,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row() {
                Text(
                    text = chatRoom.content,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                )

                val formatter = if(LocalDate.now().isEqual(chatRoom.createdAt.toLocalDate())) timeFormatter
                                else dayFormatter

                Text(
                    text = chatRoom.createdAt.format(formatter),
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier.width(ChatsScreenValue.MessageDateSize),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.size(ChatsScreenValue.SpacerBetweenMessageAndUnread))

        if(unread != "0") {
            Text(
                text = unread,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onPrimary,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .background(MaterialTheme.colors.primary, CircleShape)
                    .defaultMinSize(minWidth = ChatsScreenValue.UnreadSize)
                    .padding(3.dp)
            )
        }

    }
}
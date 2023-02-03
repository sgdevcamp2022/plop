package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plop.plopmessenger.R
import com.plop.plopmessenger.domain.model.ChatRoomType
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.presentation.component.ChatTextBar
import com.plop.plopmessenger.presentation.component.ChatTopBar
import com.plop.plopmessenger.presentation.component.Messages
import com.plop.plopmessenger.presentation.component.ProfileImages
import com.plop.plopmessenger.presentation.viewmodel.ChatViewModel
import com.plop.plopmessenger.util.KeyLine

enum class InputSelector {
    NONE,
    PICTURE,
    CAMERA
}

object ChatScreenValue {
    var ChatBarIconSize = 28.dp
    var ChatBarIconBtnSize = 34.dp
    const val ProfileImageSize = 104
    var ProfileImageBottomPadding = 10.dp
    var IconBtnSize = 38.dp
    var SmallIconBtnSize = 18.dp
    var SpaceBetweenButtons1 = 30.dp
    var SpaceBetweenButtons2 = 22.dp
    val TopBarHeight = 46.dp
    val ChatBarPadding = 12.dp
    val UserInputHeightSize = 52.dp
}

@Composable
fun ChatScreen(
    upPress: () -> Unit,
    navigateToChatInfo: (String) -> Unit,
    navigateToAddMember: (String) -> Unit,
    peopleList: List<People>? = emptyList(),
    viewModel: ChatViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        if(!peopleList.isNullOrEmpty()) {
            viewModel.getMember(peopleList)
        }
    }
    val state by viewModel.chatState.collectAsState()
    val messages = state.messages
    var query = state.query
    var textFieldFocusState = state.textFieldFocusState
    var currentInputSelector = state.currentInputSelector
    var focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = KeyLine)
    ) {
        Column(
            modifier = Modifier.clickable {
                if (textFieldFocusState) {
                    textFieldFocusState = false
                    focusManager.clearFocus()
                }
            }
        ) {
            Messages(
                messages = messages,
                onAuthorClick = {},
                onImageClick = {},
                onVideoClick = {},
                member = state.members,
                isGroupChat = false,
                userId = state.userId,
                modifier = Modifier
                    .padding(
                        top = ChatScreenValue.TopBarHeight,
                        bottom = ChatScreenValue.UserInputHeightSize
                    )
                    .fillMaxWidth()
            ) {
                if (state.chatRoomType == ChatRoomType.GROUP && !state.chatroomId.isNullOrBlank()) {
                    GroupChatButtons(
                        onAddBtnClick = { navigateToAddMember(state.chatroomId!!) },
                        onEditBtnClick = { /*TODO*/ },
                        onMemberBtnClick = { }
                    )
                }
                ChatRoomInfo(
                    images = state.members.values.map { it.profileImg },
                    title = state.title
                )
            }
        }


        ChatTopBar(
            onClick = navigateToChatInfo,
            upPress = upPress,
            chatroomTitle = state.title,
            images = state.members.values.map { it.profileImg },
            chatId = state.chatroomId,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .align(Alignment.TopCenter)
        )

        UserInput(
            onMessageSent = { /*TODO*/ },
            resetScroll = { /*TODO*/ },
            query = query,
            onQueryChange = viewModel::setQuery,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .align(Alignment.BottomStart)
                .navigationBarsPadding()
                .imePadding(),
            onTextFieldFocused = { focused ->
                if (focused) {
                    currentInputSelector = InputSelector.NONE
                }
                viewModel.setFocusState(focused)
            },
            focused = textFieldFocusState,
            currentInputSelector = currentInputSelector,
            onChangeCurrentInput = {
                currentInputSelector = it

                if (textFieldFocusState) {
                    viewModel.setFocusState(false)
                    focusManager.clearFocus()
                }
            },
            dismissKeyboard = { viewModel.setInputSelector(InputSelector.NONE) }
        )
    }
}

@Composable
fun UserInput(
    onMessageSent:() -> Unit,
    resetScroll: () -> Unit,
    query: TextFieldValue,
    focused: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    onQueryChange: (TextFieldValue) -> Unit,
    currentInputSelector: InputSelector,
    onChangeCurrentInput: (InputSelector) -> Unit,
    dismissKeyboard: () -> Unit,
    modifier: Modifier
) {

    Row(
        modifier = modifier
            .height(ChatScreenValue.UserInputHeightSize)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onChangeCurrentInput(InputSelector.CAMERA) },
            modifier = Modifier.size(ChatScreenValue.ChatBarIconBtnSize)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_camera_alt_24),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(ChatScreenValue.ChatBarIconSize)
            )
        }

        IconButton(
            onClick = { onChangeCurrentInput(InputSelector.PICTURE) },
            modifier = Modifier.size(ChatScreenValue.ChatBarIconBtnSize)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_photo_24),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(ChatScreenValue.ChatBarIconSize)
            )
        }

        ChatTextBar(
            query = query,
            onQueryChange = onQueryChange,
            focused = focused,
            onTextFieldFocused = onTextFieldFocused,
            keyboardShown = currentInputSelector == InputSelector.NONE && focused,
            modifier = Modifier
                .weight(1f)
                .padding(start = ChatScreenValue.ChatBarPadding)
        )

        if(query != TextFieldValue("")) {
            IconButton(
                onClick = {
                    onMessageSent()
                    resetScroll()
                    dismissKeyboard()
                },
                modifier = Modifier
                    .padding(start = ChatScreenValue.ChatBarPadding)
                    .size(ChatScreenValue.ChatBarIconBtnSize)

            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(ChatScreenValue.ChatBarIconSize)
                )
            }
        }
    }
}

@Composable
private fun ChatRoomInfo(
    images: List<String>,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ProfileImages(
            images = images,
            profileSize = ChatScreenValue.ProfileImageSize
        )

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier
                .padding(bottom = ChatScreenValue.ProfileImageBottomPadding)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun GroupChatButtons(
    onAddBtnClick:() -> Unit,
    onEditBtnClick:() -> Unit,
    onMemberBtnClick:() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    onClick = onAddBtnClick,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.secondary,
                            CircleShape
                        )
                        .size(ChatScreenValue.IconBtnSize)
                ) {
                    Icon(
                        modifier = Modifier.size(ChatScreenValue.SmallIconBtnSize),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_person_add),
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(id = R.string.chat_add_btn),
                    modifier = Modifier.padding(2.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.size(ChatScreenValue.SpaceBetweenButtons1))

            Column( horizontalAlignment = Alignment.CenterHorizontally ) {
                IconButton(
                    onClick = onEditBtnClick,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.secondary,
                            CircleShape
                        )
                        .size(ChatScreenValue.IconBtnSize)
                ) {
                    Icon(
                        modifier = Modifier.size(ChatScreenValue.SmallIconBtnSize),
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(id = R.string.chat_edit_btn),
                    modifier = Modifier.padding(2.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.size(ChatScreenValue.SpaceBetweenButtons2))

            Column( horizontalAlignment = Alignment.CenterHorizontally ) {
                IconButton(
                    onClick = onMemberBtnClick,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.secondary,
                            CircleShape
                        )
                        .size(ChatScreenValue.IconBtnSize)
                ) {
                    Icon(
                        modifier = Modifier.size(ChatScreenValue.SmallIconBtnSize),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_groups),
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(id = R.string.chat_member_btn),
                    modifier = Modifier.padding(vertical = 2.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
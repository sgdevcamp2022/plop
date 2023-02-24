package com.plop.plopmessenger.presentation.screen.main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.plop.plopmessenger.R
import com.plop.plopmessenger.domain.model.ChatRoomType
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.presentation.component.*
import com.plop.plopmessenger.presentation.model.MediaStoreImage
import com.plop.plopmessenger.presentation.viewmodel.ChatViewModel
import com.plop.plopmessenger.util.KeyLine
import java.io.ByteArrayOutputStream

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
    var context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()) {
        viewModel.setImage(getImageUriFromBitmap(context, it))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = KeyLine)
                .clickable {
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
                state = state,
                getNextMessage = viewModel::getMessageList,
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
                .padding(horizontal = KeyLine)
                .background(MaterialTheme.colors.background)
                .align(Alignment.TopCenter)
        )

        UserInput(
            onMessageSent = viewModel::sendMessage,
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
                    viewModel.setCurrentInputSelector(InputSelector.NONE)
                }
                viewModel.setFocusState(focused)
            },
            focused = textFieldFocusState,
            currentInputSelector = currentInputSelector,
            onChangeCurrentInput = {
                viewModel.setCurrentInputSelector(it)

                if (textFieldFocusState) {
                    viewModel.setFocusState(false)
                    focusManager.clearFocus()
                }
            },
            onCameraClick = { launcher.launch() },
            dismissKeyboard = { viewModel.setInputSelector(InputSelector.NONE) },
            images = state.images,
            onImageClick = viewModel::setImage,
            selectedImage = state.selectedImage,
            sendImage = viewModel::sendImage
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
    onCameraClick: () -> Unit,
    dismissKeyboard: () -> Unit,
    images: List<MediaStoreImage>,
    onImageClick: (Uri?) -> Unit,
    selectedImage: Uri?,
    sendImage: () -> Unit,
    modifier: Modifier
) {

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(horizontal = KeyLine)
                .defaultMinSize(minHeight = ChatScreenValue.UserInputHeightSize)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onChangeCurrentInput(InputSelector.CAMERA)
                    onCameraClick()
                },
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
                onClick = {
                    onChangeCurrentInput(InputSelector.PICTURE)
                          },
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
        if(currentInputSelector == InputSelector.PICTURE) {
            ImageBox(
                images = images,
                onImageClick = onImageClick,
                selectedImage = selectedImage,
                sendImage = sendImage
            )
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

@Composable
fun ImageBox(
    images: List<MediaStoreImage>,
    onImageClick: (Uri?) -> Unit,
    selectedImage: Uri?,
    sendImage: () -> Unit
) {
    Box() {
        LazyVerticalGrid(
            modifier = Modifier.defaultMinSize(minHeight = 300.dp),
            columns = GridCells.Adaptive(minSize = 100.dp)
        ) {
            items(images) { image ->

                val selected = image.contentUri == selectedImage
                val alphaValue = if(selected) 0.5f else 1f

                Box(modifier = Modifier.fillMaxWidth()) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(image.contentUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .alpha(alphaValue)
                            .clickable { onImageClick(image.contentUri) }
                            .fillMaxWidth()
                            .size(100.dp),
                        placeholder = painterResource(id = R.drawable.blank_profile)
                    )

                    if(selected) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(
                                    MaterialTheme.colors.primary,
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }

        if(selectedImage != null) {
            PlopButton(
                onClick = sendImage,
                content = stringResource(id = R.string.chat_image_send_btn),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(KeyLine)
                    .height(height = ButtonValue.LargeButtonHeight)
                    .fillMaxWidth()
            )
        }
    }
}

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap?): Uri {
    val bytes = ByteArrayOutputStream()
    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path.toString())
}
package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.component.ChatTextBar

enum class InputSelector {
    NONE,
    PICTURE,
    CAMERA
}

object UserInputValue {
    var IconSize = 24.dp
}

@Composable
fun ChatScreen(
    navigateToChatInfo: (Int) -> Unit
) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    var textFieldFocusState by remember { mutableStateOf(false) }
    var currentInputSelector by rememberSaveable{ mutableStateOf( InputSelector.NONE )}
    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }
    var focusManager = LocalFocusManager.current

    Column(

    ) {
        Text("Chat")
        Button(onClick = { navigateToChatInfo(1) }) {
            Text("go to chatInfo ")
        }


        Column(
            modifier = Modifier.clickable {
                if(textFieldFocusState) {
                    textFieldFocusState = false
                    focusManager.clearFocus()
                }
            }
        ) {
            Surface(
                modifier = Modifier.size(500.dp, 500.dp)
            ) {

            }
        }

        UserInput(
            onMessageSent = { /*TODO*/ },
            resetScroll = { /*TODO*/ },
            query = textState,
            onQueryChange = { textState = it },
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding(),
            onTextFieldFocused = { focused ->
                if (focused) {
                    currentInputSelector = InputSelector.NONE
                }
                textFieldFocusState = focused
            },
            focused = textFieldFocusState,
            currentInputSelector = currentInputSelector,
            onChangeCurrentInput = {
                currentInputSelector = it

                if(textFieldFocusState) {
                    textFieldFocusState = false
                    focusManager.clearFocus()
                }
            },
            dismissKeyboard = dismissKeyboard
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
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            onClick = { onChangeCurrentInput(InputSelector.CAMERA) },
            modifier = modifier
                .size(UserInputValue.IconSize)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_camera_alt_24),
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }

        IconButton(
            onClick = { onChangeCurrentInput(InputSelector.PICTURE) },
            modifier = modifier
                .size(UserInputValue.IconSize)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_photo_24),
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }

        ChatTextBar(
            query = query,
            onQueryChange = onQueryChange,
            focused = focused,
            onTextFieldFocused = onTextFieldFocused,
            keyboardShown = currentInputSelector == InputSelector.NONE && focused,
            modifier = Modifier.weight(1f)
        )

        if(query != TextFieldValue("")) {
            IconButton(
                onClick = {
                    onMessageSent()
                    resetScroll()
                    dismissKeyboard()
                },
                modifier = modifier
                    .size(UserInputValue.IconSize)
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}
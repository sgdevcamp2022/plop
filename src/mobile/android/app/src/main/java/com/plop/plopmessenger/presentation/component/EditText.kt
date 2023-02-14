package com.plop.plopmessenger.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plop.plopmessenger.presentation.theme.Gray400
import com.plop.plopmessenger.presentation.theme.Gray600

object SearchBarValue {
    val IconSize = 18.dp
    val BoxCornerSize = 9.dp
    val BoxHeight = 36.dp
    val IconSpaceSize = 38.dp
    val IconPaddingSize = 10.dp
}

object ChatTextBarValue {
    val BoxDefaultHeightValue = 28.dp
    val BoxCornerSize = 18.dp
    val EditTextHorizontalPaddingSize = 16.dp
    val EditTextVerticalPaddingSize = 9.dp
}

object LoginEditTextValue {
    val EditTextHeight = 42.dp
}


@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    searchFocused: Boolean,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = ""
) {
    val focusManager = LocalFocusManager.current

    Box (
        modifier = modifier
            .fillMaxWidth()
            .height(SearchBarValue.BoxHeight)
            .background(
                MaterialTheme.colors.secondary,
                RoundedCornerShape(SearchBarValue.BoxCornerSize)
            )
    ){
        if(!searchFocused) {
            Text(
                text = placeholder,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = SearchBarValue.IconSpaceSize)
            )
        }

        Icon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = SearchBarValue.IconPaddingSize)
                .size(SearchBarValue.IconSize),
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = MaterialTheme.colors.onBackground
        )

        if(searchFocused){
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = SearchBarValue.IconPaddingSize)
                    .background(Gray400, CircleShape)
                    .size(SearchBarValue.IconSize)
                    .clickable { onClearQuery() },
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = MaterialTheme.colors.background
            )
        }

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSecondary
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = SearchBarValue.IconSpaceSize)
                .fillMaxWidth()
                .onFocusChanged { onSearchFocusChange(it.isFocused) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() } ),
        )

    }
}

@Composable
fun ChatTextBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    focused: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    placeholder: String = "",
    keyboardShown: Boolean,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier
            .defaultMinSize(ChatTextBarValue.BoxDefaultHeightValue)
            .background(
                MaterialTheme.colors.secondary,
                RoundedCornerShape(ChatTextBarValue.BoxCornerSize)
            )
    ){
        if(!focused) {
            Text(
                text = placeholder,
                modifier = Modifier
                    .align(Alignment.CenterStart)
            )
        }

        var lastFocusState by remember { mutableStateOf(false) }
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSecondary
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .padding(
                    horizontal = ChatTextBarValue.EditTextHorizontalPaddingSize,
                    vertical = ChatTextBarValue.EditTextVerticalPaddingSize
                )
                .onFocusChanged {
                    if (lastFocusState != it.isFocused) {
                        onTextFieldFocused(it.isFocused)
                    }

                    lastFocusState = it.isFocused
                }
        )
    }
}

@Composable
fun LoginEditText(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onDone: (KeyboardActionScope.() -> Unit)?,
    searchFocused: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier,
    placeholder: String = ""
) {

    Box(modifier = modifier) {
        if(!searchFocused && query == TextFieldValue("")) {
            Text(
                text = placeholder,
                modifier = Modifier
                    .align(Alignment.CenterStart),
                color = Gray600
            )
        }

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSecondary
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .onFocusChanged { onSearchFocusChange(it.isFocused) },
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = onDone),
        )


    }
}

@Composable
fun NicknameEditText(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onDone: (KeyboardActionScope.() -> Unit)?,
    modifier: Modifier = Modifier
) {

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary
        ),
        modifier = modifier
            .onFocusChanged { onSearchFocusChange(it.isFocused) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = onDone),
    )
}
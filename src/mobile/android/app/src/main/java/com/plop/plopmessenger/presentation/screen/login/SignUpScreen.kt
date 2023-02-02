package com.plop.plopmessenger.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.component.ButtonValue
import com.plop.plopmessenger.presentation.component.LoginEditText
import com.plop.plopmessenger.presentation.component.PlopButton
import com.plop.plopmessenger.util.KeyLine

object SignUpScreenValue {
    val AppLogoSize = 128.dp
    val SpacerBetweenLogoAndEtSize = 222.dp
    val SpacerBetweenEtAndBtnSize = 34.dp
    val DividerVerticalPadding = 12.dp
    val BottomPadding = 36.dp
}

@Composable
fun SignUpScreen(onBackClick: () -> Unit) {
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var query2 by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    var textFieldFocusState by remember { mutableStateOf(false) }
    var textFieldFocusState2 by remember { mutableStateOf(false) }
    var focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(
                horizontal = KeyLine,
                vertical = SignUpScreenValue.BottomPadding
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "app_logo",
            modifier = Modifier.size(SignUpScreenValue.AppLogoSize)
        )

        Spacer(modifier = Modifier.size(SignUpScreenValue.SpacerBetweenLogoAndEtSize))

        LoginEditText(
            query = query,
            onQueryChange = { query = it},
            onSearchFocusChange = { textFieldFocusState = it },
            searchFocused = textFieldFocusState,
            placeholder = stringResource(id = R.string.initial_email_et),
            onDone = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Divider(
            modifier = Modifier
                .padding(vertical = SignUpScreenValue.DividerVerticalPadding)
                .fillMaxWidth()
        )

        LoginEditText(
            query = query2,
            onQueryChange = { query2 = it},
            onSearchFocusChange = { textFieldFocusState2 = it },
            searchFocused = textFieldFocusState2,
            placeholder = stringResource(id = R.string.initial_password_et),
            onDone = {
                focusManager.clearFocus()
            }
        )

        Spacer(modifier = Modifier.size(SignUpScreenValue.SpacerBetweenEtAndBtnSize))

        PlopButton(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonValue.LargeButtonHeight),
            enabled = (query != TextFieldValue("") && query2 != TextFieldValue("")),
            content = stringResource(id = R.string.signup_create_btn),
            contentColor = MaterialTheme.colors.onPrimary,
            disabledContentColor = MaterialTheme.colors.onSecondary,
            backgroundColor = MaterialTheme.colors.primary,
            disabledColor = MaterialTheme.colors.secondary
        )
    }
}

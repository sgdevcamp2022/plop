package com.plop.plopmessenger.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.component.ButtonValue
import com.plop.plopmessenger.presentation.component.LoginEditText
import com.plop.plopmessenger.presentation.component.PlopButton
import com.plop.plopmessenger.util.KeyLine

object LoginScreenValue {
    val AppLogoSize = 128.dp
    val SpacerBetweenLogoAndEtSize = 128.dp
    val SpacerBetweenEtAndBtnSize = 34.dp
    val SpacerBetweenBtnSize = 12.dp
    val SpacerBetweenBtnAndTextSize = 19.dp
    val DividerVerticalPadding = 12.dp
    val BottomPadding = 36.dp
}

@Composable
fun LoginScreen(
    navigateToSignUp:() -> Unit = {},
    navigateToMain:() -> Unit = {}
) {
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
                vertical = LoginScreenValue.BottomPadding
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "app_logo",
            modifier = Modifier.size(LoginScreenValue.AppLogoSize)
        )

        Spacer(modifier = Modifier.size(LoginScreenValue.SpacerBetweenLogoAndEtSize))

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
                .padding(vertical = LoginScreenValue.DividerVerticalPadding)
                .fillMaxWidth()
        )

        LoginEditText(
            query = query2,
            onQueryChange = { query2 = it},
            onSearchFocusChange = { textFieldFocusState2 = it },
            searchFocused = textFieldFocusState2,
            placeholder = stringResource(id = R.string.initial_email_et),
            onDone = {
                focusManager.clearFocus()
            }
        )

        Spacer(modifier = Modifier.size(LoginScreenValue.SpacerBetweenEtAndBtnSize))

        PlopButton(
            onClick = navigateToMain,
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonValue.LargeButtonHeight),
            enabled = (query != TextFieldValue("") && query2 != TextFieldValue("")),
            content = stringResource(id = R.string.login_login_btn),
            contentColor = MaterialTheme.colors.onPrimary,
            disabledContentColor = MaterialTheme.colors.onSecondary,
            backgroundColor = MaterialTheme.colors.primary,
            disabledColor = MaterialTheme.colors.secondary
        )

        Spacer(modifier = Modifier.size(LoginScreenValue.SpacerBetweenBtnSize))

        PlopButton(
            onClick = navigateToSignUp,
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonValue.LargeButtonHeight),
            content = stringResource(id = R.string.login_signUp_btn),
            contentColor = MaterialTheme.colors.onSecondary,
            disabledContentColor = MaterialTheme.colors.onSecondary,
            backgroundColor = MaterialTheme.colors.secondary,
            disabledColor = MaterialTheme.colors.secondary
        )

        Spacer(modifier = Modifier.size(LoginScreenValue.SpacerBetweenBtnAndTextSize))

        Text(
            text = stringResource(id = R.string.login_find),
            modifier = Modifier.clickable { navigateToSignUp() },
            color = MaterialTheme.colors.primary,
            fontSize = 14.sp
        )
    }
}
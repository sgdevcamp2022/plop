package com.plop.plopmessenger.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.component.ButtonValue
import com.plop.plopmessenger.presentation.component.LoginEditText
import com.plop.plopmessenger.presentation.component.PlopButton
import com.plop.plopmessenger.presentation.component.PlopDialog
import com.plop.plopmessenger.presentation.viewmodel.FindPasswordViewModel
import com.plop.plopmessenger.util.KeyLine

object FindPasswordScreenValue {
    val AppLogoSize = 128.dp
    val SpacerBetweenLogoAndEtSize = 222.dp
    val SpacerBetweenEtAndBtnSize = 34.dp
    val DividerVerticalPadding = 12.dp
    val BottomPadding = 36.dp
}

@Composable
fun FindPasswordScreen(onBackClick: () -> Unit) {
    val viewModel = hiltViewModel<FindPasswordViewModel>()
    val state by viewModel.findPasswordState.collectAsState()

    var focusManager = LocalFocusManager.current

    if(state.showFindPasswordDialog) {
        PlopDialog(
            onDismiss = viewModel::closeDialog,
            onClick = {
                viewModel.closeDialog()
                if(state.findPasswordState) onBackClick()
                      },
            title = if(state.findPasswordState) stringResource(id = R.string.find_password_dialog_success)
                    else state.message,
            dismissContent = "",
            content = stringResource(id = R.string.find_password_dialog_ok),
        )
    }

    Column(
        modifier = Modifier
            .padding(
                horizontal = KeyLine,
                vertical = FindPasswordScreenValue.BottomPadding
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "app_logo",
            modifier = Modifier.size(FindPasswordScreenValue.AppLogoSize)
        )

        Spacer(modifier = Modifier.size(FindPasswordScreenValue.SpacerBetweenLogoAndEtSize))

        LoginEditText(
            query = state.emailQuery,
            onQueryChange = viewModel::setEmailQuery,
            onSearchFocusChange = viewModel::setEmailState,
            searchFocused = state.emailTextFieldFocusState,
            placeholder = stringResource(id = R.string.find_password_email_et),
            onDone = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Divider(
            modifier = Modifier
                .padding(vertical = FindPasswordScreenValue.DividerVerticalPadding)
                .fillMaxWidth()
        )

        LoginEditText(
            query = state.pwdQuery,
            onQueryChange = viewModel::setPwdQuery,
            onSearchFocusChange = viewModel::setPwdState,
            searchFocused = state.pwdTextFieldFocusState,
            visualTransformation = PasswordVisualTransformation(),
            placeholder = stringResource(id = R.string.find_password_pwd_et),
            onDone = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Divider(
            modifier = Modifier
                .padding(vertical = FindPasswordScreenValue.DividerVerticalPadding)
                .fillMaxWidth()
        )

        LoginEditText(
            query = state.userIdQuery,
            onQueryChange = viewModel::setUserIdQuery,
            onSearchFocusChange = viewModel::setUserIdState,
            searchFocused = state.userIdTextFieldFocusState,
            placeholder = stringResource(id = R.string.find_password_id_et),
            onDone = {
                focusManager.clearFocus()
            }
        )

        Spacer(modifier = Modifier.size(FindPasswordScreenValue.SpacerBetweenEtAndBtnSize))

        PlopButton(
            onClick = viewModel::findPassword,
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonValue.LargeButtonHeight),
            enabled = (
                    state.emailQuery != TextFieldValue("") &&
                            state.pwdQuery != TextFieldValue("") &&
                            state.userIdQuery != TextFieldValue("")
                    ),
            content = stringResource(id = R.string.find_password_create_btn),
            contentColor = MaterialTheme.colors.onPrimary,
            disabledContentColor = MaterialTheme.colors.onSecondary,
            backgroundColor = MaterialTheme.colors.primary,
            disabledColor = MaterialTheme.colors.secondary
        )
    }
}

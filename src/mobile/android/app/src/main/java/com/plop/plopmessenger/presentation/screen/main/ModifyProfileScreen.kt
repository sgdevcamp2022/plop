package com.plop.plopmessenger.presentation.screen.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.component.MainTopBarWithLeftBtn
import com.plop.plopmessenger.presentation.component.NicknameEditText
import com.plop.plopmessenger.presentation.component.PlopDialog
import com.plop.plopmessenger.presentation.component.ProfileImage
import com.plop.plopmessenger.presentation.viewmodel.ModifyProfileScreenViewModel
import com.plop.plopmessenger.util.KeyLine

@Composable
fun ModifyProfileScreen(
    upPress: () -> Unit,
    viewModel: ModifyProfileScreenViewModel = hiltViewModel()
) {

    var state = viewModel.modifyProfileState.collectAsState()
    var focusManager = LocalFocusManager.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel.setImage(it) } }

    LaunchedEffect(key1 = state.value.saved) {
        if(state.value.saved) upPress()
    }

    if(state.value.showDialog) {
        PlopDialog(
            onDismiss = upPress,
            onClick = viewModel::saveUser,
            title = stringResource(id = R.string.setting_profile_dialog),
            dismissContent = stringResource(id = R.string.setting_profile_dialog_dismiss),
            content = stringResource(id = R.string.setting_profile_dialog_ok),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = KeyLine)
    ) {
        MainTopBarWithLeftBtn(
            onLeftBtnClick = viewModel::showDialog,
            content = stringResource(id = R.string.setting_profile_top_bar),
            leftContent = stringResource(id = R.string.setting_profile_save_btn)
        )

        ProfileImage(
            onClick = { launcher.launch("image/*") },
            uri = state.value.profileImg,
            modifier = Modifier
                .padding(top = SettingValue.ProfileImageTopPadding)
                .align(Alignment.CenterHorizontally)
        )

        NicknameEditText(
            query = state.value.nickname,
            onQueryChange = viewModel::setNicknameQuery,
            onSearchFocusChange = viewModel::setNicknameState,
            onDone = { focusManager.clearFocus() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.component.ProfileImage
import com.plop.plopmessenger.presentation.component.SubTitle
import com.plop.plopmessenger.presentation.theme.Gray400
import com.plop.plopmessenger.presentation.viewmodel.SettingViewModel
import com.plop.plopmessenger.util.KeyLine

object SettingValue{
    val SettingComponentHeight = 60.dp
    val SettingIconSize = 36.dp
    const val ProfileImageSize = 100
    val ProfileImageTopPadding = 118.dp
    val ProfileImageBottomPadding = 55.dp
}

@Composable
fun SettingScreen(
    navigateToModifyProfile: () -> Unit
) {

    val viewModel = hiltViewModel<SettingViewModel>()
    val state by viewModel.settingState.collectAsState()

    Column(
        modifier = Modifier.padding(horizontal = KeyLine)
    ) {
        ProfileImage(
            imageURL = state.image,
            profileSize = SettingValue.ProfileImageSize,
            modifier = Modifier
                .padding(top = SettingValue.ProfileImageTopPadding)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = state.nickname,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier
                .padding(bottom = SettingValue.ProfileImageBottomPadding)
                .align(Alignment.CenterHorizontally)
        )

        SubTitle(
            content = stringResource(id = R.string.setting_system_subtitle))

        SettingComponentToggle(
            content = stringResource(id = R.string.setting_dark_mode_btn),
            image = ImageVector.vectorResource(id = R.drawable.ic_set_mode),
            onClick = { viewModel.setThemeMode(!state.themeMode) },
            isChecked = state.themeMode
        )

        SettingComponentToggle(
            content = stringResource(id = R.string.setting_alarm_btn),
            image = ImageVector.vectorResource(id = R.drawable.ic_set_bell),
            onClick = { viewModel.setAlarm(!state.alarmMode) },
            isChecked = state.alarmMode
        )

        SettingComponentToggle(
            content = stringResource(id = R.string.setting_active_btn),
            image = ImageVector.vectorResource(id = R.drawable.ic_set_active),
            onClick = { viewModel.setActiveMode(!state.activeMode) },
            isChecked = state.activeMode
        )

        SubTitle(content = stringResource(id = R.string.setting_account_subtitle))

        SettingComponent(
            content = stringResource(id = R.string.setting_profile_btn),
            image = ImageVector.vectorResource(id = R.drawable.ic_set_active),
            onClick = { navigateToModifyProfile() }
        )

        SettingComponent(
            content = stringResource(id = R.string.setting_logout_btn),
            image = ImageVector.vectorResource(id = R.drawable.ic_set_out),
            onClick = { /*TODO*/ }
        )

        SettingComponent(
            content = stringResource(id = R.string.setting_withdraw_btn),
            image = ImageVector.vectorResource(id = R.drawable.ic_set_out),
            onClick = { /*TODO*/ }
        )
    }
}

@Composable
fun SettingComponentToggle(
    content: String,
    image: ImageVector,
    onClick:() -> Unit,
    modifier: Modifier = Modifier,
    isChecked: Boolean
) {

    val checkedState = remember { mutableStateOf(isChecked) }
    Box() {
        SettingComponent(
            content = content,
            image = image,
            onClick = onClick,
            modifier = modifier,
            clickable = false
        )

        Switch(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                onClick()
            },
            modifier = Modifier.align(Alignment.CenterEnd),
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Gray400,
                uncheckedTrackColor = Gray400,
                checkedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.primary
            )
        )
    }
}

@Composable
fun SettingComponent(
    content: String ,
    image: ImageVector,
    onClick:() -> Unit,
    clickable: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingValue.SettingComponentHeight)
            .clickable(
                enabled = clickable
            ) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            modifier = modifier.size(SettingValue.SettingIconSize)
        ) {
            Image(
                imageVector = image,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = content,
            fontSize = 16.sp
        )
    }
}

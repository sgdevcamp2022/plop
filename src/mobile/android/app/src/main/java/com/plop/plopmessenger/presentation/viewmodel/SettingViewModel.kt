package com.plop.plopmessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.usecase.user.UserUseCase
import com.plop.plopmessenger.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    var settingState = MutableStateFlow(SettingState())
        private set

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            userUseCase.getUserInfoUseCase().collect() { result ->
                settingState.update {
                    it.copy(
                        nickname = result.nickname,
                        image = result.profileImg,
                        themeMode = result.themeMode,
                        alarmMode = result.alarm,
                        activeMode = result.active
                    )
                }
            }
        }
    }

    fun setThemeMode(mode: Boolean) {
        viewModelScope.launch {
            userUseCase.setThemeUseCase(mode)
        }
    }

    fun setAlarm(mode: Boolean) {
        viewModelScope.launch {
            userUseCase.setAlarmUseCase(mode)
        }
    }

    fun setActiveMode(mode: Boolean) {
        viewModelScope.launch {
            userUseCase.setActiveUseCase(mode)
        }
    }

    fun closeDialog() {
        settingState.update {
            it.copy(
                showLogoutDialog = false,
                showWithdrawalDialog = false
            )
        }
    }

    fun showLogoutDialog() {
        settingState.update {
            it.copy(
                showLogoutDialog = true
            )
        }
    }

    fun showWithdrawalDialog() {
        settingState.update {
            it.copy(
                showWithdrawalDialog = true
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            userUseCase.logoutUseCase().collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        settingState.update {
                            it.copy(
                                shouldLoginState = false
                            )
                        }
                        Log.d("로그아웃", "성공..성공입니다..!")
                    }
                    is Resource.Error -> {
                        settingState.update {
                            it.copy(
                                error = result.message?: ""
                            )
                        }
                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            userUseCase.withdrawalUseCase().collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        settingState.update {
                            it.copy(
                                shouldLoginState = false
                            )
                        }
                        Log.d("탈퇴", "성공..성공입니다..!")
                    }
                    is Resource.Error -> {
                        settingState.update {
                            it.copy(
                                error = result.message?: ""
                            )
                        }
                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }
    }

}

data class SettingState(
    val nickname: String = "",
    val image: String = "",
    val themeMode: Boolean = true,
    val alarmMode: Boolean = true,
    val activeMode: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showWithdrawalDialog: Boolean = false,
    val error: String = "",
    val shouldLoginState: Boolean = true
)

package com.plop.plopmessenger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.usecase.user.UserUseCase
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
            userRepository.setThemeMode(mode)
        }
    }

    fun setAlarm(mode: Boolean) {
        viewModelScope.launch {
            userRepository.setAlarmMode(mode)
        }
    }

    fun setActiveMode(mode: Boolean) {
        viewModelScope.launch {
            userRepository.setActiveMode(mode)
        }
    }

}

data class SettingState(
    val nickname: String = "",
    val image: String = "",
    val themeMode: Boolean = true,
    val alarmMode: Boolean = true,
    val activeMode: Boolean = false,
)

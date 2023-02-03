package com.plop.plopmessenger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    var settingState = MutableStateFlow(SettingState())
        private set

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            userRepository.getUser().collect() { result ->
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
            settingState.update {
                it.copy(
                    themeMode = mode
                )
            }
        }
    }

    fun setAlarm(mode: Boolean) {
        viewModelScope.launch {
            userRepository.setAlarmMode(mode)
            settingState.update {
                it.copy(
                    alarmMode = mode
                )
            }
        }
    }

    fun setActiveMode(mode: Boolean) {
        viewModelScope.launch {
            userRepository.setActiveMode(mode)
            userRepository.getActiveModel().collect(){ result ->
                settingState.update {
                    it.copy(
                        activeMode = result
                    )
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
)

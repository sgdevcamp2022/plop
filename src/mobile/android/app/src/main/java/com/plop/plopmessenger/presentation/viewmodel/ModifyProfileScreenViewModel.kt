package com.plop.plopmessenger.presentation.viewmodel

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.usecase.user.UserUseCase
import com.plop.plopmessenger.presentation.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyProfileScreenViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    var modifyProfileState = MutableStateFlow(ModifyProfileState())
        private set

    fun setNicknameQuery(query: TextFieldValue) {
        modifyProfileState.update {
            it.copy(nickname = query)
        }
    }

    fun setNicknameState(isFocus: Boolean) {
        modifyProfileState.update {
            it.copy(nicknameTextFieldFocusState = isFocus)
        }
    }

    fun saveUser() {
        viewModelScope.launch {
            userUseCase.modifyUserUseCase(
                nickname = modifyProfileState.value.nickname.text,
                image = modifyProfileState.value.profileImg
            )
        }
    }

    fun setImage(uri: Uri) {
        modifyProfileState.update {
            it.copy(profileImg = uri)
        }
    }

    fun showDialog() {
        modifyProfileState.update {
            it.copy(showDialog = true)
        }
    }
}

data class ModifyProfileState(
    val nickname: TextFieldValue = TextFieldValue(UserState.nickname),
    val profileImg: Uri? = UserState.profileImg,
    val nicknameTextFieldFocusState: Boolean = false,
    val showDialog: Boolean = false
)
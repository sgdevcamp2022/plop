package com.plop.plopmessenger.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.usecase.user.UserUseCase
import com.plop.plopmessenger.domain.util.Resource
import com.plop.plopmessenger.presentation.state.UserState
import com.plop.plopmessenger.util.UriUtil.toFile
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ModifyProfileScreenViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    @ApplicationContext val context: Context
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
        val image = toFile(context, modifyProfileState.value.profileImg!! )
        viewModelScope.launch {
            userUseCase.modifyUserUseCase(
                nickname = modifyProfileState.value.nickname.text,
                image = image,
                img = modifyProfileState.value.profileImg!!
            ).collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        modifyProfileState.update { it.copy(saved = true, showDialog = false) }
                    }
                    else -> {
                        Log.d("saveUser","error")
                    }
                }
            }

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
    val showDialog: Boolean = false,
    val saved: Boolean = false
)
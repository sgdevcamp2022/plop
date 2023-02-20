package com.plop.plopmessenger.presentation.viewmodel

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.usecase.user.UserUseCase
import com.plop.plopmessenger.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    var findPasswordState = MutableStateFlow(FindPasswordState())
        private set

    fun findPassword() {
        viewModelScope.launch {
            userUseCase.findPasswordUseCase(
                email = findPasswordState.value.emailQuery.text,
                userId = findPasswordState.value.userIdQuery.text,
                password = findPasswordState.value.pwdQuery.text
            ).collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        findPasswordState.update { it.copy(findPasswordState = true) }
                        showFindPasswordDialog()
                    }
                    else -> {
                        findPasswordState.update { it.copy(message = result.message.toString()) }
                        showFindPasswordDialog()
                    }
                }
            }
        }
    }

    fun setEmailQuery(query: TextFieldValue) { findPasswordState.update { it.copy(emailQuery = query) } }

    fun showFindPasswordDialog() { findPasswordState.update { it.copy(showFindPasswordDialog = true) } }

    fun closeDialog() { findPasswordState.update { it.copy(showFindPasswordDialog = false) } }

    fun setPwdQuery(query: TextFieldValue) { findPasswordState.update { it.copy(pwdQuery = query) } }

    fun setUserIdQuery(query: TextFieldValue) { findPasswordState.update { it.copy(userIdQuery = query) } }

    fun setEmailState(isFocus: Boolean) { findPasswordState.update { it.copy(emailTextFieldFocusState = isFocus) } }

    fun setPwdState(isFocus: Boolean) { findPasswordState.update { it.copy(pwdTextFieldFocusState = isFocus) } }

    fun setUserIdState(isFocus: Boolean) { findPasswordState.update { it.copy(userIdTextFieldFocusState = isFocus) } }
}

data class FindPasswordState(
    val emailQuery: TextFieldValue = TextFieldValue(""),
    val pwdQuery: TextFieldValue = TextFieldValue(""),
    val userIdQuery: TextFieldValue = TextFieldValue(""),
    val emailTextFieldFocusState: Boolean = false,
    val pwdTextFieldFocusState: Boolean = false,
    val userIdTextFieldFocusState: Boolean = false,
    val showFindPasswordDialog: Boolean = false,
    val findPasswordState: Boolean = false,
    val message: String = ""
)
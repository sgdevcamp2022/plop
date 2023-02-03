package com.plop.plopmessenger.presentation.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    var loginState = MutableStateFlow(LoginState())
        private set

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            userRepository.getRefreshToken().collect(){
                if(!it.isNullOrBlank()) {
                    loginState.update {
                        it.copy(isLogin = true)
                    }
                }
            }
        }
    }

    fun setEmailQuery(query: TextFieldValue) {
        loginState.update {
            it.copy(emailQuery = query)
        }
    }

    fun setPwdQuery(query: TextFieldValue) {
        loginState.update {
            it.copy(pwdQuery = query)
        }
    }

    fun setEmailState(isFocus: Boolean) {
        loginState.update {
            it.copy(emailTextFieldFocusState = isFocus)
        }
    }

    fun setPwdState(isFocus: Boolean) {
        loginState.update {
            it.copy(pwdTextFieldFocusState = isFocus)
        }
    }
}

data class LoginState(
    val emailQuery: TextFieldValue = TextFieldValue(""),
    val pwdQuery: TextFieldValue = TextFieldValue(""),
    val isLogin: Boolean = false,
    val emailTextFieldFocusState: Boolean = false,
    val pwdTextFieldFocusState: Boolean = false,
)
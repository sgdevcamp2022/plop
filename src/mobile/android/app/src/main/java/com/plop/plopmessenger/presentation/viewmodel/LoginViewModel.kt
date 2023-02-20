package com.plop.plopmessenger.presentation.viewmodel

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.usecase.friend.FriendUseCase
import com.plop.plopmessenger.domain.usecase.user.UserUseCase
import com.plop.plopmessenger.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userUseCase: UserUseCase
): ViewModel() {

    var loginState = MutableStateFlow(LoginState())
        private set

    init {
        checkToken()
    }

    fun getMyInfo() {
        viewModelScope.launch {
            userUseCase.getUserProfileUseCase(loginState.value.emailQuery.text).collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        if(result.data != null) saveMyInfo(result.data)
                    }
                    else -> {
                        Log.d("Login", result.message.toString())
                        showLoginDialog()
                    }
                }
            }
        }
    }

    private fun saveMyInfo(people: People) {
        viewModelScope.launch {
            launch { userUseCase.setNicknameUseCase(people.nickname) }
            launch { userUseCase.setProfileImgUseCase(people.profileImg) }
            launch { userUseCase.setUserIdUseCase(people.peopleId) }
        }
    }

    fun login() {
        viewModelScope.launch {
            userUseCase.loginUseCase(
                email = loginState.value.emailQuery.text,
                password = loginState.value.pwdQuery.text,
            ).collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        setLoginState(true)
                        getMyInfo()
                        Log.d("Login", "로그인성공")
                    }
                    else -> {
                        Log.d("Login", result.message.toString())
                        showLoginDialog()
                    }
                }
            }
        }
    }

    fun setLoginState(state: Boolean) {
        loginState.update { it.copy(isLogin = state) }
    }

    private fun checkToken() {
        viewModelScope.launch {
            userRepository.getUser().collect(){
                if(!it.refreshToken.isNullOrBlank() && !loginState.value.isLogin) {
                    autoLogin(it.email)
                }
            }
        }
    }

    private suspend fun autoLogin(email: String) {
        userUseCase.autoLoginUseCase(email).collect() { result ->
            when(result) {
                is Resource.Success -> {
                    setLoginState(true)
                }
                else -> {
                    Log.d("AutoLogin", "error")
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

    fun showLoginDialog() { loginState.update { it.copy(showLoginDialog = true) } }

    fun closeDialog() { loginState.update { it.copy(showLoginDialog = false) } }
}

data class LoginState(
    val emailQuery: TextFieldValue = TextFieldValue(""),
    val pwdQuery: TextFieldValue = TextFieldValue(""),
    val isLogin: Boolean = false,
    val emailTextFieldFocusState: Boolean = false,
    val pwdTextFieldFocusState: Boolean = false,
    val message: String = "",
    val showLoginDialog: Boolean = false,
)
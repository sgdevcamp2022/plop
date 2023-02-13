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
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userUseCase: UserUseCase
): ViewModel() {

    var signUpState = MutableStateFlow(SignUpState())
        private set

    init {
    }

    fun signUp(){
        viewModelScope.launch{
            userUseCase.signUpUseCase(
                email = signUpState.value.emailQuery.text,
                password = signUpState.value.pwdQuery.text,
                nickname = signUpState.value.nicknameQuery.text,
                userId = signUpState.value.userIdQuery.text,
            ).collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        signUpState.update { it.copy( signUpState = true ) }
                        showSignUpDialog()
                    }
                    else -> {
                        signUpState.update { it.copy(message = result.message.toString()) }
                        showSignUpDialog()
                        Log.d("SignUp", result.message.toString())
                    }
                }
            }
        }
    }


    fun setEmailQuery(query: TextFieldValue) {
        signUpState.update {
            it.copy(emailQuery = query)
        }
    }

    fun showSignUpDialog() {
        signUpState.update { it.copy(showSignUpDialog = true) }
    }

    fun closeDialog() {
        signUpState.update { it.copy(showSignUpDialog = false) }
    }

    fun setPwdQuery(query: TextFieldValue) {
        signUpState.update {
            it.copy(pwdQuery = query)
        }
    }
    
    fun setNicknameQuery(query: TextFieldValue) {
        signUpState.update {
            it.copy(nicknameQuery = query)
        }
    }
    
    fun setUserIdQuery(query: TextFieldValue) {
        signUpState.update {
            it.copy(userIdQuery = query)
        }
    }

    fun setEmailState(isFocus: Boolean) {
        signUpState.update {
            it.copy(emailTextFieldFocusState = isFocus)
        }
    }

    fun setPwdState(isFocus: Boolean) {
        signUpState.update {
            it.copy(pwdTextFieldFocusState = isFocus)
        }
    }

    fun setNicknameState(isFocus: Boolean) {
        signUpState.update {
            it.copy(nicknameTextFieldFocusState = isFocus)
        }
    }

    fun setUserIdState(isFocus: Boolean) {
        signUpState.update {
            it.copy(userIdTextFieldFocusState = isFocus)
        }
    }
}

data class SignUpState(
    val emailQuery: TextFieldValue = TextFieldValue(""),
    val pwdQuery: TextFieldValue = TextFieldValue(""),
    val nicknameQuery: TextFieldValue = TextFieldValue(""),
    val userIdQuery: TextFieldValue = TextFieldValue(""),
    val emailTextFieldFocusState: Boolean = false,
    val pwdTextFieldFocusState: Boolean = false,
    val nicknameTextFieldFocusState: Boolean = false,
    val userIdTextFieldFocusState: Boolean = false,
    val showSignUpDialog: Boolean = false,
    val signUpState: Boolean = false,
    val message: String = ""
)
package com.plop.plopmessenger.presentation.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.usecase.friend.FriendUseCase
import com.plop.plopmessenger.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddChatViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase,
): ViewModel() {
    var addChatState = MutableStateFlow(AddChatState())
        private set

    init {
        getFriendList()
    }

    private fun getFriendList() {
        viewModelScope.launch {
            friendUseCase.getFriendListUseCase().collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        addChatState.update {
                            it.copy(
                                friends = result.data ?: emptyList(),
                                isLoading = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        addChatState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Error -> {
                        addChatState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun getFriendByNickname() {
        viewModelScope.launch {
            friendUseCase.getFriendByNicknameListUseCase(nickname = addChatState.value.query.text)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            addChatState.update {
                                it.copy(
                                    friends = result.data ?: emptyList(),
                                    isLoading = false,
                                )
                            }
                        }
                        is Resource.Loading -> {
                            addChatState.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resource.Error -> {
                            addChatState.update {
                                it.copy(isLoading = false)
                            }
                        }
                    }
                }
        }
    }

    fun setQuery(query: TextFieldValue) {
        addChatState.update {
            it.copy(query = query)
        }
        if(query != TextFieldValue("")) getFriendByNickname()
    }

    fun setFocusState(isFocus: Boolean) {
        addChatState.update {
            it.copy(textFieldFocusState = isFocus)
        }
    }
}

data class AddChatState(
    val friends: List<People> = emptyList(),
    var query: TextFieldValue = TextFieldValue(""),
    val result: List<People> = emptyList(),
    val textFieldFocusState: Boolean = false,
    val isLoading: Boolean = false
)

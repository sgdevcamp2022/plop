package com.plop.plopmessenger.presentation.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddChatViewModel @Inject constructor(
    private val friendRepository: FriendRepository
): ViewModel() {
    var addChatState = MutableStateFlow(AddGroupChatState())
        private set

    init {
        getFriendList()
    }

    private fun getFriendList() {
        viewModelScope.launch {
            friendRepository.loadFriend().collect { result ->
                addChatState.update {
                    it.copy(friends = result.map { it.toPeople() })
                }
            }
        }
    }

    private fun getFriendByNickname() {
        viewModelScope.launch {
            friendRepository.loadFriendByNickname(nickname = addChatState.value.query.text)
                .collectLatest { result ->
                    addChatState.update {
                        it.copy(result = result.map { it.toPeople() })
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
    val textFieldFocusState: Boolean = false
)

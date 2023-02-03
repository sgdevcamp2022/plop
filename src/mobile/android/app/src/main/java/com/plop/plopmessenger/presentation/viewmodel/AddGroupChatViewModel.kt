package com.plop.plopmessenger.presentation.viewmodel

import android.util.Log
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
class AddGroupChatViewModel @Inject constructor(
    private val friendRepository: FriendRepository
): ViewModel() {
    var addGroupChatState = MutableStateFlow(AddGroupChatState())
        private set

    init {
        getFriendList()
    }

    private fun getFriendList() {
        viewModelScope.launch {
            friendRepository.loadFriend().collect { result ->
                addGroupChatState.update {
                    it.copy(friends = result.map { it.toPeople() })
                }
            }
        }
    }

    private fun getFriendByNickname() {
        viewModelScope.launch {
            friendRepository.loadFriendByNickname(nickname = addGroupChatState.value.query.text)
                .collectLatest { result ->
                    addGroupChatState.update {
                        it.copy(result = result.map { it.toPeople() })
                    }
                }
        }
    }

    fun addPeople(people: People) {
        addGroupChatState.update {
            it.copy(checkedPeople = addGroupChatState.value.checkedPeople.plus(people))
        }
    }

    fun deletePeople(people: People) {
        addGroupChatState.update {
            it.copy(checkedPeople = addGroupChatState.value.checkedPeople.minusElement(people))
        }
    }

    fun setQuery(query: TextFieldValue) {
        addGroupChatState.update {
            it.copy(query = query)
        }
        if(query != TextFieldValue("")) getFriendByNickname()
    }

    fun setFocusState(isFocus: Boolean) {
        addGroupChatState.update {
            it.copy(textFieldFocusState = isFocus)
        }
    }
}

data class AddGroupChatState(
    val checkedPeople: List<People> = emptyList(),
    val friends: List<People> = emptyList(),
    var query: TextFieldValue = TextFieldValue(""),
    val result: List<People> = emptyList(),
    val textFieldFocusState: Boolean = false
)
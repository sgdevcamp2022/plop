package com.plop.plopmessenger.presentation.viewmodel

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.ChatRoomType
import com.plop.plopmessenger.domain.model.Member
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.usecase.chatroom.ChatRoomUseCase
import com.plop.plopmessenger.domain.usecase.friend.FriendUseCase
import com.plop.plopmessenger.domain.util.Resource
import com.plop.plopmessenger.presentation.navigation.DestinationID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddChatMemberViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase,
    private val chatRoomUseCase: ChatRoomUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var addChatMemberState = MutableStateFlow(AddChatMemeberState(chatId = savedStateHandle.get<String>(
        DestinationID.CHAT_ID)?: null))
        private set

    init {
        if(!addChatMemberState.value.chatId.isNullOrBlank()) {
            getFriendList()
            getChatroomInfo()
        }
    }

    private fun getFriendList() {
        viewModelScope.launch {
            friendUseCase.getFriendListUseCase().collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        addChatMemberState.update {
                            it.copy(
                                friends = result.data ?: emptyList(),
                                isLoading = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        addChatMemberState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Error -> {
                        addChatMemberState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun addChatMember() {
        viewModelScope.launch {
            val result = chatRoomUseCase.inviteMemberUseCase(
                addChatMemberState.value.chatId!!,
                addChatMemberState.value.checkedPeople
            )

            when(result) {
                is Resource.Success -> {
                    Log.d("AddChatMember", "성공...성공이오..")
                }
                else -> {
                    Log.d("AddChatMember", "실패...실패이오..")
                }
            }
        }
    }

    private fun getFriendByNickname() {
        viewModelScope.launch {
            friendUseCase.getFriendByNicknameListUseCase(addChatMemberState.value.query.text)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            addChatMemberState.update {
                                it.copy(
                                    friends = result.data ?: emptyList(),
                                    isLoading = false,
                                )
                            }
                        }
                        is Resource.Loading -> {
                            addChatMemberState.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resource.Error -> {
                            addChatMemberState.update {
                                it.copy(isLoading = false)
                            }
                        }
                    }
                }


        }
    }

    private fun getChatroomInfo() {
        viewModelScope.launch {
            val result = chatRoomUseCase.getChatRoomInfoUseCase(addChatMemberState.value.chatId!!)
            when (result) {
                is Resource.Success -> {
                    addChatMemberState.update {
                        it.copy(
                            chatRoomType = result.data?.type ?: ChatRoomType.DM,
                            isLoading = false,
                            members = result.data?.members ?: emptyList()
                        )
                    }
                }
                is Resource.Loading -> {
                    addChatMemberState.update {
                        it.copy(isLoading = true)
                    }
                }
                is Resource.Error -> {
                    addChatMemberState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun addPeople(people: People) {
        addChatMemberState.update {
            it.copy(checkedPeople = addChatMemberState.value.checkedPeople.plus(people))
        }
    }

    fun deletePeople(people: People) {
        addChatMemberState.update {
            it.copy(checkedPeople = addChatMemberState.value.checkedPeople.minusElement(people))
        }
    }

    fun setQuery(query: TextFieldValue) {
        addChatMemberState.update {
            it.copy(query = query)
        }
        if(query != TextFieldValue("")) getFriendByNickname()
    }

    fun setFocusState(isFocus: Boolean) {
        addChatMemberState.update {
            it.copy(textFieldFocusState = isFocus)
        }
    }
}

data class AddChatMemeberState(
    val isLoading: Boolean = false,
    val checkedPeople: List<People> = emptyList(),
    val friends: List<People> = emptyList(),
    var query: TextFieldValue = TextFieldValue(""),
    val result: List<People> = emptyList(),
    val textFieldFocusState: Boolean = false,
    val members: List<Member> = emptyList(),
    val chatRoomType: ChatRoomType = ChatRoomType.DM,
    val chatId: String?
)

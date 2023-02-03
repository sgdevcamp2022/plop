package com.plop.plopmessenger.presentation.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.*
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.repository.MessageRepository
import com.plop.plopmessenger.presentation.navigation.DestinationID
import com.plop.plopmessenger.presentation.screen.main.InputSelector
import com.plop.plopmessenger.util.getChatRoomTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val chatRoomRepository: ChatRoomRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var chatState = MutableStateFlow(ChatState(chatroomId = savedStateHandle.get<String>(
        DestinationID.CHAT_ID)?: null))
        private set

    init {
        if(!chatState.value.chatroomId.isNullOrBlank()){
            getMemberList()
            getMessageList()
            getChatTitle()
        }
        getUserId()
    }

    private fun getMessageList() {
        viewModelScope.launch {
            messageRepository.loadChatMessage(chatState.value.chatroomId!!).collect { result ->
                chatState.update {
                    it.copy(messages = result.map { it.toMessage() })
                }
            }
        }
    }

    private fun getMemberList() {
        viewModelScope.launch {
            memberRepository.loadChatMember(chatState.value.chatroomId!!).collect { result ->
                chatState.update {
                    it.copy(members = result.map { it.memberId to it.toMember() }.toMap())
                }
            }
        }
    }

    fun setQuery(query: TextFieldValue) {
        chatState.update {
            it.copy(query = query)
        }
    }

    fun setFocusState(isFocus: Boolean) {
        chatState.update {
            it.copy(textFieldFocusState = isFocus)
        }
    }

    fun setInputSelector(inputSelector: InputSelector) {
        chatState.update {
            it.copy(currentInputSelector = inputSelector)
        }
    }

    private fun getUserId() {
        viewModelScope.launch {
            userRepository.getUserId().collect() { userId ->
                chatState.update {
                    it.copy(userId = userId)
                }
            }
        }
    }

    private fun getChatTitle() {
        viewModelScope.launch {
            chatRoomRepository.loadChatRoomTitle(chatState.value.chatroomId!!).collect() { result ->
                chatState.update {
                    it.copy(title = result)
                }
            }
        }
    }

    fun getMember(people: List<People>) {
        if(chatState.value.chatroomId.isNullOrBlank()) {
            if(people.size == 1) {
                viewModelScope.launch {
                    chatRoomRepository.hasPersonalChatRoomByFriend(people.first().peopleId).collect() { result ->
                        if(result.isNullOrBlank()) {
                            chatState.update { it.copy(
                                title = people.first().nickname,
                                members = mapOf(people.first().peopleId to people.first().toMember()),
                                chatRoomType = ChatRoomType.DM
                            )
                            }
                        }else {
                            chatState.update { it.copy(chatroomId = result, title = people.first().nickname) }
                            getMessageList()
                            getMemberList()
                        }
                    }
                }
            } else {
                chatState.update {
                    it.copy(
                        title = getChatRoomTitle(people.map { it.nickname }),
                        members = people.map { it.peopleId to it.toMember() }.toMap(),
                        chatRoomType = ChatRoomType.GROUP
                    )
                }
            }
        }else {
            chatState.update {
                var result = it.members.toMutableMap()
                people.forEach { people -> result[people.peopleId] = people.toMember() }
                it.copy(
                    members = result,
                    chatRoomType = ChatRoomType.GROUP
                )
            }
        }
    }
}

data class ChatState(
    val members: Map<String, Member> = mapOf(),
    val messages: List<Message> = emptyList(),
    var chatroomId: String? = null,
    val title: String = "",
    var query: TextFieldValue = TextFieldValue(""),
    val textFieldFocusState: Boolean = false,
    val currentInputSelector: InputSelector = InputSelector.NONE,
    val chatRoomType: ChatRoomType = ChatRoomType.DM,
    val userId: String = ""
)

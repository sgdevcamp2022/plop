package com.plop.plopmessenger.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.ChatRoomType
import com.plop.plopmessenger.domain.model.Member
import com.plop.plopmessenger.domain.model.toChatRoom
import com.plop.plopmessenger.domain.model.toMember
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.presentation.navigation.DestinationID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatInfoViewModel @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var chatInfoState = MutableStateFlow(ChatInfoState(chatroomId = savedStateHandle.get<String>(
        DestinationID.CHAT_ID)?: null))
        private set

    init {
        if(!chatInfoState.value.chatroomId.isNullOrBlank()){
            getChatroomInfo()
        }
    }

    private fun getChatroomInfo() {
        viewModelScope.launch {
            chatRoomRepository.loadChatRoomAndMemberById(chatInfoState.value.chatroomId!!).collect { result ->
                chatInfoState.update {
                    it.copy(
                        title = result.chatroom.title,
                        members = result.images.map { it.toMember() },
                        roomType = result.toChatRoom().type
                    )
                }
            }
        }
    }
}

data class ChatInfoState(
    var title: String = "",
    var members: List<Member> = emptyList(),
    var chatroomId: String? = null,
    var roomType: ChatRoomType = ChatRoomType.DM
)
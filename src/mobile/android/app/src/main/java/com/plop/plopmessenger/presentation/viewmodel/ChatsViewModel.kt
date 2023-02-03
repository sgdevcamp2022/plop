package com.plop.plopmessenger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.ChatRoom
import com.plop.plopmessenger.domain.model.toChatRoom
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
): ViewModel() {
    var chatsState = MutableStateFlow(ChatsState())
        private set

    init {
        getChats()
    }

    private fun getChats() {
        viewModelScope.launch {
            chatRoomRepository.loadChatRoomAndMessage().collect { result ->
                chatsState.update {
                    it.copy(chats = result.map {
                        it.toChatRoom()
                    })
                }
            }
        }
    }
}

data class ChatsState(
    val chats: List<ChatRoom> = emptyList()
)
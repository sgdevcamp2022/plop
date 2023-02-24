package com.plop.plopmessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.ChatRoom
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toChatRoom
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.usecase.chatroom.ChatRoomUseCase
import com.plop.plopmessenger.domain.usecase.friend.FriendUseCase
import com.plop.plopmessenger.domain.usecase.presence.GetPresenceUserUseCase
import com.plop.plopmessenger.domain.usecase.presence.PresenceUseCase
import com.plop.plopmessenger.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val chatRoomUseCase: ChatRoomUseCase,
    private val presenceUserUseCase: PresenceUseCase,
    private val friendUseCase: FriendUseCase
): ViewModel() {
    var chatsState = MutableStateFlow(ChatsState())
        private set

    init {
        viewModelScope.launch {
            getRemoteChatRoom()
            getLocalChatRooms()
            getFriendList()
            getPresence()
        }
    }

    private suspend fun getRemoteChatRoom() {
        Log.d("getRemoteChatRoom", "실행")
        when(chatRoomUseCase.getRemoteChatRoomUseCase()) {
            is Resource.Success -> {
                Log.d("getRemoteChatRoom", "성공스")
            }
            else -> {

            }
        }
    }

    private fun getLocalChatRooms() {
        viewModelScope.launch {
            chatRoomUseCase.getLocalChatRoomListUseCase().collect(){ result ->
                when (result) {
                    is Resource.Success -> {
                        chatsState.update {
                            it.copy(chats = result.data?: emptyList())
                        }
                    }
                    is Resource.Loading -> {
                        chatsState.update {
                            it.copy()
                        }
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    private suspend fun getFriendList() {
        viewModelScope.launch {
            when(friendUseCase.getRemoteFriendListUseCase()) {
                is Resource.Success -> {
                }
                else -> {
                    Log.d("GetRemoteFriendList", "error")
                }
            }
        }.join()
    }

    private fun getPresence() {
        viewModelScope.launch {
            presenceUserUseCase.getPresenceUserUseCase().collect() { result ->
                when(result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> TODO()
                    is Resource.Success -> {
                        chatsState.update { it.copy(presence = result.data?: emptyList()) }
                    }
                }
            }
        }
    }
}

data class ChatsState(
    val chats: List<ChatRoom?> = emptyList(),
    val presence: List<People> = emptyList(),
    val isLoading: Boolean = false
)
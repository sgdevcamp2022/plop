package com.plop.plopmessenger.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.ChatRoomType
import com.plop.plopmessenger.domain.model.Member
import com.plop.plopmessenger.domain.model.toChatRoom
import com.plop.plopmessenger.domain.model.toMember
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.usecase.chatroom.ChatRoomUseCase
import com.plop.plopmessenger.domain.util.Resource
import com.plop.plopmessenger.presentation.navigation.DestinationID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatInfoViewModel @Inject constructor(
    private val chatRoomUseCase: ChatRoomUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var chatInfoState = MutableStateFlow(ChatInfoState(chatroomId = savedStateHandle.get<String>(
        DestinationID.CHAT_ID)?: null))
        private set

    init {
        if(!chatInfoState.value.chatroomId.isNullOrBlank()){
            getLocalChatroomInfo()
        }
    }

    private fun getLocalChatroomInfo() {
        viewModelScope.launch {
            val result = chatRoomUseCase.getChatRoomInfoUseCase(chatInfoState.value.chatroomId!!)
            when (result) {
                is Resource.Success -> {
                    chatInfoState.update {
                        it.copy(
                            title = result.data?.title?: "",
                            members = result.data?.members?: emptyList(),
                            roomType = result.data?.type?: ChatRoomType.DM,
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {
                    chatInfoState.update {
                        it.copy(isLoading = true)
                    }
                }
                is Resource.Error -> {
                    chatInfoState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }
}

data class ChatInfoState(
    var isLoading: Boolean = false,
    var title: String = "",
    var members: List<Member> = emptyList(),
    var chatroomId: String? = null,
    var roomType: ChatRoomType = ChatRoomType.DM
)
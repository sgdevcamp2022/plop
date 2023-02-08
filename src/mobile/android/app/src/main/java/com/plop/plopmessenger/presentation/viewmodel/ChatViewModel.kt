package com.plop.plopmessenger.presentation.viewmodel

import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.*
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.usecase.chatroom.ChatRoomUseCase
import com.plop.plopmessenger.domain.usecase.message.MessageUseCase
import com.plop.plopmessenger.domain.util.Resource
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
    private val messageUseCase: MessageUseCase,
    private val userRepository: UserRepository,
    private val chatRoomUseCase: ChatRoomUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var chatState = MutableStateFlow(ChatState(chatroomId = savedStateHandle.get<String>(
        DestinationID.CHAT_ID)?: null))
        private set

    var image = MutableStateFlow<Bitmap?>(null)

    init {
        if(!chatState.value.chatroomId.isNullOrBlank()){
            getMessageList()
            getChatroomInfo()
        }
        getUserId()
    }

    private fun getMessageList() {
        viewModelScope.launch {
            messageUseCase.getLocalMessageListUseCase(chatState.value.chatroomId!!).collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        chatState.update {
                            it.copy(
                                isLoading = false,
                                messages = result.data?: emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        chatState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Error -> {
                        chatState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }

            }
        }
    }

    private fun getChatroomInfo() {
        viewModelScope.launch {
            chatRoomUseCase.getChatRoomInfoUseCase(chatState.value.chatroomId!!).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        chatState.update {
                            it.copy(
                                chatRoomType = result.data?.type ?: ChatRoomType.DM,
                                isLoading = false,
                                members = result.data?.members?.map { it.memberId to it }?.toMap() ?: mapOf(),
                                title = result.data?.title ?: ""
                            )
                        }
                    }
                    is Resource.Loading -> {
                        chatState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Error -> {
                        chatState.update {
                            it.copy(isLoading = false)
                        }
                    }
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

    fun getMember(people: List<People>) {
        if(chatState.value.chatroomId.isNullOrBlank()) {
            if(people.size == 1) {
                viewModelScope.launch {
                    chatRoomUseCase.getChatRoomIdByPeopleIdUseCase(people.first().peopleId).collect() { result ->
                        if(result.data.isNullOrBlank()) {
                            chatState.update { it.copy(
                                title = people.first().nickname,
                                members = mapOf(people.first().peopleId to people.first().toMember()),
                                chatRoomType = ChatRoomType.DM
                                )
                            }
                        }else {
                            chatState.update { it.copy(chatroomId = result.data) }
                            getMessageList()
                            getChatroomInfo()
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

    fun setImage(bitmap: Bitmap?) {
        image.value = bitmap
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
    val userId: String = "",
    val isLoading: Boolean = false
)

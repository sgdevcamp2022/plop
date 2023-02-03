package com.plop.plopmessenger.presentation.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.ChatRoomType
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toChatRoom
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.presentation.navigation.DestinationID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddChatMemberViewModel @Inject constructor(
    private val friendRepository: FriendRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var addChatMemberState = MutableStateFlow(AddChatMemeberState(chatId = savedStateHandle.get<String>(
        DestinationID.CHAT_ID)?: null))
        private set

    init {
        if(!addChatMemberState.value.chatId.isNullOrBlank()) {
            getFriendList()
            getMemberId()
            getChatroomInfo()
        }
    }

    private fun getFriendList() {
        viewModelScope.launch {
            friendRepository.loadFriend().collect { result ->
                addChatMemberState.update {
                    it.copy(friends = result.map { it.toPeople() })
                }
            }
        }
    }

    private fun getMemberId() {
        viewModelScope.launch {
            memberRepository.loadChatMemberId(addChatMemberState.value.chatId!!).collect() { result ->
                addChatMemberState.update {
                    it.copy(members = result.map { it.toPeople() })
                }
            }
        }
    }

    private fun getFriendByNickname() {
        viewModelScope.launch {
            friendRepository.loadFriendByNickname(nickname = addChatMemberState.value.query.text)
                .collectLatest { result ->
                    addChatMemberState.update {
                        it.copy(result = result.map { it.toPeople() })
                    }
                }
        }
    }

    private fun getChatroomInfo() {
        viewModelScope.launch {
            chatRoomRepository.loadChatRoomAndMemberById(addChatMemberState.value.chatId!!).collect { result ->
                addChatMemberState.update {
                    it.copy(
                        chatRoomType = result.toChatRoom().type
                    )
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
    val checkedPeople: List<People> = emptyList(),
    val friends: List<People> = emptyList(),
    var query: TextFieldValue = TextFieldValue(""),
    val result: List<People> = emptyList(),
    val textFieldFocusState: Boolean = false,
    val members: List<People> = emptyList(),
    val chatRoomType: ChatRoomType = ChatRoomType.DM,
    val chatId: String?
)

package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImage
import kotlinx.coroutines.flow.Flow

interface ChatRoomRepository {
    fun loadChatRoomTitle(chatroomId: String): Flow<String>
    fun loadChatRoomAndMessage(): Flow<List<ChatRoomMemberImage>>
    fun loadChatRoomAndMemberById(chatroomId: String): Flow<ChatRoomMemberImage>
    fun hasPersonalChatRoomByFriend(friendId: String): Flow<String?>
}
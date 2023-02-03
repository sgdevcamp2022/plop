package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImage
import com.plop.plopmessenger.data.local.entity.ChatRoom
import kotlinx.coroutines.flow.Flow
import java.util.*

interface ChatRoomRepository {
    fun loadChatRoomTitle(chatroomId: String): Flow<String>
    fun loadChatRoomAndMessage(): Flow<List<ChatRoomMemberImage>>
    fun loadChatRoomAndMemberById(chatroomId: String): Flow<ChatRoomMemberImage>
    fun hasPersonalChatRoomByFriend(friendId: String): Flow<String?>
    fun insertChatRoom(chatRoom: ChatRoom)
    fun insertAllChatRoom(chatRooms: List<ChatRoom>)
    fun updateChatRoom(chatroom: ChatRoom)
    fun updateChatroomAll(chatrooms: List<ChatRoom>)
    fun updateChatRoomTitleById(chatroomId: String, title: String)
    fun updateChatRoomUnreadById(chatroomId: String, unread: Int)
    fun plusChatRoomUnreadById(chatroomId: String, unread: Int)
    fun updateChatRoomContentById(chatroomId: String, content: String, updatedAt: Date)
    fun deleteChatRoom(chatroomId: String)
}
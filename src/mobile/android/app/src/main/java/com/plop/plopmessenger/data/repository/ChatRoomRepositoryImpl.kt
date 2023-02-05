package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.local.dao.ChatRoomDao
import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImage
import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImageDao
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import java.util.*

class ChatRoomRepositoryImpl (
    private val chatRoomDao: ChatRoomDao,
    private val chatroomMemberImageDao: ChatRoomMemberImageDao
): ChatRoomRepository {
    override fun loadChatRoomTitle(chatroomId: String): Flow<String> {
        return chatRoomDao.loadChatRoomTitle(chatroomId)
    }

    override fun loadChatRoomAndMessage(): Flow<List<ChatRoomMemberImage>> {
        return chatroomMemberImageDao.loadChatRoomAndMessage()
    }

    override fun loadChatRoomAndMemberById(chatroomId: String): Flow<ChatRoomMemberImage> {
        return chatroomMemberImageDao.loadChatRoomAndMemberById(chatroomId)
    }

    override fun hasPersonalChatRoomByFriend(friendId: String): Flow<String?> {
        return chatRoomDao.hasPersonalChatRoomByFriend(friendId)
    }

    override suspend fun insertChatRoom(chatRoom: ChatRoom) {
        return chatRoomDao.insertChatRoom(chatRoom)
    }

    override suspend fun insertAllChatRoom(chatRooms: List<ChatRoom>) {
        return chatRoomDao.insertAllChatRoom(*chatRooms.toTypedArray())
    }

    override suspend fun updateChatRoom(chatroom: ChatRoom) {
        return chatRoomDao.updateChatRoom(chatroom)
    }

    override suspend fun updateChatroomAll(chatrooms: List<ChatRoom>) {
        return chatRoomDao.updateChatroomAll(*chatrooms.toTypedArray())
    }

    override suspend fun updateChatRoomTitleById(chatroomId: String, title: String) {
        return chatRoomDao.updateChatRoomTitleById(chatroomId, title)
    }

    override suspend fun updateChatRoomUnreadById(chatroomId: String, unread: Int) {
        return chatRoomDao.updateChatRoomUnreadById(chatroomId, unread)
    }

    override suspend fun plusChatRoomUnreadById(chatroomId: String, unread: Int) {
        return chatRoomDao.plusChatRoomUnreadById(chatroomId, unread)
    }

    override suspend fun updateChatRoomContentById(chatroomId: String, content: String, updatedAt: Date) {
        return chatRoomDao.updateChatRoomContentById(chatroomId, content, updatedAt)
    }

    override suspend fun deleteChatRoom(chatroomId: String) {
        return chatRoomDao.deleteChatRoom(chatroomId)
    }
}
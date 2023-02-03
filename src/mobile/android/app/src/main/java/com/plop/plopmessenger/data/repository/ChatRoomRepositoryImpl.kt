package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.local.dao.ChatRoomDao
import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImage
import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImageDao
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow

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
}
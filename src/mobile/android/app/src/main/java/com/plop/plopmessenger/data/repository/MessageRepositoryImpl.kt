package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.local.dao.MessageDao
import com.plop.plopmessenger.data.local.entity.Message
import com.plop.plopmessenger.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(
    private val messageDao: MessageDao
): MessageRepository {
    override fun loadChatMessage(chatroomId: String): Flow<List<Message>> {
        return messageDao.loadChatMessage(chatroomId)
    }

    override suspend fun insertMessage(message: Message) {
        return messageDao.insertMessage(message)
    }

    override suspend fun insertAllMessage(messages: List<Message>) {
        return messageDao.insertAllMessage(*messages.toTypedArray())
    }


}
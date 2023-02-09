package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.local.dao.MessageDao
import com.plop.plopmessenger.data.local.entity.Message
import com.plop.plopmessenger.data.remote.api.Constants
import com.plop.plopmessenger.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
): MessageRepository {
    override suspend fun loadChatMessage(chatroomId: String, page: Int): List<Message> {
        return messageDao.loadChatMessage(chatroomId, page, Constants.PAGE_SIZE)
    }

    override fun loadChatFirstMessage(chatroomId: String): Flow<Message> {
        return messageDao.loadChatFirstMessage(chatroomId)
    }

    override suspend fun insertMessage(message: Message) {
        return messageDao.insertMessage(message)
    }

    override suspend fun insertAllMessage(messages: List<Message>) {
        return messageDao.insertAllMessage(*messages.toTypedArray())
    }


}
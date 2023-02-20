package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.local.entity.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun loadChatMessage(chatroomId: String, page: Int): List<Message>
    fun loadChatFirstMessage(chatroomId: String): Flow<Message?>
    suspend fun insertMessage(message: Message)
    suspend fun insertAllMessage(messages: List<Message>)
}
package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.local.entity.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun loadChatMessage(chatroomId: String): Flow<List<Message>>
    suspend fun insertMessage(message: Message)
    suspend fun insertAllMessage(messages: List<Message>)
}
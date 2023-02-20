package com.plop.plopmessenger.domain.usecase.socket

import android.util.Log
import com.plop.plopmessenger.data.dto.request.chat.PostMessageRequest
import com.plop.plopmessenger.domain.model.MessageType
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import javax.inject.Inject

class sendMessageUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(roomId: String, content: String, userId: String) {
        chatRoomRepository.postMessage(
            postMessageRequest = PostMessageRequest(
                roomId = roomId,
                messageType = MessageType.TEXT.toString(),
                content = content,
                senderId = userId
            )
        )
    }
}
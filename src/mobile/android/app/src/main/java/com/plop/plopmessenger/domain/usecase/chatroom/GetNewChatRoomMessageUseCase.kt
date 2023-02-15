package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.dto.response.toMessage
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MessageRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNewChatRoomMessageUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(roomid: String, readMsgId: String): Resource<Boolean> {
        try {
            val response = chatRoomRepository.getChatroomNewMessage(roomid, readMsgId)
            if(response.isSuccessful) {
                val newMessage = response.body()

                if (newMessage?.getChatRoomNewMessageDto?.isEmpty() == false) {
                    messageRepository.insertAllMessage(newMessage.getChatRoomNewMessageDto.map {
                        it.toMessage()
                    })
                    return Resource.Success(true)
                }
                return Resource.Success(true)
            } else {
                Log.d("GetNewChatRoomMessageUseCase", "실패..실패오..")
                return Resource.Error("error")
            }
        }catch (e: Exception) {
            Log.d("GetNewChatRoomMessageUseCase", e.message.toString())
            return Resource.Error(e.message.toString())
        }
    }
}
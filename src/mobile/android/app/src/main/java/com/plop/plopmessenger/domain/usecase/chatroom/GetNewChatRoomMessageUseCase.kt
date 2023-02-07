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
    operator fun invoke(roomid: String, readMsgId: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = chatRoomRepository.getChatroomNewMessage(roomid, readMsgId)
            when(response.code()){
                200 -> {
                    val newMessage = response.body()

                    if (newMessage?.getChatRoomNewMessageDto?.isEmpty() == false) {
                        emit(Resource.Success(true))
                        messageRepository.insertAllMessage(newMessage.getChatRoomNewMessageDto.map {
                            it.toMessage()
                        })
                    }
                }
                else -> {
                    Log.d("GetNewChatRoomMessageUseCase", response.code().toString())
                }
            }
        }catch (e: Exception) {
            Log.d("GetNewChatRoomMessageUseCase", e.message.toString())
        }
    }
}

private fun MessageTypeConverter(type: String): Int {
    return when(type) {
        "TEXT" -> 1
        "IMG" -> 2
        "VIDEO" -> 3
        "ENTER" -> 4
        else -> 1
    }
}
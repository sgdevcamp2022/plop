package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.dto.response.toMessage
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MessageRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMessageHistoryUseCase @Inject constructor(
    private val chatroomRespository: ChatRoomRepository,
    private val messageRepository: MessageRepository
){
    operator fun invoke(roomid: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = chatroomRespository.getChatroomHistory(roomid)
            when(response.code()) {
                200 -> {
                    emit(Resource.Success(true))
                    val chats = response.body()!!.getHistoryMessageDto
                    messageRepository.insertAllMessage(
                        chats.map {
                            it.toMessage()
                        }
                    )
                }
                else -> {
                    emit(Resource.Error(response.message()))
                    Log.d("GetMessageHistoryUseCase", "error")
                }
            }
        } catch (e: Exception) {
            Log.d("GetMessageHistoryUseCase", e.message.toString())
        }

    }
}
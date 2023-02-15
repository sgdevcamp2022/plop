package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.local.entity.toChatRoom
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRemoteChatRoomUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(): Resource<Boolean> {
        try {
            withContext(Dispatchers.IO) {
                val response = chatRoomRepository.getMyRooms()
                if(response.isSuccessful) {
                    val chatrooms = response.body()
                    if(!chatrooms?.getMyRoomDto.isNullOrEmpty()) {
                        chatRoomRepository.insertAllChatRoom(
                            chatrooms?.getMyRoomDto!!.map { it.toChatRoom() }
                        )
                    } else {

                    }
                } else {
                    Log.d("GetRemoteChatRoomUseCase", "error")
                }
            }
        } catch (e: Exception){
            Log.d("GetRemoteChatRoomUseCase", e.message.toString())
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(true)
    }
}
package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LeaveChatRoomUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(chatRoomId: String): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = chatRoomRepository.deleteChatroom(chatRoomId)
                if(response.isSuccessful){
                    chatRoomRepository.deleteChatRoom(chatRoomId)
                }
                Resource.Success(true)
            } catch (e: Exception) {
                Log.d("LeaveChatRoomUseCase", e.message.toString())
                Resource.Error(e.message.toString())
            }
        }
    }
}
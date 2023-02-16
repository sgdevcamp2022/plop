package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.domain.model.ChatRoom
import com.plop.plopmessenger.domain.model.toChatRoom
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetChatRoomInfoUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val getRemoteChatRoomInfoUseCase: GetRemoteChatRoomInfoUseCase
) {
    suspend operator fun invoke(chatRoomId: String): Resource<ChatRoom> {
        return withContext(Dispatchers.IO) {
            try {
                //getRemoteChatRoomInfoUseCase(chatRoomId)
                Resource.Success(chatRoomRepository.loadChatRoomAndMemberById(chatRoomId).toChatRoom())
            } catch (e: Exception) {
                Log.d("GetChatRoomInfoUseCase", e.message.toString())
                Resource.Error(e.message.toString())
            }
        }
    }
}
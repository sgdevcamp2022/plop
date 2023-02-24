package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteChatRoomsUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke() {
        CoroutineScope(Dispatchers.IO).launch {
            chatRoomRepository.deleteChatRoomData()
        }
    }
}
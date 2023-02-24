package com.plop.plopmessenger.domain.usecase.message

import android.util.Log
import com.plop.plopmessenger.domain.model.Message
import com.plop.plopmessenger.domain.model.toMessage
import com.plop.plopmessenger.domain.repository.MessageRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetLocalMessageListUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(chatroomId: String, page: Int): Flow<Resource<List<Message>>> = flow {
        try {
            emit(Resource.Loading())
            val result = messageRepository.loadChatMessage(chatroomId, page)
            emit(Resource.Success(result.map { it.toMessage() }))
        } catch (e: IOException) {
            Log.d("GetMessageListUseCase", "IOException")
        } catch (e: Exception) {
            Log.d("GetMessageListUseCase", e.message.toString())
        }
    }
}
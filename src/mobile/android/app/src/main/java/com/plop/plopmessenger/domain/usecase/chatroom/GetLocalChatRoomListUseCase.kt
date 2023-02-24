package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.domain.model.ChatRoom
import com.plop.plopmessenger.domain.model.toChatRoom
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetLocalChatRoomListUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
) {
    operator fun invoke(): Flow<Resource<List<ChatRoom?>>> = flow {
        try {
            emit(Resource.Loading())
            chatRoomRepository.loadChatRoomAndMessage().collect(){ result ->
                emit(Resource.Success(result.map { it?.toChatRoom() }))
            }
        } catch (e: IOException) {
            Log.d("getLocalChatRoomListUseCase", "IOException")
        } catch (e: Exception) {
            Log.d("getLocalChatRoomListUseCase", e.message.toString())
        }
    }

}
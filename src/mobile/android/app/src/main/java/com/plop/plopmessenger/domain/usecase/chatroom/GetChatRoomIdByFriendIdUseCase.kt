package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetChatRoomIdByPeopleIdUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
) {
    operator fun invoke(peopleId: String): Flow<Resource<String?>> = flow {
        try {
            emit(Resource.Loading())
            chatRoomRepository.hasPersonalChatRoomByFriend(peopleId).collect(){ result ->
                emit(Resource.Success(result?: null))
            }
        } catch (e: IOException) {
            Log.d("GetChatRoomIdByPeopleId", "IOException")
        } catch (e: Exception) {
            Log.d("GetChatRoomIdByPeopleId", e.message.toString())
        }
    }
}
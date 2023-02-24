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
    suspend operator fun invoke(peopleId: String): Resource<String?> {
        try {
            return Resource.Success(chatRoomRepository.hasPersonalChatRoomByFriend(peopleId))
        } catch (e: IOException) {
            Log.d("GetChatRoomIdByPeopleId", "IOException")
            return  Resource.Error("IOException")
        } catch (e: Exception) {
            return  Resource.Error(e.message.toString())
        }
    }
}
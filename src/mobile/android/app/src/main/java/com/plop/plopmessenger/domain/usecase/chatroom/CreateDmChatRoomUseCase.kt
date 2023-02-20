package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.dto.request.chat.PostDmRoomRequest
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.data.local.entity.Member
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.repository.MessageRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class CreateDmChatRoomUseCase @Inject constructor(
    private val repository: ChatRoomRepository,
    private val memberRepository: MemberRepository,
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(friend: People): Resource<String> {
        try {
            val response = repository.postDmChatroom(PostDmRoomRequest(friend.peopleId))
            if(response.isSuccessful) {
                val chatroom = response.body()
                val createdAt = LocalDateTime.now()

                repository.insertChatRoom(
                    ChatRoom(chatroom?.roomId?:"", friend.nickname, 0, "", createdAt?: LocalDateTime.now(), 1)
                )
                memberRepository.insertMember(
                    Member(chatroom?.roomId?:"", friend.peopleId, friend.nickname, friend.profileImg, null)
                )
                return Resource.Success(chatroom?.roomId?:"")
            }
            else {
                Log.d("CreateDmChatRoomUseCase", "error")
                return Resource.Error(response.message().toString())
            }
        } catch (e: Exception) {
            Log.d("CreateDmChatRoomUseCase", e.message.toString())
            return Resource.Error(e.message.toString())
        }
    }
}
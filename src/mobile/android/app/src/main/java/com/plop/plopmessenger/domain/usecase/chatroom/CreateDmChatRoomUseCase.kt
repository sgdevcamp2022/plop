package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.dto.request.chat.PostDmRoomRequest
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.data.local.entity.Member
import com.plop.plopmessenger.data.local.entity.Message
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.repository.MessageRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class CreateDmChatRoomUseCase @Inject constructor(
    private val repository: ChatRoomRepository,
    private val memberRepository: MemberRepository,
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(friend: People): Flow<Resource<String>> = flow{
        try {
            val response = repository.postDmChatroom(PostDmRoomRequest(friend.peopleId))
            if(response.isSuccessful) {
                val chatroom = response.body()

                if (chatroom?.roomId != null) {
                    repository.insertChatRoom(
                        ChatRoom(chatroom.roomId, friend.nickname, 0, "", LocalDateTime.now(), 1)
                    )
                    memberRepository.insertMember(
                        Member(chatroom.roomId, friend.peopleId, friend.nickname, friend.profileImg, null)
                    )
                    emit(Resource.Success(chatroom.roomId))
                }
            }
            else {
                Log.d("CreateDmChatRoomUseCase", "error")
                emit(Resource.Error(response.message().toString()))
            }
        } catch (e: Exception) {
            Log.d("CreateDmChatRoomUseCase", e.message.toString())
            emit(Resource.Error(e.message.toString()))
        }
    }
}
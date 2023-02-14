package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.dto.request.chat.PostGroupRoomRequest
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.data.local.entity.Member
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.util.Resource
import com.plop.plopmessenger.util.getChatRoomTitle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class CreateGroupChatRoomUseCase @Inject constructor(
    private val repository: ChatRoomRepository,
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(friends: List<People>): Flow<Resource<String>> = flow{
        try {
            val response = repository.postGroupChatroom(PostGroupRoomRequest(friends.map{it.peopleId}))
            if(response.isSuccessful) {
                val chatroom = response.body()
                if (chatroom?.roomId != null) {
                    repository.insertChatRoom(
                        ChatRoom(chatroom.roomId, chatroom.title, 0, "", LocalDateTime.now(), 2)
                    )
                    memberRepository.insertAllMember(
                        friends.map {
                            Member(chatroom.roomId, it.peopleId, it.nickname, it.profileImg, null)
                        }
                    )
                    emit(Resource.Success(chatroom?.roomId))
                    Log.d("CreateGroupChatRoomUseCase","성공..성공이오..")
                }
            }
            else {
                Log.d("CreateGroupChatRoomUseCase", response.code().toString())
            }
        } catch (e: Exception) {
            Log.d("CreateGroupChatRoomUseCase", e.message.toString())
        }
    }
}
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
    suspend operator fun invoke(friends: List<People>): Flow<Resource<Boolean>> = flow{
        try {
            val response = repository.postGroupChatroom(PostGroupRoomRequest(friends.map{it.peopleId}))
            when(response.code()){
                200 -> {
                    val chatroom = response.body()

                    if (chatroom?.roomId != null) {
                        emit(Resource.Success(true))
                        repository.insertChatRoom(
                            ChatRoom(chatroom.roomId, getChatRoomTitle(friends.map{it.nickname}), 0, "", LocalDateTime.now(), 2)
                        )
                        memberRepository.insertAllMember(
                            friends.map {
                                Member(chatroom.roomId, it.peopleId, it.nickname, it.profileImg, null)
                            }
                        )
                    }
                }
                else -> {
                    Log.d("CreateGroupChatRoomUseCase", response.code().toString())
                }
            }
        } catch (e: Exception) {
            Log.d("CreateGroupChatRoomUseCase", e.message.toString())
        }
    }
}
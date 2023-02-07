package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.dto.request.chat.PostDmRoomRequest
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.data.local.entity.Member
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class CreateDmChatRoomUseCase @Inject constructor(
    private val repository: ChatRoomRepository,
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(friend: People): Flow<Resource<String>> = flow{
        try {
            val response = repository.postDmChatroom(PostDmRoomRequest(friend.peopleId))
            when(response.code()){
                200 -> {
                    val chatroom = response.body()

                    if (chatroom?.roomId != null) {
                        emit(Resource.Success())
                        repository.insertChatRoom(
                            ChatRoom(chatroom.roomId, friend.nickname, 0, "", Date(), 1)
                        )
                        memberRepository.insertMember(
                            Member(chatroom.roomId, friend.peopleId, friend.nickname, friend.profileImg, null)
                        )
                    }
                }
                else -> {
                    Log.d("CreateDmChatRoomUseCase", response.code().toString())
                }
            }
        } catch (e: Exception) {
            Log.d("CreateDmChatRoomUseCase", e.message.toString())
        }
    }
}
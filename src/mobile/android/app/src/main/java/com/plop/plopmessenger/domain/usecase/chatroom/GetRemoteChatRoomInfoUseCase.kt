package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.dto.response.toMember
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRemoteChatRoomInfoUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository
) {
    operator fun invoke(roomId: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = chatRoomRepository.getChatRoomInfo(roomId)
            when(response.code()) {
                200 -> {
                    val chatroom = response.body()
                    memberRepository.insertAllMember(chatroom!!.members.map { it.toMember(roomId) })
                    emit(Resource.Success(true))
                }
                else -> {
                    Log.d("GetRemoteChatRoomInfoUseCase", "error")
                }
            }
        } catch (e: Exception){
            Log.d("GetRemoteChatRoomInfoUseCase", e.message.toString())
        }
    }
}
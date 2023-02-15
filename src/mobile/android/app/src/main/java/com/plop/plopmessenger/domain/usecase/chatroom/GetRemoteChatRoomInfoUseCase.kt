package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.dto.response.toMember
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRemoteChatRoomInfoUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(roomId: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = chatRoomRepository.getChatRoomInfo(roomId)
                if(response.isSuccessful) {
                    val chatroom = response.body()
                    memberRepository.insertAllMember(chatroom!!.members.map { it.toMember(roomId) })
                    Resource.Success(true)
                } else {
                    Log.d("GetRemoteChatRoomInfoUseCase", "error")
                    Resource.Error("error")
                }
            } catch (e: Exception){
                Log.d("GetRemoteChatRoomInfoUseCase", e.message.toString())
                Resource.Error("error")
            }
        }
    }
}
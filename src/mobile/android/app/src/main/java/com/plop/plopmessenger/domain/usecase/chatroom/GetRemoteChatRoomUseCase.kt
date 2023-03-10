package com.plop.plopmessenger.domain.usecase.chatroom

import android.util.Log
import com.plop.plopmessenger.data.dto.response.toMember
import com.plop.plopmessenger.data.local.entity.toChatRoom
import com.plop.plopmessenger.data.local.entity.toMember
import com.plop.plopmessenger.domain.model.toMember
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.repository.SocketRepository
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRemoteChatRoomUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository,
    private val socketRepository: SocketRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<Boolean> {
        try {
            withContext(Dispatchers.IO) {
                val response = chatRoomRepository.getMyRooms()
                if(response.isSuccessful) {
                    val chatrooms = response.body()
                    val chatRoomIsEmpty = chatRoomRepository.loadChatRoomIdList().isEmpty()
                    if(!chatrooms?.getMyRoomDto.isNullOrEmpty()) {
                        chatRoomRepository.insertAllChatRoom(
                            chatrooms?.getMyRoomDto?.map { it.toChatRoom() } ?: emptyList()
                        )
                        chatrooms?.getMyRoomDto?.forEach {
                            memberRepository.insertAllMember(
                                it.members.map { member ->
                                    val memberProfile = userRepository.getUserProfile(member.userId).body()
                                    memberProfile?.user?.toMember(it.roomId)!!
                                }
                            )
                        }
                        if(chatRoomIsEmpty) socketRepository.joinAll() else {}
                    } else {

                    }
                } else {
                    Log.d("GetRemoteChatRoomUseCase", "error")
                }

            }
        } catch (e: Exception){
            Log.d("GetRemoteChatRoomUseCase", e.message.toString())
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(true)
    }
}
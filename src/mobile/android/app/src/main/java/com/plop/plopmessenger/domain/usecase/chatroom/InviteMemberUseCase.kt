package com.plop.plopmessenger.domain.usecase.chatroom

import com.plop.plopmessenger.data.dto.request.chat.PostInvitationRequest
import com.plop.plopmessenger.data.local.entity.Member
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InviteMemberUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository
) {
    operator fun invoke(roomid: String, members: List<People>): Flow<Resource<String>> = flow {
        try {
            val response = chatRoomRepository.postInvitation(PostInvitationRequest(members.map { it.peopleId }, roomid))
            when(response.code()) {
                200 -> {
                    val chatroom = response.body()
                    memberRepository.insertAllMember(
                        members.map {
                            Member(roomid, it.peopleId, it.nickname, it.profileImg, null)
                        }
                    )
                    if (chatroom != null) {
                        emit(Resource.Success())
                    }
                }
            }

        }catch (e: Exception){

        }
    }
}
package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun loadChatMemberImage(chatroomId: String): Flow<List<String>>
    fun loadChatMember(chatroomId: String): Flow<List<Member>>
    fun loadChatMemberId(chatroomId: String): Flow<List<Member>>
    suspend fun insertMember(member: Member)
    suspend fun insertAllMember(members: List<Member>)
    suspend fun updateMember(member: Member)
    suspend fun updateMemberLastRead(memberId: String, messageId: String)
    suspend fun insertOrUpdate(member: Member, chatroomId: String)
    suspend fun updateAllMember(members: List<Member>)
    suspend fun deleteMember(member: Member)
}
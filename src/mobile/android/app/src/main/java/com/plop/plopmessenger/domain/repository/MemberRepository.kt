package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun loadChatMemberImage(chatroomId: String): Flow<List<String>>
    fun loadChatMember(chatroomId: String): Flow<List<Member>>
    fun loadChatMemberId(chatroomId: String): Flow<List<Member>>
}
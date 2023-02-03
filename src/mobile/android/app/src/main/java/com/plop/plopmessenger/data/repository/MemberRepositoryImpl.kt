package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.local.dao.MemberDao
import com.plop.plopmessenger.data.local.entity.Member
import com.plop.plopmessenger.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow

class MemberRepositoryImpl(
    private val memberDao: MemberDao
): MemberRepository {
    override fun loadChatMemberImage(chatroomId: String): Flow<List<String>> {
        return memberDao.loadChatMemberImage(chatroomId)
    }

    override fun loadChatMember(chatroomId: String): Flow<List<Member>> {
        return memberDao.loadChatMember(chatroomId)
    }

    override fun loadChatMemberId(chatroomId: String): Flow<List<Member>> {
        return memberDao.loadChatMemberId(chatroomId)
    }
}
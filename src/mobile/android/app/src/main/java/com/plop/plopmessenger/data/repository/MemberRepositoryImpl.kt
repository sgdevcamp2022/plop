package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.local.dao.MemberDao
import com.plop.plopmessenger.data.local.entity.Member
import com.plop.plopmessenger.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
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

    override suspend fun insertMember(member: Member) {
        return memberDao.insertMember(member)
    }

    override suspend fun insertAllMember(members: List<Member>) {
        return memberDao.insertAllMember(*members.toTypedArray())
    }

    override suspend fun updateMember(member: Member) {
        return memberDao.updateMember(member)
    }

    override suspend fun updateMemberLastRead(memberId: String, messageId: String) {
        return memberDao.updateMemberLastRead(memberId, messageId)
    }

    override suspend fun insertOrUpdate(member: Member, chatroomId: String) {
        return memberDao.insertOrUpdate(member = member, chatroomId = chatroomId)
    }

    override suspend fun updateAllMember(members: List<Member>) {
        return memberDao.updateAllMember(*members.toTypedArray())
    }

    override suspend fun deleteMember(member: Member) {
        return memberDao.deleteMember(member)
    }
}
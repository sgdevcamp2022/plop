package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.local.dao.FriendDao
import com.plop.plopmessenger.data.local.entity.Friend
import com.plop.plopmessenger.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow

class FriendRepositoryImpl(
    private val friendDao: FriendDao
): FriendRepository {
    override fun loadFriend(): Flow<List<Friend>> {
        return friendDao.loadFriend()
    }

    override fun loadFriendByNickname(nickname: String): Flow<List<Friend>> {
        return friendDao.loadFriendByNickname(nickname = "%$nickname%")
    }
}
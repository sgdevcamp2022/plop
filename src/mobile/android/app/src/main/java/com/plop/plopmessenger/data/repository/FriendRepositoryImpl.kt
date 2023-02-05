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

    override suspend fun insertFriend(friend: Friend) {
        return friendDao.insertFriend(friend)
    }

    override suspend fun insertAllFriend(friends: List<Friend>) {
        return friendDao.insertAllFriend(*friends.toTypedArray())
    }

    override suspend fun updateFriend(friend: Friend) {
        return friendDao.updateFriend(friend)
    }

    override suspend fun updateFriendStateToBlockById(friendId: String) {
        return friendDao.updateFriendStateToBlockById(friendId)
    }

    override suspend fun updateFriendStateToFriendById(friendId: String) {
        return friendDao.updateFriendStateToFriendById(friendId)
    }

    override suspend fun updateAllFriend(friends: List<Friend>) {
        return friendDao.updateAllFriend(*friends.toTypedArray())
    }
}
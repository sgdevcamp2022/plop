package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.local.entity.Friend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    fun loadFriend(): Flow<List<Friend>>
    fun loadFriendByNickname(nickname: String): Flow<List<Friend>>
    suspend fun insertFriend(friend: Friend)
    suspend fun insertAllFriend(friends: List<Friend>)
    suspend fun updateFriend(friend: Friend)
    suspend fun updateFriendStateToBlockById(friendId: String)
    suspend fun updateFriendStateToFriendById(friendId: String)
    suspend fun updateAllFriend(friends: List<Friend>)
}
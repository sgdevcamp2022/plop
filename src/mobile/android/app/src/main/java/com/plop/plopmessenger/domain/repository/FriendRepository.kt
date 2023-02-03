package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.local.entity.Friend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    fun loadFriend(): Flow<List<Friend>>
    fun loadFriendByNickname(nickname: String): Flow<List<Friend>>
}
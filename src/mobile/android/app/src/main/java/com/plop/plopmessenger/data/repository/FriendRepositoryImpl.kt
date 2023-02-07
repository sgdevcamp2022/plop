package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.dto.request.user.DeleteFriendRequestRequest
import com.plop.plopmessenger.data.dto.request.user.PostFriendRequest
import com.plop.plopmessenger.data.dto.response.user.*
import com.plop.plopmessenger.data.local.dao.FriendDao
import com.plop.plopmessenger.data.local.entity.Friend
import com.plop.plopmessenger.data.remote.api.FriendApi
import com.plop.plopmessenger.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendDao: FriendDao,
    private val friendApi: FriendApi
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

    override suspend fun getFriendList(): Response<GetFriendListResponse> {
        return friendApi.getFriendList()
    }

    override suspend fun postFriendRequest(postFriendRequest: PostFriendRequest): Response<PostFriendResponse> {
        return friendApi.postFriendRequest(postFriendRequest)
    }

    override suspend fun deleteFriendRequest(deleteFriendRequestRequest: DeleteFriendRequestRequest): Response<DeleteFriendRequestResponse> {
        return friendApi.deleteFriendRequest(deleteFriendRequestRequest)
    }

    override suspend fun deleteFriend(friendid: String): Response<DeleteFriendResponse> {
        return friendApi.deleteFriend(friendid)
    }

    override suspend fun putFriendRequest(
        friendid: String,
        status: Boolean
    ): Response<PutFriendResponse> {
        return friendApi.putFriendRequest(friendid, status)
    }
}
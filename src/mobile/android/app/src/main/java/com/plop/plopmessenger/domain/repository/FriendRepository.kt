package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.dto.request.user.DeleteFriendRequestRequest
import com.plop.plopmessenger.data.dto.request.user.PostFriendRequest
import com.plop.plopmessenger.data.dto.response.user.*
import com.plop.plopmessenger.data.local.entity.Friend
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query

interface FriendRepository {
    fun loadFriend(): Flow<List<Friend>>
    fun loadFriendByNickname(nickname: String): Flow<List<Friend>>
    suspend fun insertFriend(friend: Friend)
    suspend fun insertAllFriend(friends: List<Friend>)
    suspend fun updateFriend(friend: Friend)
    suspend fun updateFriendStateToBlockById(friendId: String)
    suspend fun updateFriendStateToFriendById(friendId: String)
    suspend fun updateAllFriend(friends: List<Friend>)
    suspend fun getFriendList(): Response<GetFriendListResponse>
    suspend fun postFriendRequest(postFriendRequest: PostFriendRequest): Response<PostFriendResponse>
    suspend fun deleteFriendRequest(deleteFriendRequestRequest: DeleteFriendRequestRequest): Response<DeleteFriendRequestResponse>
    suspend fun deleteFriend(friendid: String): Response<DeleteFriendResponse>
    suspend fun putFriendRequest(friendid: String, status: Boolean): Response<PutFriendResponse>
}
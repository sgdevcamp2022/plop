package com.plop.plopmessenger.data.remote.api

import com.plop.plopmessenger.data.dto.request.user.DeleteFriendRequestRequest
import com.plop.plopmessenger.data.dto.request.user.PostFriendRequest
import com.plop.plopmessenger.data.dto.response.user.*
import com.plop.plopmessenger.data.remote.api.Constants.DELETE_FRIEND
import com.plop.plopmessenger.data.remote.api.Constants.DELETE_FRIEND_REQUEST
import com.plop.plopmessenger.data.remote.api.Constants.GET_FRIEND_LIST
import com.plop.plopmessenger.data.remote.api.Constants.POST_FRIEND_REQUEST
import com.plop.plopmessenger.data.remote.api.Constants.PUT_FRIEND_REQUEST
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface FriendApi {
    @GET(GET_FRIEND_LIST)
    suspend fun getFriendList(): Response<GetFriendListResponse>

    @POST(POST_FRIEND_REQUEST)
    suspend fun postFriendRequest(
        @Body postFriendRequest: PostFriendRequest
    ): Response<PostFriendResponse>

    @DELETE(DELETE_FRIEND_REQUEST)
    suspend fun deleteFriendRequest(
        @Body deleteFriendRequestRequest: DeleteFriendRequestRequest
    ): Response<DeleteFriendRequestResponse>

    @DELETE(DELETE_FRIEND)
    suspend fun deleteFriend(
        @Query("friendid") friendid: String
    ): Response<DeleteFriendResponse>

    @PUT(PUT_FRIEND_REQUEST)
    suspend fun putFriendRequest(
        @Query("friendid") friendid: String,
        @Query("status") status: Boolean
    ): Response<PutFriendResponse>
}
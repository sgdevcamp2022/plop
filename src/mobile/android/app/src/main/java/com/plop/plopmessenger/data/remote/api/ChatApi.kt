package com.plop.plopmessenger.data.remote.api

import com.plop.plopmessenger.data.dto.request.chat.*
import com.plop.plopmessenger.data.dto.response.chat.*
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {

    @POST(Constants.POST_DM_CHATROOM)
    suspend fun postDmChatroom(
        @Body postDmRoomRequest: PostDmRoomRequest
    ): Response<PostDmRoomResponse>

    @POST(Constants.POST_GROUP_CHATROOM)
    suspend fun postGroupChatroom(
        @Body postGroupRoomRequest: PostGroupRoomRequest
    ): Response<PostGroupRoomResponse>

    @POST(Constants.POST_INVITATION)
    suspend fun postInvitation(
        @Body postInvitationRequest: PostInvitationRequest
    ): Response<PostInvitationResponse>

    @GET(Constants.GET_MY_ROOMS)
    suspend fun getMyRooms(): Response<GetMyRoomResponse>

    @DELETE("${Constants.DELETE_CHATROOM}/{roomid}")
    suspend fun deleteChatroom(
        @Path("roomid") roomid: String
    ): Response<DeleteChatRoomResponse>

    @GET("${Constants.GET_CHATROOM_NEW_MESSAGE}/{roomid}/{readMsgId}")
    suspend fun getChatroomNewMessage(
        @Path("roomid") roomid: String,
        @Path("readMsgId") readMsgId: String,
    ): Response<GetChatRoomNewMessageResponse>

    @GET("${Constants.GET_CHATROOM_HISTORY}/{roomid}")
    suspend fun getChatroomHistory(
        @Path("roomid") roomid: String
    ): Response<GetHistoryMessageResponse>

    @GET("${Constants.GET_CHATROOM_INFO}/{roomid}")
    suspend fun getChatRoomInfo(
        @Path("roomid") roomid: String
    ): Response<GetChatRoomInfoResponse>

    @POST(Constants.POST_MESSAGE)
    suspend fun postMessage(
        @Body postMessageRequest: PostMessageRequest
    ): Response<Void>
}